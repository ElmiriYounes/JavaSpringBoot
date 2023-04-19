package org.elmiriyounes.javabackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.dto.CourseDTO;
import org.elmiriyounes.javabackend.dto.CreateCourseDTO;
import org.elmiriyounes.javabackend.entity.Course;
import org.elmiriyounes.javabackend.entity.User;
import org.elmiriyounes.javabackend.repository.CourseRepository;
import org.elmiriyounes.javabackend.repository.UserRepository;
import org.elmiriyounes.javabackend.service.CourseService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

	private final CourseRepository courseRepository;
	private final UserRepository userRepository;
	@Override
	public List<CourseDTO> getAllCourses() {
		List<Course> courses = courseRepository.findAll();

		return courses.stream()
				.map(course -> CourseDTO.builder()
						.idCourse(course.getId())
						.title(course.getTitle())
						.teacher(course.getTeacher().getLastname() + " " + course.getTeacher().getFirstname())
						.students(course.getStudents().stream()
								.map(student -> student.getLastname() + " " + student.getFirstname())
								.collect(Collectors.toList()))
						.build()
				)
				.collect(Collectors.toList());
	}

	@Override
	public CourseDTO getCourseById(Integer id) {
		boolean existingCourse = courseRepository.existsById(id);

		if(!existingCourse){
			throw new InvalidParameterException("Course does not exists");
		}

		Course course = courseRepository.findById(id).get();

		return CourseDTO.builder()
				.idCourse(course.getId())
				.title(course.getTitle())
				.teacher(course.getTeacher().getLastname() + " " + course.getTeacher().getFirstname())
				.students(course.getStudents().stream()
						.map(student -> student.getLastname() + " " + student.getFirstname())
						.collect(Collectors.toList()))
				.build();
	}

	@Override
	public String addCourse(CreateCourseDTO newCourse) {
		boolean existingCourse = courseRepository.existsByTitle(newCourse.getTitle());

		if (existingCourse){
			throw new DataIntegrityViolationException("Course already exists");
		}

		/* get the teacher from the authentication generated in the filter from the token because each course as
		only one teacher, and he is the teacher who adding it */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailTeacher = authentication.getName();

		User teacher = userRepository.findByEmail(emailTeacher).get();
		var countByTeacher = courseRepository.countByTeacher(teacher);

		if(countByTeacher == 2){
			throw new DataIntegrityViolationException("the limit for adding course has been reached (max 2 per teacher)");
		}

		Course savedCourse = Course.builder()
				.title(newCourse.getTitle())
				.teacher(userRepository.findByEmail(emailTeacher).get())
				.students(
						newCourse.getStudentEmails() != null ? newCourse.getStudentEmails().stream()
								.map(email -> userRepository.findByEmail(email).orElse(null))
								// Filter Objects nonNull = to ignore if a student does not exist
								.filter(Objects::nonNull)
								.collect(Collectors.toList()) : new ArrayList<>()
				)
				.build();
		try{
			courseRepository.save(savedCourse);

			return "Course added successfully";
		}catch (DataIntegrityViolationException ex){
			throw new DataIntegrityViolationException(ex.getMessage());
		}
	}

	@Override
	public String updateCourse(Integer idCourse, String courseTitle) {
		boolean existingCourse = courseRepository.existsById(idCourse);

		if (!existingCourse){
			throw new DataIntegrityViolationException("Course does not exist");
		}

		try{
			Course updatedCourse = courseRepository.findById(idCourse).get();

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String emailTeacher = authentication.getName();

			if(!emailTeacher.equals(updatedCourse.getTeacher().getEmail())){
				throw new DataIntegrityViolationException("You are not allowed to edit a course of another teacher");
			}

			updatedCourse.setTitle(courseTitle);

			courseRepository.save(updatedCourse);

			return "course edited successfully";
		}catch (DataIntegrityViolationException ex){
			throw new DataIntegrityViolationException(ex.getMessage());
		}
	}

	@Override
	public String addStudentToCourse(Integer idCourse, String studentEmail) {
		boolean existingCourse = courseRepository.existsById(idCourse);
		boolean existingStudent = userRepository.existsByEmail(studentEmail);

		if (!existingCourse){
			throw new DataIntegrityViolationException("Course does not exist");
		}

		if (!existingStudent){
			throw new DataIntegrityViolationException("Student does not exist");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailUser = authentication.getName();

		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("STUDENT"))
				&& !studentEmail.equals(emailUser)){
			throw new DataIntegrityViolationException("As student, you are not allowed to subscribe another student");
		}

		Course course = courseRepository.findById(idCourse).get();
		User student = userRepository.findByEmail(studentEmail).get();

		if(
				course.getStudents().stream().anyMatch(studentSubscribed -> studentSubscribed.getEmail().equals(studentEmail))
		){
			throw new DataIntegrityViolationException("Student already subscribed");
		}

		course.getStudents().add(student);

		try{
			courseRepository.save(course);

			return "Student subscribed successfully";
		}catch (DataIntegrityViolationException ex){
			throw new DataIntegrityViolationException(ex.getMessage());
		}
	}

	@Override
	public String removeStudentFromCourse(Integer idCourse, String studentEmail) {
		boolean existingCourse = courseRepository.existsById(idCourse);
		boolean existingStudent = userRepository.existsByEmail(studentEmail);

		if (!existingCourse){
			throw new DataIntegrityViolationException("Course does not exist");
		}

		if (!existingStudent){
			throw new DataIntegrityViolationException("Student does not exist");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailUser = authentication.getName();

		if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("STUDENT"))
				&& !studentEmail.equals(emailUser)){
			throw new DataIntegrityViolationException("As student, you are not allowed to unsubscribe another student");
		}

		Course course = courseRepository.findById(idCourse).get();
		User student = userRepository.findByEmail(studentEmail).get();

		if(
				!course.getStudents().stream().anyMatch(studentSubscribed -> studentSubscribed.getEmail().equals(studentEmail))
		){
			throw new DataIntegrityViolationException("Student already unsubscribed");
		}

		course.getStudents().remove(student);

		try{
			courseRepository.save(course);

			return "Student unsubscribed successfully";
		}catch (DataIntegrityViolationException ex){
			throw new DataIntegrityViolationException(ex.getMessage());
		}
	}

	@Override
	public String deleteCourse(Integer idCourse) {
		boolean existingCourse = courseRepository.existsById(idCourse);

		if(!existingCourse){
			throw new DataIntegrityViolationException("Course does not exist");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String emailTeacher = authentication.getName();

		Course course = courseRepository.findById(idCourse).get();

		if(!course.getTeacher().getEmail().equals(emailTeacher)){
			throw new DataIntegrityViolationException("you can not delete the course of another teacher");
		}

		try{
			courseRepository.delete(course);

			return "course deleted successfully";
		}catch (DataIntegrityViolationException ex){
			throw new DataIntegrityViolationException(ex.getMessage());
		}
	}
}
