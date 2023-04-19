package org.elmiriyounes.javabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseDTO {
	@NotNull(message = "title should not be null")
	@NotBlank(message = "title is required")
	@NotEmpty(message = "title should not be empty")
	@Pattern(regexp = "^[a-zA-Z\\s]*$", message = "title should contain only letters and/or spaces")
	private String title;
}
