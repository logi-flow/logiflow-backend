package com.logi_flow.backend.common.constants;

public interface ResponseCode {
    String SUCCESS = "SU";
    String FAILED = "FA";

    String TOKEN_EXPIRED = "TE";
    String INVALID_TOKEN = "IT";
    String MISSING_TOKEN = "MT";

    String USER_ALREADY_EXISTS = "UAX";
    String USER_NOT_FOUND = "UNF";
    String USER_CONFLICT = "UC";
    String ALREADY_EXISTS = "AX";
    String NOT_CORRECT_PASSWORD = "NMP";
    String NOT_MATCH_PASSWORD = "NMP";
    String PASSWORD_CHANGE_REQUIRED = "PCR";
    String NOT_MATCH = "NM";
    String NOT_MATCH_INFORMATION = "NMI";
    String INTERNAL_SERVER_ERROR = "ISE";
    String MAIL_SEND_FAIL = "MSF";
    String MAIL_NOT_FOUND = "MNF";

    String EXISTS_TYPE_CODE = "ETC";
    String NOT_USED_INACTIVE_TYPE = "NUT";
    String SYSTEM_ITEM_IMMUTABLE = "SI";
    String ALREADY_DELETED = "AD";
    String LOCK_PAYROLL_CONFIRMED = "LPC";
    String NOT_DELETE_USED_TYPE = "NDT";

    String FORBIDDEN = "FB";
    String INVALID_STATE = "IST";
    String ACTION_TOO_EARLY = "ATE";
    String ACTION_TOO_LATE = "ATL";
    String DELIVERY_NOT_COMPLETED = "DNC";
    String CONTRACT_EXPIRED = "CE";
    String INVALID_REQUESTED_STATE = "IRS";
}
