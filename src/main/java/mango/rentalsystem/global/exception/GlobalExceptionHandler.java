package mango.rentalsystem.global.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		log.error("CustomException : {}", e.getMessage(), e);
		return ResponseEntity.status(e.getErrorCode().getStatus()).body(ErrorResponse.of(e.getErrorCode()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("INTERNAL_SERVER_ERROR : {}", e.getMessage(), e);
		return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
			.body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	protected ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException e,
		WebRequest request) {
		log.error("UNAUTHORIZED_DOMAIN : {}", e.getMessage(), e);
		return ResponseEntity.status(ErrorCode.UNAUTHORIZED_DOMAIN.getStatus())
			.body(ErrorResponse.of(ErrorCode.UNAUTHORIZED_DOMAIN));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error("METHOD_ARGUMENT_NOT_VALID : {}", e.getMessage(), e);
		String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return ResponseEntity.status(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())
			.body(ErrorResponse.of(ErrorCode.METHOD_ARGUMENT_NOT_VALID, errorMessage));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error("HTTP_MESSAGE_NOT_READABLE : {}", e.getMessage(), e);
		return ResponseEntity.status(ErrorCode.HTTP_MESSAGE_NOT_READABLE.getStatus())
			.body(ErrorResponse.of(ErrorCode.HTTP_MESSAGE_NOT_READABLE));
	}

}
