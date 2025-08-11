package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.user.response.GetUserDetailResponseDto;
import com.logi_flow.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.USER_API)
public class UserController {
    private final UserService userService;

    private static final String MY_INFO_API = "/me";
    private static final String USER_ID_API = "/{userId}";
    private static final String PROFILE_IMAGE_API = MY_INFO_API + "/profile-image";

//    @GetMapping(MY_INFO_API)
//    public ResponseEntity<ResponseDto<GetUserDetailResponseDto>> getMyInfo() {
//        ResponseDto<GetUserDetailResponseDto> response = userService.getMyInfo();
//        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
//    }

//    @PutMapping(MY_INFO_API)
//    public ResponseEntity<ResponseDto<UpdateUser>>
}
