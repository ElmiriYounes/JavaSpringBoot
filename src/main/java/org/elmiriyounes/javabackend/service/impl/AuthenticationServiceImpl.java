package org.elmiriyounes.javabackend.service.impl;

import lombok.AllArgsConstructor;
import org.elmiriyounes.javabackend.dto.LoginRequestDTO;
import org.elmiriyounes.javabackend.dto.LoginResponseDTO;
import org.elmiriyounes.javabackend.repository.UserRepository;
import org.elmiriyounes.javabackend.service.AuthenticationService;
import org.elmiriyounes.javabackend.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final JwtService jwtService;

	@Override
	public LoginResponseDTO authenticate(LoginRequestDTO login) {
		// authenticateManager will check if username and password match with database
		// catch AuthenticationException
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

		// login successfully, get the user for the token
		var user = userRepository.findByEmail(login.getEmail())
				.orElseThrow();

		return LoginResponseDTO.builder()
				.token(jwtService.generateToken(user))
				.build();
	}
}
