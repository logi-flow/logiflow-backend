package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.destinationSite.request.CreateDestinationSiteRequestDto;
import com.logi_flow.backend.dto.destinationSite.request.UpdateDestinationSiteRequestDto;
import com.logi_flow.backend.dto.destinationSite.response.CreateDestinationSiteResponseDto;
import com.logi_flow.backend.dto.destinationSite.response.GetAllDestinationSiteResponseDto;
import com.logi_flow.backend.dto.destinationSite.response.UpdateDestinationSiteResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;

public interface DestinationSiteService {
    ResponseDto<CreateDestinationSiteResponseDto> createDestinationSite(UserPrincipal userPrincipal, @Valid CreateDestinationSiteRequestDto dto);

    ResponseDto<UpdateDestinationSiteResponseDto> updateDestinationSite(UserPrincipal userPrincipal, Long destinationSiteId, @Valid UpdateDestinationSiteRequestDto dto) throws AccessDeniedException;

    Page<GetAllDestinationSiteResponseDto> getAllDestinationSite(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<Void> deleteDestinationSite(UserPrincipal userPrincipal, Long destinationSiteId) throws AccessDeniedException;
}
