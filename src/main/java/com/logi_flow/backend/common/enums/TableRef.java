package com.logi_flow.backend.common.enums;

import lombok.Getter;

@Getter
public enum TableRef {
    ROLE("roles"),
    USER("users"),
    CUSTOMER("customers"),
    CONTRACT("contracts"),
    DRIVER("drivers"),
    EMPLOYEE("employees"),
    DRIVER_LICENSE("driver_licenses"),
    VEHICLE("vehicles"),
    DELIVERY("deliveries"),
    ASSIGNMENT("assignments"),
    ALLOCATION("allocations"),
    SCHEDULE("schedules"),
    ATTENDANCE("attendances"),
    ALLOWANCE_TYPE("allowance_types"),
    DEDUCTION_TYPE("deduction_types"),
    DRIVER_PAYROLL("driver_payrolls"),
    DRIVER_ALLOWANCE("driver_allowances"),
    DRIVER_DEDUCTION("driver_deductions");

    private final String value;

    TableRef(String value) {
        this.value = value;
    }
}
