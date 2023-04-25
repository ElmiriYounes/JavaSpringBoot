package org.elmiriyounes.javabackend.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.util.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentDTO {
	@Nullable
	@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email")
	private String email;
	@Nullable
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Firstname should contain only letters")
	private String firstname;
	@Nullable
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Lastname should contain only letters")
	private String lastname;
	@Nullable
	private String password;
	private final List<String> additionalPropsWarning = new ArrayList<>();

	// @JsonAnySetter = use this method when an attribute detected inside a JSON
	// request doesn't exist
	// between the attributes of the class
	@JsonAnySetter
	private void setAdditionalProp(String name, Object value) {
		this.additionalPropsWarning.add(name);
	}

	public boolean areAllFieldsNull() {
		return email == null && firstname == null && lastname == null && password == null;
	}

	public boolean areAnyFieldEmpty() {
		return (email != null && email.isEmpty())
				|| (firstname != null && firstname.isEmpty())
				|| (lastname != null && lastname.isEmpty())
				|| (password != null && password.isEmpty());
	}

}
