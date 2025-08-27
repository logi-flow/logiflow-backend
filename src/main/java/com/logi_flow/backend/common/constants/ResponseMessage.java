package com.logi_flow.backend.common.constants;

public interface ResponseMessage {
    String SUCCESS = "성공";
    String FAILED = "실패";

    String NO_PERMISSION = "접근 권한 없음";

    String USER_ALREADY_EXISTS = "이미 존재하는 유저";
    String USER_NOT_FOUND = "유저를 찾을 수 없음";
    String USER_CONFLICT = "동일 정보로 여러 계정이 확인 되었습니다.";
    String ALREADY_EXISTS = "이미 존재합니다. 중복 불가";
    String FILE_NOT_FOUND = "파일을 찾을 수 없음";
    String RESOURCE_NOT_FOUND = "리소스를 찾을 수 없음";

    String TOKEN_EXPIRED = "토큰이 만료되었습니다.";
    String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
    String MISSING_TOKEN = "토큰이 누락되었습니다.";

    String NOT_CORRECT_PASSWORD = "비밀번호가 일치하지 않습니다.";
    String NOT_MATCH_PASSWORD = "비밀번호 확인이 일치하지 않습니다.";
    String PASSWORD_CHANGE_REQUIRED = "비밀번호 변경이 필요합니다.";
    String NOT_MATCH = "아이디 또는 비밀번호가 일치하지 않습니다.";
    String NOT_MATCH_INFORMATION = "찾을려는 아이디의 정보가 일치하지 않습니다.";
    String INTERNAL_SERVER_ERROR = "서버 오류가 발생했습니다.";
    String MAIL_SEND_FAIL = "메일 발송에 실패하였습니다.";
    String MAIL_NOT_FOUND = "이메일을 찾을 수 없습니다.";

    String NO_OPEN_ATTENDANCE = "미퇴근 기록이 존재하지 않습니다.";

    String EXISTS_PAYROLL = "해당 기간의 급여대장이 이미 존재합니다.";
    String EXISTS_TYPE_CODE = "이미 존재하는 항목 코드입니다.";
    String NOT_USED_INACTIVE_TYPE = "비활성화된 타입은 사용할 수 없습니다.";
    String SYSTEM_ITEM_IMMUTABLE = "시스템에서 자동 생성한 항목은 수정 및 삭제할 수 없습니다.";
    String ALREADY_DELETED = "이미 삭제된 항목입니다.";
    String LOCK_PAYROLL_CONFIRMED = "확정된 급여대장은 수정 및 삭제를 할 수 없습니다.";
    String NOT_DELETE_USED_TYPE = "확정된 급여대장에 사용된 항목은 삭제할 수 없습니다.";
    String NOT_OWN_PAYROLL = "본인의 급여대장이 아닙니다.";

    String INVALID_STATE = "잘못된 상태입니다.";
    String CHECK_LICENSE = "라이센스는 배정 시 필수 입니다.";
}
