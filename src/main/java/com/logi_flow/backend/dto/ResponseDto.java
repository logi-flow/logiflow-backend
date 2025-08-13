package com.logi_flow.backend.dto;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
public class ResponseDto<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ResponseDto<T> success(String code, String message, T data) {
        return new ResponseDto<>(code, message, data);
    }

    public static <T> ResponseDto<T> success(String code, String message) {
        return new ResponseDto<>(code, message, null);
    }

    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(code, message, null);
    }

    public static ResponseEntity<ResponseDto<?>> failWithStatus(String code, String message, HttpStatus status) {
        ResponseDto<?> response = new ResponseDto<>(code, message, null);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ResponseDto<T>> toResponseEntity(HttpStatus status, ResponseDto<T> body) {
        return ResponseEntity.status(status).body(body);
    }

    public static <T> ResponseEntity<ResponseDto<T>> toResponseEntity(HttpStatus status, T data) {
        return ResponseEntity.status(status).body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data));
    }
}
