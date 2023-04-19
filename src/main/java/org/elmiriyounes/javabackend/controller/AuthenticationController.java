package org.elmiriyounes.javabackend.controller;

import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.dto.LoginRequestDTO;
import org.elmiriyounes.javabackend.dto.LoginResponseDTO;
import org.elmiriyounes.javabackend.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@GetMapping("/login")
	public ResponseEntity<LoginResponseDTO>authenticate(@RequestBody @Valid LoginRequestDTO loginRequest){
		return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
	}
}
