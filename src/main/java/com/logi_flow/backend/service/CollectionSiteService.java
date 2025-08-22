package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.collectionSite.request.CreateCollectionSiteRequestDto;
import com.logi_flow.backend.dto.collectionSite.request.UpdateCollectionSiteRequestDto;
import com.logi_flow.backend.dto.collectionSite.response.CreateCollectionSiteResponseDto;
import com.logi_flow.backend.dto.collectionSite.response.GetAllCollectionSiteResponseDto;
import com.logi_flow.backend.dto.collectionSite.response.UpdateCollectionSiteResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;

public interface CollectionSiteService {
    ResponseDto<CreateCollectionSiteResponseDto> createCollectionSite(UserPrincipal userPrincipal, @Valid CreateCollectionSiteRequestDto dto);

    ResponseDto<UpdateCollectionSiteResponseDto> updateCollectionSite(UserPrincipal userPrincipal, Long collectionSiteId, @Valid UpdateCollectionSiteRequestDto dto) throws AccessDeniedException;

    Page<GetAllCollectionSiteResponseDto> getAllCollectionSite(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<Void> deleteCollectionSite(UserPrincipal userPrincipal, Long collectionSiteId) throws AccessDeniedException;
}
