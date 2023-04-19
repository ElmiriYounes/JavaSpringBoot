package org.elmiriyounes.javabackend.repository;

import org.elmiriyounes.javabackend.entity.Course;
import org.elmiriyounes.javabackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
	boolean existsByTitle(String title);
	Long countByTeacher(User teacher);
}
