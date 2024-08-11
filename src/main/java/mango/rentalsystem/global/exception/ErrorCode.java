package mango.rentalsystem.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// 400 BAD_REQUEST
	INVALID_DEPARTMENT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 학과 id입니다."),

	// 401 UNAUTHORIZED

	// 403 FORBIDDEN

	// 404 NOT_FOUND

	// 409 CONFLICT
	DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "이미 존재하는 카테고리는 추가할 수 없습니다."),

	// 500 INTERNAL_SERVER_ERROR
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");

	private final HttpStatus status;
	private final String message;
}
