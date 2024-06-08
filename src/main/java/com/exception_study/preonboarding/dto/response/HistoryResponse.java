package com.exception_study.preonboarding.dto.response;

import com.exception_study.preonboarding.dto.*;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@Data
public class HistoryResponse {
  private List<ProductDto> buy_history;
  private List<ProductDto> reserve_history;

  public static HistoryResponse of(List<ProductDto> buy_history, List<ProductDto> reserve_history){
    return new HistoryResponse(buy_history, reserve_history);
  }
}
