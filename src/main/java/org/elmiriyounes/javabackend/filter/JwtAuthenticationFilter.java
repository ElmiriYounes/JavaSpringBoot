package org.elmiriyounes.javabackend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.dto.ErrorDTO;
import org.elmiriyounes.javabackend.dto.RequestPathDTO;
import org.elmiriyounes.javabackend.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	private List<RequestPathDTO> existingPaths = Arrays.asList(
			// user path
			RequestPathDTO.builder().method("GET").path("/auth/login").build(),
			RequestPathDTO.builder().method("GET").path("/users/allUsers").build(),
			RequestPathDTO.builder().method("GET").path("/users/allTeachers").build(),
			RequestPathDTO.builder().method("GET").path("/users/allStudents").build(),
			// ^/users/user/[^/\s/]+/?$ == regex to match /users/user/something without space/
			RequestPathDTO.builder().method("GET").path("^/users/user/[^/\\s/]+/?$").build(),
			RequestPathDTO.builder().method("POST").path("/users/saveStudent").build(),
			RequestPathDTO.builder().method("PUT").path("^/users/editStudent/[^/\\s/]+/?$").build(),
			RequestPathDTO.builder().method("GET").path("/users/allUsers").build(),
			RequestPathDTO.builder().method("DELETE").path("^/users/deleteStudent/[^/\\s/]+/?$").build(),
			// course path
			RequestPathDTO.builder().method("GET").path("/courses/allCourses").build(),
			//{idCourse} must be just number
			RequestPathDTO.builder().method("GET").path("^/courses/course/[^/\\s/]+/?$").build(),
			RequestPathDTO.builder().method("POST").path("/courses/saveCourse").build(),
			RequestPathDTO.builder().method("PUT").path("^/courses/editCourse/[^/\\s/]+/?$").build(),
			RequestPathDTO.builder().method("POST").path("^/courses/subscribeStudent/[^/\\s/]+/?$").build(),
			RequestPathDTO.builder().method("POST").path("^/courses/unsubscribeStudent/[^/\\s/]+/?$").build(),
			RequestPathDTO.builder().method("DELETE").path("^/courses/deleteCourse/[^/\\s/]+/?$").build()
	);

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if(
				!existingPaths.stream().anyMatch(
						requestPathDTO -> request.getMethod().equals(requestPathDTO.getMethod())
								&& request.getRequestURI().matches(requestPathDTO.getPath())
				)
		){
			sendJsonError(response,HttpStatus.NOT_FOUND,404,"Not found", "path and method do not match");
			return;
		}

		// only /auth/** does not need token
		if(request.getRequestURI().startsWith("/auth")){
			filterChain.doFilter(request, response);
			return;
		}

		if(authHeader == null || !authHeader.startsWith("Bearer ")){
			sendJsonError(response, HttpStatus.UNAUTHORIZED, 401, "Authentication error", "Token null or not start with 'Bearer '");
			return;
		}

		jwt = authHeader.substring(7);

		try{
			// extract username (email) from token
			username = jwtService.extractUsername(jwt);

			// extract username's role from token
			final List<SimpleGrantedAuthority> roles = jwtService.extractAuthorities(jwt);

			// check if this token's username exists in the DB
			// catch UsernameNotFoundException
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			if(SecurityContextHolder.getContext().getAuthentication() == null){
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,
						roles
				);
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
				);
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

			filterChain.doFilter(request, response);

		}catch (SignatureException ex){
			sendJsonError(response, HttpStatus.UNAUTHORIZED, 401, "Authentication error", "token signature");
		}catch (ExpiredJwtException ex){
			sendJsonError(response, HttpStatus.UNAUTHORIZED, 401, "Authentication error", "token expired");
		}catch (UsernameNotFoundException ex){
			sendJsonError(response, HttpStatus.UNAUTHORIZED, 401, "User not found", "User from token not found in the database");
		} catch (Exception ex) {
			sendJsonError(response, HttpStatus.NOT_FOUND, 404, "Not Found", ex.getMessage());
		}
	}

	public static void sendJsonError(
			HttpServletResponse response, HttpStatus httpStatus, Integer code, String error, String message
	) throws IOException {
		ErrorDTO errorDTO = ErrorDTO.builder()
				.status(httpStatus)
				.code(code)
				.error(error)
				.message(message)
				.build();

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		response.setStatus(httpStatus.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Date date = Date.from(errorDTO.getTimestamp().atZone(ZoneId.systemDefault()).toInstant());
		response.setDateHeader("timestamp", date.getTime());
		response.getWriter().write(mapper.writeValueAsString(errorDTO));
	}

}