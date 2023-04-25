package org.elmiriyounes.javabackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.dto.CreateStudentDTO;
import org.elmiriyounes.javabackend.dto.UpdateStudentDTO;
import org.elmiriyounes.javabackend.dto.UserDTO;
import org.elmiriyounes.javabackend.entity.Course;
import org.elmiriyounes.javabackend.entity.Role;
import org.elmiriyounes.javabackend.entity.User;
import org.elmiriyounes.javabackend.repository.CourseRepository;
import org.elmiriyounes.javabackend.repository.UserRepository;
import org.elmiriyounes.javabackend.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CourseRepository courseRepository;
	private final PasswordEncoder passwordEncoder;
	@Override
	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();

		return createUsersDTO(users);
	}

	@Override
	public List<UserDTO> getAllTeachers() {
		var teachers = userRepository.findByRole(Role.TEACHER).orElseThrow();

		return createUsersDTO(teachers);
	}

	@Override
	public List<UserDTO> getAllStudents() {
		var students = userRepository.findByRole(Role.STUDENT).orElseThrow();

		return createUsersDTO(students);
	}

	private List<UserDTO> createUsersDTO(List<User> users){
		return users.stream()
				.map(user -> UserDTO.builder()
						.email(user.getEmail())
						.lastname(user.getLastname())
						.firstname(user.getFirstname())
						.role(user.getRole())
						.courses(user.getRole() == Role.STUDENT
								? user.getCoursesAsStudent()
								.stream()
								.map(course -> course.getTitle()).collect(Collectors.toList())
								:  user.getCoursesAsTeacher()
								.stream()
								.map(course -> course.getTitle()).collect(Collectors.toList()))
						.build())
				.collect(Collectors.toList());
	}

	@Override
	public UserDTO getUserByEmail(String email) {
		var user = userRepository.findByEmail(email).orElseThrow();

		return UserDTO.builder()
				.email(user.getEmail())
				.lastname(user.getLastname())
				.firstname(user.getFirstname())
				.role(user.getRole())
				.courses(user.getRole() == Role.STUDENT
						? user.getCoursesAsStudent()
						.stream()
						.map(course -> course.getTitle()).collect(Collectors.toList())
						:  user.getCoursesAsTeacher()
						.stream()
						.map(course -> course.getTitle()).collect(Collectors.toList()))
				.build();
	}

	@Override
	public String addStudent(CreateStudentDTO newStudent) {
		boolean existingUser = userRepository.existsByEmail(newStudent.getEmail());

		if(existingUser){
			throw new DataIntegrityViolationException("Student already exists");
		}

		if(userRepository.countByRole(Role.STUDENT) == 10){
			throw new DataIntegrityViolationException("the limit for adding students has been reached (max 10)");
		}

		User newUser = User.builder()
				.email(newStudent.getEmail())
				.lastname(newStudent.getLastname())
				.firstname(newStudent.getFirstname())
				.password(passwordEncoder.encode(newStudent.getPassword()))
				.role(Role.STUDENT)
				.build();

		try {
			userRepository.save(newUser);

			return "Student: " + newStudent.getLastname() + " " + newStudent.getFirstname() + " added successfully";
		}catch (DataIntegrityViolationException ex){
			throw new DataIntegrityViolationException(ex.getMessage());
		}
	}

	@Override
	public String updateUser(String studentEmail, UpdateStudentDTO updatedStudent) {
		Optional<User> existingUser = userRepository.findByEmail(studentEmail);
		// because Optional<T> is an object so existingUSer isn't the object User, we have to extract it
		// from Optional Object with get()
		// or using: User updatedUser = existingUser.orElseThrow(() -> new NoSuchElementException("Student not found"));
		// orElseThrow = get()
		User updatedUser = existingUser.get();


		updatedUser.setEmail(
				updatedStudent.getEmail() != null ? updatedStudent.getEmail() : updatedUser.getEmail()
		);
		updatedUser.setLastname(
				updatedStudent.getLastname() != null ? updatedStudent.getLastname() : updatedUser.getLastname()
		);
		updatedUser.setFirstname(
				updatedStudent.getFirstname() != null ? updatedStudent.getFirstname() : updatedUser.getFirstname()
		);
		updatedUser.setPassword(
				updatedStudent.getPassword() != null
						? passwordEncoder.encode(updatedStudent.getPassword())
						: updatedUser.getPassword()
		);


		try {
			userRepository.save(updatedUser);

			return "Student edited successfully";
		}catch (DataIntegrityViolationException ex){
			throw new DataIntegrityViolationException(ex.getMessage());
		}
	}

	@Override
	public String deleteStudent(String email) throws AccessDeniedException {
		Optional<User> existingUser = userRepository.findByEmail(email);

		User deletedUser = existingUser.get();

		if(deletedUser.getRole() == Role.TEACHER){
			throw new AccessDeniedException("Deleting teacher is not allowed");
		}

		// remove relations
		List<Course> courses = courseRepository.findAll();

		for (Course course : courses) {
			course.getStudents().remove(deletedUser);
		}

		try {
			userRepository.delete(deletedUser);

			return "Student deleted successfully";
		}catch (DataIntegrityViolationException ex){
			throw new DataIntegrityViolationException(ex.getMessage());
		}
	}

}
