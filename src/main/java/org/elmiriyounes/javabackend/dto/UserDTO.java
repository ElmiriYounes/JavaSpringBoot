package org.elmiriyounes.javabackend.dto;

import lombok.*;
import org.elmiriyounes.javabackend.entity.Course;
import org.elmiriyounes.javabackend.entity.Role;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private String email;
	private String firstname;
	private String lastname;
	private Role role;
	private List<String> courses;

}
