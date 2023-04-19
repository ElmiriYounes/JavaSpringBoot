package org.elmiriyounes.javabackend.controller;

import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.dto.*;
import org.elmiriyounes.javabackend.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

	private final CourseService courseService;

	@GetMapping("/allCourses")
	public ResponseEntity<List<CourseDTO>> getAllCourses(){
		return ResponseEntity.ok(courseService.getAllCourses());
	}

	@GetMapping("/course/{idCourse}")
	public ResponseEntity<CourseDTO> getCourseById(@PathVariable Integer idCourse){

		return ResponseEntity.ok(courseService.getCourseById(idCourse));
	}

	@PostMapping("/saveCourse")
	public ResponseEntity<String> saveCourse(@RequestBody @Valid CreateCourseDTO newCourse){

		return ResponseEntity.ok(courseService.addCourse(newCourse));
	}

	@PutMapping("/editCourse/{idCourse}")
	public ResponseEntity<String> editCourse(
			@PathVariable Integer idCourse, @RequestBody @Valid UpdateCourseDTO updateCourseDTO
	){

		return ResponseEntity.ok(courseService.updateCourse(idCourse, updateCourseDTO.getTitle()));
	}

	@PostMapping("/subscribeStudent/{idCourse}")
	public ResponseEntity<String> subscribeStudent(
			@PathVariable Integer idCourse, @RequestBody @Valid SubscribeCourseDTO subscribeCourseDTO
	){
		return ResponseEntity.ok(courseService.addStudentToCourse(idCourse, subscribeCourseDTO.getStudentEmail()));
	}

	@PostMapping("/unsubscribeStudent/{idCourse}")
	public ResponseEntity<String> unsubscribeStudent(
			@PathVariable Integer idCourse, @RequestBody @Valid SubscribeCourseDTO subscribeCourseDTO
	){
		return ResponseEntity.ok(courseService.removeStudentFromCourse(idCourse, subscribeCourseDTO.getStudentEmail()));
	}

	@DeleteMapping("/deleteCourse/{idCourse}")
	public ResponseEntity<String> deleteCourse(@PathVariable Integer idCourse) {

		return ResponseEntity.ok(courseService.deleteCourse(idCourse));
	}
}
