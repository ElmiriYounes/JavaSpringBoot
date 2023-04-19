package org.elmiriyounes.javabackend.filter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.exception.InvalidRoleException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.elmiriyounes.javabackend.filter.JwtAuthenticationFilter.sendJsonError;

@Component
@RequiredArgsConstructor
public class AuthorizeRoleFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		// these paths do not need role permissions
		if(
				request.getRequestURI().startsWith("/auth")
						|| request.getRequestURI().contains("/allUsers")
						|| request.getRequestURI().contains("/allTeachers")
						|| request.getRequestURI().contains("/allStudents")
						|| request.getRequestURI().contains("/users/user/")
						|| request.getRequestURI().contains("/allCourses")
						|| request.getRequestURI().contains("/courses/course/")
						|| request.getRequestURI().contains("/courses/subscribeStudent/")
						|| request.getRequestURI().contains("/courses/unsubscribeStudent/")
		){
			filterChain.doFilter(request, response);
			return;
		}

		// get context
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		final List<SimpleGrantedAuthority> roles = auth.getAuthorities()
				.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
				.collect(Collectors.toList());

		final String username = auth.getName();

		if(
				request.getRequestURI().contains("/saveStudent")
						&& !roles.contains(new SimpleGrantedAuthority("TEACHER"))
		){
			throw new InvalidRoleException("Only teachers can add students");
		}

		if(request.getRequestURI().contains("/deleteStudent")){
			// getRequestURI = /users/editStudent/{email}
			// split("/") = String[] = [, users, deleteStudent, {email}]
			// [, users, deleteStudent, {email}], index 0 is empty because when we use split, if the delimiter of split is
			// at the beginning, it'll generate an empty string
			String emailPathVariable = request.getRequestURI().split("/")[3];

			if (!isValidEmail(emailPathVariable)) {
				throw new InvalidParameterException("Invalid email format");
			}

			// check if user exists before deleting
			try{
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(emailPathVariable);
			}catch (UsernameNotFoundException ex){
				sendJsonError(
						response,
						HttpStatus.UNAUTHORIZED,
						404,
						"User not found",
						emailPathVariable + ": user not found from the database"
				);
				return;
			}

			if(!roles.contains(new SimpleGrantedAuthority("TEACHER"))){
				throw new InvalidRoleException("Only teachers can delete students");
			}
		}

		if(
				request.getRequestURI().contains("/editStudent")
		){
			String emailPathVariable = request.getRequestURI().split("/")[3];

			if (!isValidEmail(emailPathVariable)) {
				throw new InvalidParameterException("Invalid email format");
			}

			try{
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(emailPathVariable);

				if(
						roles.contains(new SimpleGrantedAuthority("STUDENT"))
								&& !username.equals(emailPathVariable)
				){
						throw new InvalidRoleException("As student, you can't modify the account of another user");
				}

				if(
						roles.contains(new SimpleGrantedAuthority("TEACHER"))
								&& !username.equals(emailPathVariable)
								&& userDetails.getAuthorities().contains(new SimpleGrantedAuthority("TEACHER"))
				){
					throw new InvalidRoleException("As teacher, you can't modify the account of another teacher");
				}
			}catch (UsernameNotFoundException ex){
				sendJsonError(
						response,
						HttpStatus.UNAUTHORIZED,
						404,
						"User not found",
						emailPathVariable + ": user not found from the database"
				);
				return;
			}
		}

		if(
				(request.getRequestURI().contains("/saveCourse")
						|| request.getRequestURI().contains("/editCourse"))
						&& !roles.contains(new SimpleGrantedAuthority("TEACHER"))
		){
			throw new InvalidRoleException("As student, you can't add or edit courses");
		}

		if(
				request.getRequestURI().contains("/courses/deleteCourse/")
						&& !roles.contains(new SimpleGrantedAuthority("TEACHER"))
		){
			throw new InvalidRoleException("As student, you can't delete courses");
		}

		filterChain.doFilter(request, response);

	}
	public static boolean isValidEmail(String email) {
		String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

}
