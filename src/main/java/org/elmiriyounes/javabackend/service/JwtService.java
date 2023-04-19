package org.elmiriyounes.javabackend.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
	String extractUsername(String token);
	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);
	String generateToken(UserDetails userDetails);
	String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
	boolean isTokenValid(String token, UserDetails userDetails);
	boolean isTokenExpired(String token);
	Date extractExpiration(String token);
	Claims extractAllClaims(String token);
	Key getSignInKey();
	List<SimpleGrantedAuthority> extractAuthorities(String token);
}
