package com.logi_flow.backend.dto.returnDelivery.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateReturnDeliveryRequestDto {
    @NotNull(message = "도착 희망일은 필수 항목입니다.")
    private LocalDateTime requestDate;

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

    @NotBlank(message = "수령인 이름은 필수 항목입니다.")
    private String recipientName;
    @NotBlank(message = "수령인 번호는 필수 항목입니다.")
    private String recipientPhone;
    @NotBlank(message = "수령인 우편번호는 필수 항목입니다.")
    private String recipientZipcode;
    @NotBlank(message = "수령인 주소는 필수 항목입니다.")
    private String recipientAddress;
    private String recipientAddressDetail;
}
