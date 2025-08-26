package com.logi_flow.backend.dto.destinationSite.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CreateDestinationSiteResponseDto {
    private Long id;
    private Long customerId;
    private String name;
    private String zipcode;
    private String phoneNumber;
    private String address;
    private String addressDetail;
    private String createdAt;
    private String updatedAt;
}
