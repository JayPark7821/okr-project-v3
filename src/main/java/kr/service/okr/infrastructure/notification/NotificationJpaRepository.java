package kr.service.okr.infrastructure.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.service.okr.domain.notification.Notification;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
