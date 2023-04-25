package org.elmiriyounes.javabackend.controller;

import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.dto.LoginRequestDTO;
import org.elmiriyounes.javabackend.dto.LoginResponseDTO;
import org.elmiriyounes.javabackend.dto.UserDTO;
import org.elmiriyounes.javabackend.entity.User;
import org.elmiriyounes.javabackend.repository.UserRepository;
import org.elmiriyounes.javabackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	@Autowired
	private UserRepository userRepository;
	private final AuthenticationService authenticationService;

	@GetMapping("/login")
	public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody @Valid LoginRequestDTO loginRequest) {

		return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
	}
}
