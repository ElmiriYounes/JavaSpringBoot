package org.elmiriyounes.javabackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
	@NotNull(message = "email should not be null")
	@NotBlank(message = "email is required")
	@NotEmpty(message = "email should not be empty")
	@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email")
	String email;

	@NotNull(message = "password should not be null")
	@NotBlank(message = "password is required")
	@NotEmpty(message = "password should not be empty")
	String password;
}
