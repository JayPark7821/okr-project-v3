package kr.jay.okrver3.domain.notification;

import java.util.List;

import kr.jay.okrver3.domain.user.User;

public interface NotificationService {
	void sendInvitationNotification(List<User> notiSendUsers, String projectName, String invitedUserEamil);

}
