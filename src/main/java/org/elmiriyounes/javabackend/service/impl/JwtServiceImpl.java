package org.elmiriyounes.javabackend.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.elmiriyounes.javabackend.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

	@Value("${secret_key}")
	private String secretKey;
	@Override
	public String extractUsername(String token) {

		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	@Override
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		if(!userDetails.getAuthorities().isEmpty()){
			List<String> authorities = userDetails.getAuthorities().stream()
					.map(Object::toString)
					.collect(Collectors.toList());

			claims.put("authorities", authorities);
		}

		var token = generateToken(claims, userDetails);
		return token;
	}

	@Override
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				// 1000 ms (=1s) * 60 s (=1 minute) * 60 min (=1 hour) * 24 h (=1 day)
				// 1000 * 60 * 60 * 24 = token will expire 1 day after generated
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	@Override
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	@Override
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	@Override
	public Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public List<SimpleGrantedAuthority> extractAuthorities(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		List<String> authorities = claims.get("authorities", List.class);

		return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
}
