package com.logi_flow.backend.dto.delivery.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateIsHiddenRequestDto {
    private Boolean isHidden;
}
