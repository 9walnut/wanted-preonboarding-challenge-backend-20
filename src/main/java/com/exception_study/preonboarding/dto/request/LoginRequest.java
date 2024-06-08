package com.exception_study.preonboarding.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
  private String userId;
  private String password;
}
