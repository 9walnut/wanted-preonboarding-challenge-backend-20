package com.exception_study.preonboarding.service;

import com.exception_study.preonboarding.dto.*;
import com.exception_study.preonboarding.dto.request.*;
import com.exception_study.preonboarding.dto.response.*;
import com.exception_study.preonboarding.entity.*;
import com.exception_study.preonboarding.exception.*;
import com.exception_study.preonboarding.global.exception.*;
import com.exception_study.preonboarding.global.config.security.*;
import com.exception_study.preonboarding.repository.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class UserAccountService {
  private final UserAccountRepository userAccountRepository;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;

  @Value("JwtSecretKey")
  private String SECRET_KEY;

  public SignUpResponse signUp(UserAccountDto dto){
    log.info("input Data {},{}",dto.getUserId(),dto.getPassword());
    if(userAccountRepository.existsById(dto.getUserId())){
      throw new StudyApplicationException(ErrorCode.USER_ALREADY_EXIST);
    }

    String encodedPassword = passwordEncoder.encode(dto.getPassword());
    UserAccount userAccount = UserAccount.of(dto.getUserId(), encodedPassword,dto.getUserName());
    userAccountRepository.save(userAccount);
    return SignUpResponse.of("SignUp Success");
  }

  public LoginResponse login(LoginRequest request){
    String userId = request.getUserId();
    String password = request.getPassword();

    UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(
            () -> new StudyApplicationException(com.exception_study.exception.ErrorCode.USER_NOT_FOUND)
    );
    if(!passwordEncoder.matches(password,userAccount.getPassword())){
      throw new StudyApplicationException(com.exception_study.exception.ErrorCode.INVALID_PASSWORD);
    }
    String token = tokenProvider.createAccessToken(userId);
    long expiredTime = 1800000L;

    return LoginResponse.of(token,expiredTime);
  }

}
