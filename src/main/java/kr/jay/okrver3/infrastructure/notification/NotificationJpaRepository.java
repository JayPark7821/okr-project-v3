package kr.jay.okrver3.infrastructure.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.service.impl.NotificationRepository;

public interface NotificationJpaRepository extends NotificationRepository, JpaRepository<Notification, Long> {

}

