package org.elmiriyounes.javabackend.service;

import org.elmiriyounes.javabackend.dto.CreateStudentDTO;
import org.elmiriyounes.javabackend.dto.UpdateStudentDTO;
import org.elmiriyounes.javabackend.dto.UserDTO;
import org.elmiriyounes.javabackend.entity.User;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface UserService {
	List<UserDTO> getAllUsers();
	List<UserDTO> getAllTeachers();
	List<UserDTO> getAllStudents();
	UserDTO getUserByEmail(String email);
	String addStudent(CreateStudentDTO newStudent);
	String updateUser(String studentEmail, UpdateStudentDTO updatedStudent);
	String deleteStudent(String email) throws AccessDeniedException;
}
