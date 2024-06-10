package com.exception_study.preonboarding.api.user_account.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
public class UserAccount {
  @Id
  @Column(name ="user_Id")
  private String userId;

  @Column(name = "password")
  private String password;

  @Column(name = "user_name")
  private String userName;

  @Override
  public boolean equals(Object o){
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserAccount that = (UserAccount) o;
    return Objects.equals(userId, that.userId) && Objects.equals(password, that.password);
  }
}
