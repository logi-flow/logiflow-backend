package com.logi_flow.backend.common.constants;

public interface ResponseMessage {
    String SUCCESS = "성공";
    String FAILED = "실패";

    String INVALID_INPUT = "잘못된 입력값입니다.";
    String VALIDATION_FAIL = "입력값이 유효하지 않습니다.";
    String NO_PERMISSION = "접근 권한 없음";
    String DATA_INTEGRITY_VIOLATION = "데이터 무결성 오류가 발생했습니다.";

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
    String INVALID_REQUESTED_STATE = "요청 상태가 아닙니다.";


    String EXISTS_PAYROLL = "해당 기간의 급여대장이 이미 존재합니다.";
    String EXISTS_TYPE_CODE = "이미 존재하는 항목 코드입니다.";
    String NOT_USED_INACTIVE_TYPE = "비활성화된 타입은 사용할 수 없습니다.";
    String SYSTEM_ITEM_IMMUTABLE = "시스템에서 자동 생성한 항목은 수정 및 삭제할 수 없습니다.";
    String ALREADY_DELETED = "이미 삭제된 항목입니다.";
    String LOCK_PAYROLL_CONFIRMED = "확정된 급여대장은 수정 및 삭제를 할 수 없습니다.";
    String NOT_DELETE_USED_TYPE = "확정된 급여대장에 사용된 항목은 삭제할 수 없습니다.";
    String NOT_OWN_PAYROLL = "본인의 급여대장이 아닙니다.";
    String CONFIRMED_PAYROLL_ACCESSIBLE = "확정된 급여대장만 조회할 수 있습니다.";
    String INVALID_ALLOWANCE_ID = "잘못된 수당 ID가 요청되었습니다.";

    String CHECK_LICENSE = "라이센스는 배정 시 필수 입니다.";
    String INVALID_STATE = "잘못된 상태입니다.";
    String ACTION_TOO_EARLY = "7일이 경과하지 않아 요청을 처리할 수 없습니다.";

    String ACTION_TOO_LATE = "환불 신청 가능 기간이 지났습니다.";
    String DELIVERY_NOT_COMPLETED = "배송이 완료되지 않은 상태에서는 반품 신청을 할 수 없습니다.";
    String CONTRACT_EXPIRED = "계약이 종료되었습니다.";
    String INVALID_DATE_RANGE = "조회 시작 일자는 종료 일자보다 이전이어야 합니다.";

    String PRECONDITION_FAILED = "상태가 COMPLETED 이고 완료 후 7일 경과해야 숨김처리 가능합니다.";
    String INVALID_STATUS_FOR_DELIVERY_UPDATE = "배송 요청 상태에만 배송 수정 가능합니다.";
    String DELIVERY_UPDATE_ALLOWED_ONLY_IN_REQUESTED_STATUS = "REQUESTED 상태에서는 CANCELLED 또는 RECEIPTED 로만 변경 가능합니다.";
    String DELIVERY_UPDATE_ALLOWED_ONLY_IN_RECEIPTED_STATUS = "RECEIPTED 상태에서는 ASSIGNED 또는 REJECTED 로만 변경 가능합니다.";
    String DELIVERY_DELETE_ALLOWED_ONLY_IN_REQUESTED_STATUS = "상태가 요청 상태일 때만 취소 가능합니다.";
    String CUSTOMER_CANCEL_ONLY = "고객사는 취소만 가능합니다.";
    String INVALID_DELIVERY_OR_RETURN_DELIVERY_ID = "유효한 deliveryId 또는 returnDeliveryId가 필요합니다.";
    String ALLOWED_ONLY_IN_APPROVED_STATUS = "승인된 배송에서만 배차 등록이 가능합니다.";
    String DELIVERY_OR_RETURN_DELIVERY_ID_REQUESTED = "deliveryId 와 returnDeliveryId 둘 중 하나만 입력해야합니다.";
    String CONTRACT_STATUS_NOT_APPROVED = "계약 상태가 승인이 아닙니다.";
    String INVALID_DATE_FOR_CREATE_DELIVERY = "계약기간에 포함되지 않습니다.";

    String OUT_OF_TIME = "만료일 기간이 맞지 않습니다.";
}
