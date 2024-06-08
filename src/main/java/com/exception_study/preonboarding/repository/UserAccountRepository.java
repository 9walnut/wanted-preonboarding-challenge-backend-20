package com.exception_study.preonboarding.repository;

import com.exception_study.preonboarding.entity.*;
import org.springframework.data.jpa.repository.*;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
