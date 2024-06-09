package com.exception_study.preonboarding.api.product.entity;

import com.exception_study.preonboarding.entity.*;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="name")
  private String name;

  @Column(name ="price")
  private int price;

  @Column(name = "status")
  private String status;

  @JoinColumn(name = "seller")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private UserAccount seller;

  public static Product of(String name, int price, UserAccount seller){
    Product entity = new Product();
    entity.setName(name);
    entity.setPrice(price);
    entity.setStatus("판매중");
    entity.setSeller(seller);
    return entity;
  }
}
