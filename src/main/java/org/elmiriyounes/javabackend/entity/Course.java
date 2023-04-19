package org.elmiriyounes.javabackend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

	@Id
	@GeneratedValue
	private Integer id;
	private String title;
	@ManyToOne
	private User teacher;

	@ManyToMany
	@Where(clause = "role='STUDENT'")
	private List<User> students;

}
