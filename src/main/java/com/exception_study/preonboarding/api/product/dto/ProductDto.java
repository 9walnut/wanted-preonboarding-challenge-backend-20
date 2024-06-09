package com.exception_study.preonboarding.api.product.dto;

import com.exception_study.preonboarding.dto.*;
import com.exception_study.preonboarding.entity.*;
import lombok.*;

@Data
@AllArgsConstructor
public class ProductDto {
  private Long id;
  private String name;
  private int price;
  private String status;
  private UserAccountDto seller;

  public static ProductDto of(Long id, String name, int price, String status, UserAccountDto seller){
    return new ProductDto(id,name,price,status,seller);
  }
  public static ProductDto from(Product entity){
    return ProductDto.of(
            entity.getId(),
            entity.getName(),
            entity.getPrice(),
            entity.getStatus(),
            UserAccountDto.from(entity.getSeller())
    );
  }
}