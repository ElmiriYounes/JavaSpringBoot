package org.elmiriyounes.javabackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {
	private HttpStatus status;
	private Integer code;
	private String error;
	private String message;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("Europe/Brussels"));

}
