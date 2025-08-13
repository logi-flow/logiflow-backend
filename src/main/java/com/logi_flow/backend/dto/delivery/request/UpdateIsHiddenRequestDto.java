package com.logi_flow.backend.dto.delivery.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateIsHiddenRequestDto {
    private boolean isHidden;
}
