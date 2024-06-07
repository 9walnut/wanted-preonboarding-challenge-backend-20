package com.exception_study.preonboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyApplicationException extends RuntimeException{
  private com.exception_study.exception.ErrorCode errorCode;
  private String message;

  public StudyApplicationException(com.exception_study.exception.ErrorCode errorCode){
    this.errorCode = errorCode;
    this.message = null;
  }

  @Override
  public String getMessage(){
    if(message == null){
      return errorCode.getMessage();
    }else{
      return String.format("%s, %s", errorCode.getMessage(),message);
    }
  }
}