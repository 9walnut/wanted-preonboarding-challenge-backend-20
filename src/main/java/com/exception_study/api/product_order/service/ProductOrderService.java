package com.exception_study.api.product_order.service;

import com.exception_study.api.product.entity.*;
import com.exception_study.api.product.repository.*;
import com.exception_study.api.product_order.dto.*;
import com.exception_study.api.product_order.dto.response.*;
import com.exception_study.api.product_order.entity.*;
import com.exception_study.api.product_order.repository.*;
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
public class ProductOrderService {

    private final UserAccountRepository userAccountRepository;
    private final ProductRepository productRepository;
    private final ProductOrderRepository productOrderRepository;


    public ProductOrderDto reserve(Long productId, String userId) {
        UserAccount buyer = userAccountRepository.findById(userId).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.USER_NOT_FOUND, String.format("user id is %s", userId))
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.PRODUCT_NOT_FOUND, String.format("product id is %d", productId))
        );

        ProductOrder orderCheck = productOrderRepository.findByProduct_IdAndBuyer(productId, buyer);
        if (orderCheck != null) {
            throw new StudyApplicationException(ErrorCode.PRODUCT_ALREADY_RESERVED);
        }

        int current_quantity = product.getQuantity();
        if (current_quantity <= 0) {
            throw new StudyApplicationException(ErrorCode.PRODUCT_SOLD_OUT);
        }

        ProductOrder productOrder = ProductOrder.of(null, product, product.getPrice(), product.getSeller(), buyer, "판매중", null);
        productOrderRepository.save(productOrder);
        return ProductOrderDto.from(productOrder);
    }

    public void approve(Long id, String userId) {
        UserAccount seller = userAccountRepository.findById(userId).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.USER_NOT_FOUND, String.format("user id is %s", userId))
        );
        ProductOrder productOrder = productOrderRepository.findById(id).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.PRODUCT_ORDER_NOT_FOUND, String.format("product order id is %d", id))
        );
        if (!productOrder.getSeller().equals(seller)) {
            throw new StudyApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        Product product = productOrder.getProduct();
        int current_quantity = product.getQuantity();
        if (current_quantity == 1) {
            product.setQuantity(0);
            productOrder.setSellerStatus("예약중");
            product.setStatus("예약중");
        } else if (current_quantity < 1) {
            throw new StudyApplicationException(ErrorCode.PRODUCT_SOLD_OUT);
        } else {
            product.setQuantity(current_quantity - 1);
        }
        productOrder.setSellerStatus("예약중");
        productOrder.setBuyerStatus("예약중");
    }

    public void confirm(Long id, String userId) {
        UserAccount buyer = userAccountRepository.findById(userId).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.USER_NOT_FOUND, String.format("user id is %s", userId))
        );
        ProductOrder productOrder = productOrderRepository.findById(id).orElseThrow(
                () -> new StudyApplicationException(ErrorCode.PRODUCT_ORDER_NOT_FOUND, String.format("product order id is %d", id))
        );
        if (!productOrder.getBuyer().equals(buyer)) {
            throw new StudyApplicationException(ErrorCode.INVALID_PERMISSION);
        }
        if ((productOrder.getBuyerStatus().equals("예약중") && productOrder.getSellerStatus().equals("예약중"))) {
            Product product = productOrder.getProduct();
            int current_quantity = product.getQuantity();
            productOrder.setSellerStatus("완료");
            productOrder.setBuyerStatus("완료");
            if (current_quantity == 0) {
                boolean flag = false;
                List<ProductOrder> orders = productOrderRepository.findAllByProductName(product.getName());
                for (ProductOrder order : orders) {
                    String sellerStatus = order.getSellerStatus();
                    if (sellerStatus.equals("예약중")) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    product.setStatus("완료");
                }
            }
        } else {
            throw new StudyApplicationException(ErrorCode.PRODUCT_ORDER_STATUS_ABNORMAL);
        }
    }


    public HistoryResponse history(String userId) {
        List<ProductOrderDto> buyList = productOrderRepository.findAllByBuyer(userId).stream().map(ProductOrderDto::from).toList();
        List<ProductOrderDto> reservedList = productOrderRepository.findReservedProducts(userId).stream().map(ProductOrderDto::from).toList();

        return HistoryResponse.of(buyList, reservedList);
    }


}
