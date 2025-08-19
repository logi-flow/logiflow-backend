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
    String NOT_MATCH_PASSWORD = "NMP";
    String NOT_MATCH = "NM";
    String NOT_MATCH_INFORMATION = "NMI";
    String INTERNAL_SERVER_ERROR = "ISE";
  
    String ALREADY_OPEN_ATTENDANCE = "AOA";

    String EXISTS_TYPE_CODE = "ETC";
}
