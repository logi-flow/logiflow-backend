package com.logi_flow.backend.dto.returnDelivery.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateReturnDeliveryRequestDto {
    @NotNull(message = "도착 희망일은 필수 항목입니다.")
    private LocalDate requestDate;

    @NotBlank(message = "반품 사유는 필수 항목입니다.")
    private String reason;

    @NotBlank(message = "수거지 이름은 필수 항목입니다.")
    private String pickupName;
    @NotBlank(message = "수거지 번호는 필수 항목입니다.")
    private String pickupPhone;
    @NotBlank(message = "수거지 우편번호는 필수 항목입니다.")
    private String pickupZipcode;
    @NotBlank(message = "수거지 주소는 필수 항목입니다.")
    private String pickupAddress;
    private String pickupAddressDetail;

    @NotNull(message = "도착지 아이디는 필수 항목입니다.")
    private Long destinationSiteId;
}
