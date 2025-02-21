package com.fourseason.delivery.global.exception;

import com.fourseason.delivery.domain.payment.exception.CustomRestClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle CustomException
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ErrorResponseEntity.toResponseEntity(errorCode);
    }

    /**
     * Handle MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.ok(ErrorResponseEntity.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build());
    }

    /**
     * Handle NoHandlerFoundException
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ResponseEntity.ok(ErrorResponseEntity.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("요청하신 페이지가 없습니다.")
                .build());
    }

    /**
     * Handle AccessDeniedHandler
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseEntity> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.ok(ErrorResponseEntity.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("권한이 없습니다.")
                .build());
    }

    /**
     * Handel CustomRestClientException
     */
    @ExceptionHandler(CustomRestClientException.class)
    public ResponseEntity<ErrorResponseEntity> handleCustomRestClientException(CustomRestClientException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ErrorResponseEntity.builder()
                .status(e.getStatusCode().value())
                .message(e.getMessage())
                .build());
    }
}
