package org.elmiriyounes.javabackend.exception;

import org.elmiriyounes.javabackend.dto.ErrorDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {


	// for args sent with postman inside Body JSON
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorDTO handleArgumentException(MethodArgumentNotValidException ex){
//		ErrorDTO errors = new ErrorDTO(HttpStatus.BAD_REQUEST, 400, "args not valid", ex.getBindingResult().getFieldError().getDefaultMessage());
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.BAD_REQUEST)
				.code(400)
				.error("args not valid")
				.message(ex.getBindingResult().getFieldError().getDefaultMessage())
				.build();

		return errors;
	}

	// when Body JSON from postman hasn't args (without {} = empty) and should receive args
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		ErrorDTO errorDTO = ErrorDTO.builder()
				.status(HttpStatus.BAD_REQUEST)
				.code(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getMessage())
				.build();
		return ResponseEntity.badRequest().body(errorDTO);
	}


	// when user not found with find() hibernate
	@ExceptionHandler(value = NoSuchElementException.class)
	public ErrorDTO handleNoSuchElementException(NoSuchElementException ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.NOT_FOUND)
				.code(404)
				.error("User not found")
				.message("User not found from the database")
				.build();

		return errors;
	}

	// when UsernamePasswordAuthenticationToken from AuthenticationServImpl fails to authenticate
	@ExceptionHandler(AuthenticationException.class)
	public ErrorDTO handleAuthenticationException(AuthenticationException ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.code(401)
				.error("Authentication failed")
				.message("Email or password incorrect")
				.build();

		return errors;
	}

	// from UserDetailsService of ApplicationConfig Security
	@ExceptionHandler(UsernameNotFoundException.class)
	public ErrorDTO handleUsernameNotFoundException(UsernameNotFoundException ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.NOT_FOUND)
				.code(404)
				.error("Authentication failed")
				.message("User not found")
				.build();

		return errors;
	}

	// from controller getUserByEmail when email as path-variable is not valid format
	@ExceptionHandler(InvalidParameterException.class)
	public ErrorDTO handleInvalidParameterException(InvalidParameterException ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.BAD_REQUEST)
				.code(400)
				.error("Parameter invalid")
				.message(ex.getMessage())
				.build();

		return errors;
	}

	// when saving student and is already existing
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ErrorDTO handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.CONFLICT)
				.code(409)
				.error("Saving failed")
				.message(ex.getMessage())
				.build();

		return errors;
	}

	// when sending request with unknown attributes
	@ExceptionHandler(InvalidRequestBodyException.class)
	public ErrorDTO handleInvalidRequestBodyException(InvalidRequestBodyException ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.BAD_REQUEST)
				.code(400)
				.error("Bad attributes")
				.message(ex.getMessage())
				.build();

		return errors;
	}

	// when invalid role inside the filter
	@ExceptionHandler(InvalidRoleException.class)
	public ErrorDTO handleInvalidRoleException(InvalidRoleException ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.code(401)
				.error(ex.getMessage())
				.message(ex.getMessage())
				.build();

		return errors;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ErrorDTO handleAccessDeniedException(AccessDeniedException ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.code(401)
				.error("Access denied")
				.message(ex.getMessage())
				.build();

		return errors;
	}

	@ExceptionHandler(Exception.class)
	public ErrorDTO handleException(Exception ex) {
		ErrorDTO errors = ErrorDTO.builder()
				.status(HttpStatus.UNAUTHORIZED)
				.code(401)
				.error(ex.getMessage())
				.message(ex.getMessage())
				.build();

		return errors;
	}

}
