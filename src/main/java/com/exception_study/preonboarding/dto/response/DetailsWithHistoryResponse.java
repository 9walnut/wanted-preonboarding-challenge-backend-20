package com.exception_study.preonboarding.dto.response;

import com.exception_study.preonboarding.dto.*;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@Data
public class DetailsWithHistoryResponse {
  private ProductDto product;
  private List<ProductDto> history;

  public static DetailsWithHistoryResponse of(ProductDto product, List<ProductDto> history){
    return new DetailsWithHistoryResponse(product, history);
  }
}
