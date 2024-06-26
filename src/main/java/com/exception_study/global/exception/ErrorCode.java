package com.exception_study.global.exception;

import lombok.*;
import org.springframework.http.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT,"User already exists"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND,"Product not founded"),
    PRODUCT_SOLD_OUT(HttpStatus.CONFLICT,"Product sold out"),
    PRODUCT_ALREADY_RESERVED(HttpStatus.CONFLICT,"Already reserved product"),
    PRODUCT_ORDER_STATUS_ABNORMAL(HttpStatus.CONFLICT,"Product order already reserved or not approved"),
    PRODUCT_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"Product order not founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "User has invalid permission"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurs");

    private final HttpStatus status;
    private final String message;
}
