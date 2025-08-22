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
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.response.CreateContractResponseDto;
import com.logi_flow.backend.dto.contract.response.GetAllContractResponseDto;
import com.logi_flow.backend.dto.contract.response.UpdateContractResponseDto;
import com.logi_flow.backend.service.CollectionSiteService;
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
public class CollectionSiteController {

    private final CollectionSiteService collectionSiteService;

    private static final String MY_COLLECTION_SITE_API = "/me/collections";
    private static final String COLLECTION_SITE_ID_API = MY_COLLECTION_SITE_API + "/{collectionSiteId}";

    @PostMapping(MY_COLLECTION_SITE_API)
    public ResponseEntity<ResponseDto<CreateCollectionSiteResponseDto>> createCollectionSite(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateCollectionSiteRequestDto dto
    ){
        ResponseDto<CreateCollectionSiteResponseDto> response = collectionSiteService.createCollectionSite(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(COLLECTION_SITE_ID_API)
    public ResponseEntity<ResponseDto<UpdateCollectionSiteResponseDto>> updateCollectionSite(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long collectionSiteId,
            @Valid @RequestBody UpdateCollectionSiteRequestDto dto
    ) throws AccessDeniedException {
        ResponseDto<UpdateCollectionSiteResponseDto> response = collectionSiteService.updateCollectionSite(userPrincipal, collectionSiteId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(MY_COLLECTION_SITE_API)
    public ResponseEntity<ResponseDto<PageDto<GetAllCollectionSiteResponseDto>>> getAllCollectionSite(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllCollectionSiteResponseDto> result = collectionSiteService.getAllCollectionSite(userPrincipal, page, size, sort);
        PageDto<GetAllCollectionSiteResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(COLLECTION_SITE_ID_API)
    public ResponseEntity<ResponseDto<Void>> deleteCollectionSite(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long collectionSiteId
    ) throws AccessDeniedException {
        ResponseDto<Void> response = collectionSiteService.deleteCollectionSite(userPrincipal, collectionSiteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
