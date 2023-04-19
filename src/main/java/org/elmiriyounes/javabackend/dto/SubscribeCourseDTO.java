package org.elmiriyounes.javabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeCourseDTO {
	@NotNull(message = "studentEmail should not be null")
	@NotBlank(message = "studentEmail is required")
	@NotEmpty(message = "studentEmail should not be empty")
	@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email")
	private String studentEmail;
}
