package com.exception_study.api.product.service;

import com.exception_study.api.product.dto.*;
import com.exception_study.api.product.dto.response.*;
import com.exception_study.api.product.entity.*;
import com.exception_study.api.product.repository.*;
import com.exception_study.api.product_order.dto.*;
import com.exception_study.api.product_order.repository.*;
import com.exception_study.api.user_account.dto.request.*;
import com.exception_study.api.user_account.entity.*;
import com.exception_study.api.user_account.repository.*;
import com.exception_study.global.exception.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserAccountRepository userAccountRepository;
    private final ProductOrderRepository productOrderRepository;

    @Transactional(readOnly = true)
    public List<ProductDto> getList() {
        return productRepository.findAll().stream().map(ProductDto::from).toList();
    }

    public void register(RegisterRequest dto, String userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.USER_NOT_FOUND,String.format("user id is %s",userId))
        );
        Product product = Product.of(dto.getName(), dto.getPrice(),dto.getQuantity(),userAccount);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public ProductDto getDetails(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.PRODUCT_NOT_FOUND,String.format("product id is %d",id))
        );
        return ProductDto.from(product);
    }

    @Transactional(readOnly = true)
    public DetailsWithHistoryResponse getDetails(Long id, String userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.USER_NOT_FOUND,String.format("user id is %s",userId))
        );
        Product product = productRepository.findById(id).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.PRODUCT_NOT_FOUND,String.format("product id is %d",id))
        );

        List<ProductOrderDto> history = productOrderRepository.findTransaction_History(product.getSeller().getUserId(),userAccount.getUserId())
                .stream().map(ProductOrderDto::from).toList();

        return DetailsWithHistoryResponse.of(ProductDto.from(product),history);
    }


}
