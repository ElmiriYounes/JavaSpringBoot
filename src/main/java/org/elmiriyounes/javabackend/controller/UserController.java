package org.elmiriyounes.javabackend.controller;


import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.dto.CreateStudentDTO;
import org.elmiriyounes.javabackend.dto.UpdateStudentDTO;
import org.elmiriyounes.javabackend.dto.UserDTO;
import org.elmiriyounes.javabackend.exception.InvalidRequestBodyException;
import org.elmiriyounes.javabackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.security.InvalidParameterException;
import java.util.List;

import static org.elmiriyounes.javabackend.filter.AuthorizeRoleFilter.isValidEmail;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/allUsers")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@GetMapping("/allTeachers")
	public ResponseEntity<List<UserDTO>> getAllTeachers(){
		return ResponseEntity.ok(userService.getAllTeachers());
	}

	@GetMapping("/allStudents")
	public ResponseEntity<List<UserDTO>> getAllStudents(){
		return ResponseEntity.ok(userService.getAllStudents());
	}

	@GetMapping("/user/{email}")
	public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email){
		if (!isValidEmail(email)) {
			throw new InvalidParameterException("Invalid email format");
		}

		return ResponseEntity.ok(userService.getUserByEmail(email));
	}

	@PostMapping("/saveStudent")
	public ResponseEntity<String> saveStudent(@RequestBody @Valid CreateStudentDTO newStudent){
		if(!newStudent.getAdditionalPropsWarning().isEmpty()){
			throw new InvalidRequestBodyException("Please retry to send your request without: [" +
					String.join(", ", newStudent.getAdditionalPropsWarning())+
					"], "+
					(newStudent.getAdditionalPropsWarning().size() > 1
							? "these attributes do not exist"
							: "this attribute does not exist")
					+ ". The attributes allowed are: ['email', 'lastname', 'firstname' or 'password'].");
		}

		return ResponseEntity.ok(userService.addStudent(newStudent));
	}

	@PutMapping("/editStudent/{email}")
	public ResponseEntity<String> editStudent(
			@PathVariable String email,
			@RequestBody @Valid UpdateStudentDTO editedStudent
	){

		if(editedStudent.areAllFieldsNull()){
			throw new InvalidParameterException("All fields are null: at least one field should not be null (email, lastname, firstname, password)");
		}

		if(!editedStudent.getAdditionalPropsWarning().isEmpty()){
			throw new InvalidRequestBodyException("Please retry to send your request without: [" +
					String.join(", ", editedStudent.getAdditionalPropsWarning())+
					"], "+
					(editedStudent.getAdditionalPropsWarning().size() > 1
							? "these attributes do not exist"
							: "this attribute does not exist")
			+ ". The attributes allowed are: ['email', 'lastname', 'firstname' or 'password'].");
		}

		return ResponseEntity.ok(userService.updateUser(email, editedStudent));
	}

	@DeleteMapping("/deleteStudent/{email}")
	public ResponseEntity<String> deleteUser(@PathVariable String email) throws AccessDeniedException {

		return ResponseEntity.ok(userService.deleteStudent(email));
	}
}
