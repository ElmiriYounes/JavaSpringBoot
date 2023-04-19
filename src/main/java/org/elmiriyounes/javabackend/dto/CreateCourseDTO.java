package org.elmiriyounes.javabackend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCourseDTO {

	@NotNull(message = "title should not be null")
	@NotBlank(message = "title is required")
	@NotEmpty(message = "title should not be empty")
	@Pattern(regexp = "^[a-zA-Z\\s]*$", message = "title should contain only letters and/or spaces")
	private String title;


	// Optional variable
	@Valid
	@Size(min = 0, message = "studentEmails should be empty if you do not want to add any students")
	private List<@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email") String> studentEmails;


}

