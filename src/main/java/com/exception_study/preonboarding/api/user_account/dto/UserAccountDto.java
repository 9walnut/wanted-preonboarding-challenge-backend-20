package com.exception_study.preonboarding.api.user_account.dto;

import com.exception_study.preonboarding.entity.*;
import lombok.*;

@AllArgsConstructor
@Getter
public class UserAccountDto {
  private String userId;
  private String password;
  private String userName;

  public static UserAccountDto of(String userId, String password, String userName){
    return new UserAccountDto(userId, password, userName);
  }
  public static UserAccountDto form(UserAccount entity){
    return UserAccountDto.of(
            entity.getUserId(),
            entity.getPassword(),
            entity.getUserName()
    );
  }
}
