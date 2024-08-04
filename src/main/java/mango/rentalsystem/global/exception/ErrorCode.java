package mango.rentalsystem.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// 400 BAD_REQUEST

	// 401 UNAUTHORIZED

	// 403 FORBIDDEN

	// 404 NOT_FOUND

	// 409 CONFLICT

	// 500 INTERNAL_SERVER_ERROR
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");

	private final HttpStatus status;
	private final String message;
}
