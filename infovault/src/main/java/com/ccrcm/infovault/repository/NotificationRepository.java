package com.ccrcm.infovault.repository;

import com.ccrcm.infovault.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByActiveTrueOrderByCreatedAtDesc();
}
