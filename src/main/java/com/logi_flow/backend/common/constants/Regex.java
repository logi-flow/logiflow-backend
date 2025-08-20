package com.logi_flow.backend.common.constants;

public interface Regex {
    String USER_NAME = "^[a-zA-Z][a-zA-Z0-9]{4,11}$";
    String PASSWORD =  "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&*()-_=+])[A-Za-z0-9~!@#$%^&*()-_=+]{8,15}$";
    String NAME_KOREAN = "^[가-힣]{2,10}$";
    String BUSINESS_NUMBER = "^([0-9]{3})-([0-9]{2})-([0-9]{5})$";
    String IDENTITY_NUMBER = "^([0-9]{6})-([0-9]{7})$";
    String TELEPHONE = "^([0-9]{3})-([0-9]{3,4})-([0-9]{4})$";
    String PHONE_NUMBER = "^(01[0-9]{1})[ -]?([0-9]{3,4})[ -]?([0-9]{4})$";
    String EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    String ZIPCODE = "^\\d{5}$";
    String ADDRESS = "^[가-힣a-zA-Z0-9\\s\\-,]{5,100}$";
    String ADDRESS_DETAIL = "^[가-힣a-zA-Z0-9\\s\\-\\.,/#]{1,100}$";
    String BUSINESS = "^[가-힣a-zA-Z0-9\\s·&/()\\-]{1,50}$";
    String FAX = "^(0\\d{1,2})-(\\d{3,4})-(\\d{4})$";
}