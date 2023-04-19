package org.elmiriyounes.javabackend.config;

import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.filter.AuthorizeRoleFilter;
import org.elmiriyounes.javabackend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthorizeRoleFilter authorizeRoleFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
				.csrf()
				.disable()
				.authorizeRequests()
				.antMatchers("/auth/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(authorizeRoleFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
