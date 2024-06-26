package com.exception_study.api.user_account.controller;

import com.exception_study.api.user_account.dto.*;
import com.exception_study.api.user_account.dto.request.*;
import com.exception_study.api.user_account.dto.response.*;
import com.exception_study.api.user_account.service.*;
import com.exception_study.global.exception.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 테스트 - UserAccount")
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserAccountService userAccountService;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("올바른 회원가입 요청이 오면 성공 응답을 반환한다.")
    @Test
    @WithAnonymousUser
    public void givenUserInfo_whenRequestingSigningUp_thenReturnsOk() throws Exception {
        //given
        String userId = "test";
        String password = "1234";
        String userName = "테스터";
        UserAccountDto dto = UserAccountDto.of(userId, password, userName);

        //when&then
        mockMvc.perform(post("/api/v1/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(dto))
        ).andExpect(status().isOk());
        verify(userAccountService).signUp(any(UserAccountDto.class));
    }

    @DisplayName("이미 존재하는 회원정보로 요청이 오면 예외를 반환한다.")
    @Test
    @WithAnonymousUser
    public void givenExistingUserInfo_whenRequestingJoin_thenReturnsException() throws Exception {
        //given
        String userId = "test";
        String password = "1234";
        String userName = "테스터";
        UserAccountDto dto = UserAccountDto.of(userId, password, userName);

        //when&then
        doThrow(new StudyApplicationException(ErrorCode.USER_ALREADY_EXIST))
                .when(userAccountService).signUp(any(UserAccountDto.class));

        mockMvc.perform(post("/api/v1/user/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(dto))
        ).andExpect(status().is(ErrorCode.USER_ALREADY_EXIST.getStatus().value()));
    }

    @DisplayName("올바른 로그인 요청이 오면 성공 응답을 반환한다.")
    @Test
    @WithAnonymousUser
    public void givenCorrectUserInfo_whenRequestingLogin_thenReturnsSuccess() throws Exception {
        //given
        String userId = "test";
        String password = "1234";
        LoginRequest loginRequest = LoginRequest.of(userId, password);

        //when&then
        when(userAccountService.login(loginRequest)).thenReturn(any(LoginResponse.class));

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("존재하지 않는 정보로 로그인 요청이 오면 예외를 반환한다.")
    @Test
    @WithAnonymousUser
    public void givenNotExistsUser_whenRequestingLogin_thenReturnsException() throws Exception {
        //given
        String userId = "test";
        String password = "1234";
        LoginRequest loginRequest = LoginRequest.of(userId, password);

        //when&then
        when(userAccountService.login(any())).thenThrow(new StudyApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest))
                ).andDo(print())
                .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()));
    }

    @DisplayName("틀린 정보로 로그인 요청이 오면 예외를 반환한다.")
    @Test
    @WithAnonymousUser
    public void givenWrongUserInfo_whenRequestingLogin_thenReturnsException() throws Exception {
        //given
        String userId = "test";
        String password = "1234";
        LoginRequest loginRequest = LoginRequest.of(userId, password);

        //when&then
        when(userAccountService.login(any())).thenThrow(new StudyApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest))
                ).andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getStatus().value()));
    }



}
