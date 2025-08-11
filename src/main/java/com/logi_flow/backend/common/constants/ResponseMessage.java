package com.logi_flow.backend.common.constants;

public interface ResponseMessage {
    String SUCCESS = "성공";
    String FAILED = "실패";

    String NO_PERMISSION = "접근 권한 없음";

    String USER_ALREADY_EXISTS = "이미 존재하는 유저";
    String USER_NOT_FOUND = "유저를 찾을 수 없음";
    String FILE_NOT_FOUND = "파일을 찾을 수 없음";
    String RESOURCE_NOT_FOUND = "리소스를 찾을 수 없음";
}
