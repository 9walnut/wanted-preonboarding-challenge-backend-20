package com.exception_study.api.user_account.repository;

import com.exception_study.api.user_account.entity.*;
import org.springframework.data.jpa.repository.*;

public interface UserAccountRepository extends JpaRepository<UserAccount,String> {
}
