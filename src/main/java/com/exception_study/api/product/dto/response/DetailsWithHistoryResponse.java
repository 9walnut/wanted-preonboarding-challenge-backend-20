package com.exception_study.api.product.dto.response;

import com.exception_study.api.product.dto.*;
import com.exception_study.api.product_order.dto.*;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@Data
public class DetailsWithHistoryResponse {
    private ProductDto product;
    private List<ProductOrderDto> history;


    public static DetailsWithHistoryResponse of(ProductDto product, List<ProductOrderDto> history){
        return new DetailsWithHistoryResponse(product,history);
    }
}
