package org.elmiriyounes.javabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
	private Integer idCourse;
	private String title;
	private String teacher;
	private List<String> students;
}
