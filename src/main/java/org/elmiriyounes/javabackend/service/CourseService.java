package org.elmiriyounes.javabackend.service;

import org.elmiriyounes.javabackend.dto.CourseDTO;
import org.elmiriyounes.javabackend.dto.CreateCourseDTO;
import org.elmiriyounes.javabackend.entity.Course;

import java.util.List;

public interface CourseService {
	List<CourseDTO> getAllCourses();
	CourseDTO getCourseById(Integer id);
	String addCourse(CreateCourseDTO newCourse);
	String updateCourse(Integer idCourse, String courseTitle);
	String addStudentToCourse(Integer idCourse, String studentEmail);
	String removeStudentFromCourse(Integer idCourse, String studentEmail);
	String deleteCourse(Integer idCourse);
}
