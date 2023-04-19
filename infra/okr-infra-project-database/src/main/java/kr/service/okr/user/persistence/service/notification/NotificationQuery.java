package kr.service.okr.user.persistence.service.notification;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.persistence.entity.notification.NotificationJpaEntity;
import kr.service.okr.user.persistence.repository.notification.NotificationJpaRepository;
import kr.service.okr.user.persistence.repository.notification.NotificationQueryDslRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationQuery {

	private final NotificationQueryDslRepository notificationQueryDslRepository;
	private final NotificationJpaRepository notificationJpaRepository;

	public Page<NotificationJpaEntity> findNotificationByUserSeq(Pageable pageable, Long userSeq) {
		return notificationQueryDslRepository.findNotificationByUserSeq(pageable, userSeq);
	}

	public Optional<NotificationJpaEntity> findByNotificationTokenAndUserSeq(final String notificationToken,
		final Long userSeq) {
		return notificationJpaRepository.findByNotificationTokenAndUserSeq(notificationToken, userSeq);
	}

}
