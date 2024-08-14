package mango.rentalsystem.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	// 400 BAD_REQUEST
	INVALID_DEPARTMENT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 학과 id입니다."),
	INVALID_RENTAL_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 상태 변경입니다."),
	INVALID_RENTAL_REVIEW(HttpStatus.BAD_REQUEST, "유효하지 않은 리뷰 작성입니다."),
	METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "유효하지 않은 인자입니다."),
	HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "필드가 잘못된 타입이거나 혹은 누락되었습니다."),

	// 401 UNAUTHORIZED
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
	UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 형식의 토큰입니다."),
	WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다."),
	WRONG_SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 서명이 유효하지 않습니다."),
	UNKNOWN_TOKEN(HttpStatus.UNAUTHORIZED, "인증 토큰이 존재하지 않습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	UNAUTHORIZED_ITEM(HttpStatus.UNAUTHORIZED, "이 물품에 접근할 권한이 없습니다."),
	UNAUTHORIZED_RENTAL(HttpStatus.UNAUTHORIZED, "이 대여에 접근할 권한이 없습니다."),

	// 403 FORBIDDEN
	NOT_OPERATING_HOURS(HttpStatus.FORBIDDEN, "대여 서비스 운영 시간이 아닙니다."),

	// 404 NOT_FOUND
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
	ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 물품입니다."),
	RENTAL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 대여입니다."),

	// 409 CONFLICT
	DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "이미 존재하는 카테고리는 추가할 수 없습니다."),

	// 500 INTERNAL_SERVER_ERROR
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");

	private final HttpStatus status;
	private final String message;
}
