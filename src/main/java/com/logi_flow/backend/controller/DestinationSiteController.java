package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.collectionSite.request.CreateCollectionSiteRequestDto;
import com.logi_flow.backend.dto.collectionSite.request.UpdateCollectionSiteRequestDto;
import com.logi_flow.backend.dto.collectionSite.response.CreateCollectionSiteResponseDto;
import com.logi_flow.backend.dto.collectionSite.response.GetAllCollectionSiteResponseDto;
import com.logi_flow.backend.dto.collectionSite.response.UpdateCollectionSiteResponseDto;
import com.logi_flow.backend.dto.destinationSite.request.CreateDestinationSiteRequestDto;
import com.logi_flow.backend.dto.destinationSite.request.UpdateDestinationSiteRequestDto;
import com.logi_flow.backend.dto.destinationSite.response.CreateDestinationSiteResponseDto;
import com.logi_flow.backend.dto.destinationSite.response.GetAllDestinationSiteResponseDto;
import com.logi_flow.backend.dto.destinationSite.response.UpdateDestinationSiteResponseDto;
import com.logi_flow.backend.service.CollectionSiteService;
import com.logi_flow.backend.service.DestinationSiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CUSTOMER_API)
public class DestinationSiteController {

    private final DestinationSiteService destinationSiteService;

    private static final String MY_DESTINATION_SITE_API = "/me/destinations";
    private static final String DESTINATION_SITE_ID_API = MY_DESTINATION_SITE_API + "/{destinationSiteId}";

    @PostMapping(MY_DESTINATION_SITE_API)
    public ResponseEntity<ResponseDto<CreateDestinationSiteResponseDto>> createDestinationSite(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateDestinationSiteRequestDto dto
    ){
        ResponseDto<CreateDestinationSiteResponseDto> response = destinationSiteService.createDestinationSite(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(DESTINATION_SITE_ID_API)
    public ResponseEntity<ResponseDto<UpdateDestinationSiteResponseDto>> updateDestinationSite(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long destinationSiteId,
            @Valid @RequestBody UpdateDestinationSiteRequestDto dto
    ) throws AccessDeniedException {
        ResponseDto<UpdateDestinationSiteResponseDto> response = destinationSiteService.updateDestinationSite(userPrincipal, destinationSiteId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(MY_DESTINATION_SITE_API)
    public ResponseEntity<ResponseDto<PageDto<GetAllDestinationSiteResponseDto>>> getAllDestinationSite(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDestinationSiteResponseDto> result = destinationSiteService.getAllDestinationSite(userPrincipal, page, size, sort);
        PageDto<GetAllDestinationSiteResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(DESTINATION_SITE_ID_API)
    public ResponseEntity<ResponseDto<Void>> deleteDestinationSite(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long destinationSiteId
    ) throws AccessDeniedException {
        ResponseDto<Void> response = destinationSiteService.deleteDestinationSite(userPrincipal, destinationSiteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
