package com.exception_study.api.user_account.dto.response;

import lombok.*;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private String token;
    private long expiredTime;

    public static LoginResponse of(String token, long expiredTime){
        return new LoginResponse(token,expiredTime);
    }
}
