package kr.jay.okrver3.domain.notification.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.Notifications;
import kr.jay.okrver3.domain.notification.service.NotificationService;
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

	@Override
	public void sendInvitationNotification(List<User> notiSendUsers, String projectName, String invitedUserEamil) {
		notificationRepository.bulkInsert(notiSendUsers.stream()
			.map(user -> new Notification(user, Notifications.NEW_TEAM_MATE,
				Notifications.NEW_TEAM_MATE.getMsg(invitedUserEamil,projectName))).toList());
	}
}
