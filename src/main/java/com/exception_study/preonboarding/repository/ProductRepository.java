package com.exception_study.preonboarding.repository;

import com.exception_study.preonboarding.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {

}