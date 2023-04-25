package org.elmiriyounes.javabackend;


import lombok.RequiredArgsConstructor;
import org.elmiriyounes.javabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@RequiredArgsConstructor
public class JavabackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavabackendApplication.class, args);
	}

}
