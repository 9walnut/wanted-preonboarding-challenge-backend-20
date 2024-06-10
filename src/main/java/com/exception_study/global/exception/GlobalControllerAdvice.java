package com.exception_study.global.exception;

import com.exception_study.global.dto.response.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.exception_study.global.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(StudyApplicationException.class)
    public ResponseEntity<?> errorHandler(StudyApplicationException e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        if(stackTrace.length>0){
            StackTraceElement element = stackTrace[0];
            log.error("Error occurs in {}, cause {}",element.getClassName(),e.getMessage());
        }else{
            log.error("Error occurs {}",e.getMessage());
        }
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ResponseDto.error(e.getErrorCode().name()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> databaseErrorHandler(IllegalArgumentException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(DATABASE_ERROR.getStatus())
                .body(ResponseDto.error(DATABASE_ERROR.name()));
    }

}
