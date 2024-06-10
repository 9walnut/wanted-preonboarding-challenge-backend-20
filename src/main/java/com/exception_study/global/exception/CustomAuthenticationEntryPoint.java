package com.exception_study.global.exception;

import com.exception_study.global.dto.response.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;

import java.io.*;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, IOException {
        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write(ResponseDto.error(HttpStatus.UNAUTHORIZED.getReasonPhrase()).toStream());
    }

}