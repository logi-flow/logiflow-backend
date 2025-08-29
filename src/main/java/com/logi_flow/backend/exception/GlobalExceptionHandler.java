package com.logi_flow.backend.exception;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<ResponseDto<?>> handleBadRequest(RuntimeException e) {
        return logAndRespond(ResponseCode.INVALID_INPUT, ResponseMessage.INVALID_INPUT, HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<?>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : ResponseMessage.VALIDATION_FAIL;
        return logAndRespond(ResponseCode.VALIDATION_FAIL, errorMessage, HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseDto<?>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return logAndRespond(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDto<?>> handleAccessDeniedException(AccessDeniedException e) {
        return logAndRespond(ResponseCode.NO_PERMISSION, ResponseMessage.NO_PERMISSION, HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return logAndRespond(ResponseCode.DATA_INTEGRITY_VIOLATION, ResponseMessage.DATA_INTEGRITY_VIOLATION, HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> handleGeneral(Exception e) {
        return logAndRespond(ResponseCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<ResponseDto<?>> logAndRespond(String code, String message, HttpStatus status, Exception e) {
        if (status.is5xxServerError()) {
            log.error("{}: {}", status, e.getMessage(), e);
        } else {
            log.warn("{}: {}", status, e.getMessage());
        }

        return ResponseDto.failWithStatus(code, message, status);
    }
}
