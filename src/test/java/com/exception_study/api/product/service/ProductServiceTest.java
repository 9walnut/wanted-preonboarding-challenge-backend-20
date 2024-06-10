package com.exception_study.api.product.service;

import com.exception_study.api.product.entity.*;
import com.exception_study.api.product.repository.*;
import com.exception_study.api.product_order.entity.*;
import com.exception_study.api.product_order.repository.*;
import com.exception_study.api.user_account.dto.request.*;
import com.exception_study.api.user_account.entity.*;
import com.exception_study.api.user_account.repository.*;
import com.exception_study.global.exception.*;
import com.exception_study.global.fixture.product.*;
import com.exception_study.global.fixture.product_order.*;
import com.exception_study.global.fixture.user.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("비즈니스 로직 - 상품 등록과 조회")
@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {
    @Autowired
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    UserAccountRepository userAccountRepository;

    @MockBean
    ProductOrderRepository productOrderRepository;

    @DisplayName("상품 리스트를 조회하면 성공한다.")
    @Test
    public void givenNothing_whenGettingList_thenSuccess() {
        //given
        ProductInfoFixture.TestInfo productFixture = ProductInfoFixture.get();
        UserInfoFixture.TestInfo userFixture = UserInfoFixture.get();

        //when
        when(productRepository.findAll()).thenReturn(List.of(
                ProductFixture.get(
                        productFixture.getId(),
                        productFixture.getName(),
                        productFixture.getPrice(),
                        productFixture.getStatus(),
                        productFixture.getQuantity(),
                        UserAccountFixture.get(userFixture.getUserId(), userFixture.getPassword(), userFixture.getUserName())
                ))
        );
        //then
        assertDoesNotThrow(() -> productService.getList());
    }

    @DisplayName("상품을 등록하면 성공한다.")
    @Test
    public void givenProductInfo_whenRegistering_thenSuccess() {
        //given
        ProductInfoFixture.TestInfo productFixture = ProductInfoFixture.get();
        UserInfoFixture.TestInfo userFixture = UserInfoFixture.get();
        //when
        when(userAccountRepository.findById(userFixture.getUserId())).thenReturn(
                Optional.of(UserAccountFixture.get(userFixture.getUserId(), userFixture.getPassword(), userFixture.getUserName()))
        );
        when(productRepository.save(any())).thenReturn(
                ProductFixture.get(
                        productFixture.getId(),
                        productFixture.getName(),
                        productFixture.getPrice(),
                        productFixture.getStatus(),
                        productFixture.getQuantity(),
                        UserAccountFixture.get(userFixture.getUserId(), userFixture.getPassword(), userFixture.getUserName())
                ));
        //then
        assertDoesNotThrow(() -> productService.register(
                RegisterRequest.of(productFixture.getName(), productFixture.getPrice(), productFixture.getQuantity())
                , userFixture.getUserId()
        ));
    }

    @DisplayName("없는 유저가 상품을 등록하면 실패한다.")
    @Test
    public void givenNotExistsUserProductInfo_whenRegistering_thenException() {
        //given
        ProductInfoFixture.TestInfo productFixture = ProductInfoFixture.get();
        UserInfoFixture.TestInfo userFixture = UserInfoFixture.get();
        //when
        StudyApplicationException exception = Assertions.assertThrows(StudyApplicationException.class,
                () -> productService.register(
                        RegisterRequest.of(productFixture.getName(), productFixture.getPrice(), productFixture.getQuantity()),
                        userFixture.getUserId()
                ));
        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @DisplayName("상품 상세를 조회하면 성공한다.")
    @Test
    public void givenNothing_whenGettingDetails_thenSuccess() {
        //given
        ProductInfoFixture.TestInfo productFixture = ProductInfoFixture.get();
        UserInfoFixture.TestInfo userFixture = UserInfoFixture.get();

        //when
        when(productRepository.findById(anyLong())).thenReturn(
                Optional.of(ProductFixture.get(
                        productFixture.getId(),
                        productFixture.getName(),
                        productFixture.getPrice(),
                        productFixture.getStatus(),
                        productFixture.getQuantity(),
                        UserAccountFixture.get(userFixture.getUserId(), userFixture.getPassword(), userFixture.getUserName())
                ))
        );
        //then
        assertDoesNotThrow(() -> productService.getDetails(productFixture.getId()));
    }

    @DisplayName("없는 상품의 상품 상세를 조회하면 실패한다.")
    @Test
    public void givenNotExistsProductInfo_whenGettingDetails_thenException() {
        //given
        ProductInfoFixture.TestInfo productFixture = ProductInfoFixture.get();
        //when
        StudyApplicationException exception = Assertions.assertThrows(StudyApplicationException.class,
                () -> productService.getDetails(
                        productFixture.getId()
                ));
        //then
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }

    @DisplayName("상품 상세와 거래 내역을 조회하면 성공한다.")
    @Test
    public void givenUserInfo_whenGettingDetailsWithHistory_thenSuccess() {
        //given
        ProductInfoFixture.TestInfo productFixture = ProductInfoFixture.get();
        ProductOrderInfoFixture.TestInfo productOrderFixture = ProductOrderInfoFixture.get();
        UserInfoFixture.TestInfo userFixture = UserInfoFixture.get();

        UserAccount testUser = UserAccountFixture.get(userFixture.getUserId(), userFixture.getPassword(), userFixture.getUserName());
        Product testProduct = ProductFixture.get(
                productFixture.getId(),
                productFixture.getName(),
                productFixture.getPrice(),
                productFixture.getStatus(),
                productFixture.getQuantity(),
                UserAccountFixture.get(userFixture.getUserId(), userFixture.getPassword(), userFixture.getUserName()));

        ProductOrder testProductOrder = ProductOrderFixture.get(
                productOrderFixture.getId(),
                testProduct,
                productOrderFixture.getPrice(),
                testUser,
                testUser,
                productOrderFixture.getSellerStatus(),
                productOrderFixture.getBuyerStatus()
        );
        //when
        when(userAccountRepository.findById(userFixture.getUserId())).thenReturn(
                Optional.of(testUser)
        );
        when(productRepository.findById(anyLong())).thenReturn(
                Optional.of(testProduct)
        );
        when(productOrderRepository.findTransaction_History(userFixture.getUserId(), userFixture.getUserId()))
                .thenReturn(List.of(
                        testProductOrder
                ));
        //then
        assertDoesNotThrow(() -> productService.getDetails(productFixture.getId(),userFixture.getUserId()));
    }

    @DisplayName("없는 유저가 상품 상세와 거래내역을 조회하면 실패한다.")
    @Test
    public void givenNotExistsUser_whenGettingDetailsWithHistory_thenException() {
        //given
        ProductInfoFixture.TestInfo productFixture = ProductInfoFixture.get();
        UserInfoFixture.TestInfo userFixture = UserInfoFixture.get();

        //when
        StudyApplicationException exception = Assertions.assertThrows(StudyApplicationException.class,
                () -> productService.getDetails(
                        productFixture.getId(),
                        userFixture.getUserId()
                ));
        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @DisplayName("없는 상품의 상품 상세와 거래내역을 조회하면 실패한다.")
    @Test
    public void givenNotExistsProductInfo_whenGettingDetailsWithHistory_thenException() {
        //given
        ProductInfoFixture.TestInfo productFixture = ProductInfoFixture.get();
        UserInfoFixture.TestInfo userFixture = UserInfoFixture.get();
        UserAccount testUser = UserAccountFixture.get(userFixture.getUserId(), userFixture.getPassword(), userFixture.getUserName());

        //when
        when(userAccountRepository.findById(userFixture.getUserId())).thenReturn(
                Optional.of(testUser)
        );
        StudyApplicationException exception = Assertions.assertThrows(StudyApplicationException.class,
                () -> productService.getDetails(
                        productFixture.getId(),
                        userFixture.getUserId()
                ));
        //then
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
    }




}
