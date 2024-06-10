package com.exception_study.preonboarding.api.product.response;

import com.exception_study.preonboarding.api.product.dto.*;
import com.exception_study.preonboarding.dto.response.*;
import lombok.*;

@AllArgsConstructor
@Data
public class DetailsWithHistory {
  private ProductDto product;
  private List<ProductOrderDto> history;

  public static DetailsWithHistoryResponse of(ProductDto product, List<ProductOrderDto> history){
    return new DetailsWithHistoryResponse((product, history))
  }
}
