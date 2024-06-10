package com.exception_study.api.user_account.controller;

import com.exception_study.api.user_account.dto.*;
import com.exception_study.api.user_account.dto.request.*;
import com.exception_study.api.user_account.dto.response.*;
import com.exception_study.api.user_account.service.*;
import com.exception_study.global.dto.response.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @PostMapping("/signUp")
    public ResponseDto<Void> signUp(@RequestBody UserAccountDto dto){
        userAccountService.signUp(dto);
        return ResponseDto.success();
    }

    @PostMapping("/login")
    public ResponseDto<LoginResponse> login(@RequestBody LoginRequest dto){
        LoginResponse response = userAccountService.login(dto);
        return ResponseDto.success(response);
    }


}
