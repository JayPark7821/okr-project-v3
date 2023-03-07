package kr.jay.okrver3.infrastructure.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.notification.Notification;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
