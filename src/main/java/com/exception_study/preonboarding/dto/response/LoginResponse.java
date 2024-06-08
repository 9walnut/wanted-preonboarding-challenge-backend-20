package com.exception_study.preonboarding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.*;

@AllArgsConstructor
@Getter
public class LoginResponse {
  private String token;
  private long expiredTime;

  public static LoginResponse of(String token, long expiredTime){
    return new LoginResponse(token, expiredTime);
  }
}
