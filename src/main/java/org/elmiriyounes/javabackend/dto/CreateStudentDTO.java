package org.elmiriyounes.javabackend.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentDTO {
	@NotNull(message = "email should not be null")
	@NotBlank(message = "email is required")
	@NotEmpty(message = "email should not be empty")
	@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email")
	private String email;
	@NotNull(message = "firstname should not be null")
	@NotBlank(message = "firstname is required")
	@NotEmpty(message = "firstname should not be empty")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "firstname should contain only letters")
	private String firstname;
	@NotNull(message = "lastname should not be null")
	@NotBlank(message = "lastname is required")
	@NotEmpty(message = "lastname should not be empty")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "lastname should contain only letters")
	private String lastname;
	@NotNull(message = "password should not be null")
	@NotBlank(message = "password is required")
	@NotEmpty(message = "password should not be empty")
	private String password;

	private final List<String> additionalPropsWarning = new ArrayList<>();

	// @JsonAnySetter = use this method when an attribute detected inside a JSON
	// request doesn't exist
	// between the attributes of the class
	@JsonAnySetter
	private void setAdditionalProp(String name, Object value) {
		this.additionalPropsWarning.add(name);
	}

}
