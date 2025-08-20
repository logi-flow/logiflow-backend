package com.logi_flow.backend.dto.delivery.request;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateDeliveryRequestDto {
    @NotNull(message = "계약 ID는 필수 항목입니다.")
    private Long contractId;
    @NotNull(message = "도착 희망일은 필수 항목입니다.")
    private LocalDateTime requestDate;
    @NotBlank(message = "품목은 필수 항목입니다.")
    private String item;
    @NotNull(message = "무게는 필수 항목입니다.")
    private BigDecimal weight;

    private String message;
    private boolean isHidden;

    @NotNull(message = "배송 상태 선택은 필수입니다.")
    private DeliveryStatus status;

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
