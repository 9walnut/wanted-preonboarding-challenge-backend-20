package com.exception_study.api.product.repository;

import com.exception_study.api.product.entity.*;
import com.exception_study.api.user_account.entity.*;
import org.springframework.data.jpa.repository.*;

public interface ProductRepository extends JpaRepository<Product,Long> {


    Product findBySeller(UserAccount user);
}
