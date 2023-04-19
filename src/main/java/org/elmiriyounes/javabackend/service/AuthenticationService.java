package org.elmiriyounes.javabackend.service;

import org.elmiriyounes.javabackend.dto.LoginRequestDTO;
import org.elmiriyounes.javabackend.dto.LoginResponseDTO;

import javax.validation.Valid;

public interface AuthenticationService {
	LoginResponseDTO authenticate(@Valid LoginRequestDTO login);
}
