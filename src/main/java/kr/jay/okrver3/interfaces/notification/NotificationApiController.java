package kr.jay.okrver3.interfaces.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.application.notification.NotificationFacade;
import kr.jay.okrver3.common.Response;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.notification.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationApiController {

	private final NotificationFacade notificationFacade;
	private final NotificationDtoMapper mapper;

	@GetMapping
	ResponseEntity<Page<NotificationResponse>> getNotifications(
		Authentication authentication,
		Pageable pageable
	){
		return Response.successOk(
			notificationFacade.getNotifications(pageable, getUserFromAuthentication(authentication))
				.map(mapper::of)
		);

	}



	private Long getUserFromAuthentication(Authentication authentication) {
		return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_FAILED))
			.getUserSeq();
	}
}
