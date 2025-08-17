package com.logi_flow.backend.common.constants;

public interface ResponseMessage {
    String SUCCESS = "성공";
    String FAILED = "실패";

    String NO_PERMISSION = "접근 권한 없음";

    String USER_ALREADY_EXISTS = "이미 존재하는 유저";
    String USER_NOT_FOUND = "유저를 찾을 수 없음";
    String ALREADY_EXISTS = "이미 존재합니다. 중복 불가";
    String FILE_NOT_FOUND = "파일을 찾을 수 없음";
    String RESOURCE_NOT_FOUND = "리소스를 찾을 수 없음";

    String TOKEN_EXPIRED = "토큰이 만료되었습니다.";
    String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
    String MISSING_TOKEN = "토큰이 누락되었습니다.";

    String ALREADY_OPEN_ATTENDANCE = "미퇴근 기록 또는 중복된 출근 기록이 존재합니다.";
    String NO_OPEN_ATTENDANCE = "미퇴근 기록이 존재하지 않습니다.";
}
