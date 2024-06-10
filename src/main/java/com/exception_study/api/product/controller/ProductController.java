package com.exception_study.api.product.controller;

import com.exception_study.api.product.dto.*;
import com.exception_study.api.product.dto.response.*;
import com.exception_study.api.product.service.*;
import com.exception_study.api.user_account.dto.request.*;
import com.exception_study.global.dto.response.*;
import lombok.*;
import org.springframework.security.core.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;


    @GetMapping("/list")
    public ResponseDto<List<ProductDto>> list(){
        List<ProductDto> result = productService.getList();
        return ResponseDto.success(result);
    }

    @PostMapping("/register")
    public ResponseDto<Void> register(@RequestBody RegisterRequest request, @AuthenticationPrincipal String userId){
        productService.register(request,userId);
        return ResponseDto.success();
    }

    @GetMapping("/{id}/details")
    public ResponseDto<ProductDto> details(@PathVariable Long id){
        ProductDto result = productService.getDetails(id);
        return ResponseDto.success(result);
    }

    @GetMapping("/{id}/details/authenticated")
    public ResponseDto<DetailsWithHistoryResponse> details(@PathVariable Long id, @AuthenticationPrincipal String userId){
        DetailsWithHistoryResponse result = productService.getDetails(id,userId);
        return ResponseDto.success(result);
    }

}
