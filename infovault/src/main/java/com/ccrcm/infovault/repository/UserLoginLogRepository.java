package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {

    Optional<UserLoginLog> findTopByUserIdOrderByLoginTimeDesc(Long userId);
}
