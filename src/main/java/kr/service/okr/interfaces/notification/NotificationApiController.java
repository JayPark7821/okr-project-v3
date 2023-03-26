package kr.service.okr.interfaces.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.okr.application.notification.NotificationFacade;
import kr.service.okr.common.Response;
import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.common.utils.ClassUtils;
import kr.service.okr.domain.user.User;
import kr.service.okr.interfaces.notification.response.NotificationResponse;
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
	) {
		return Response.successOk(
			notificationFacade.getNotifications(pageable, getUserFromAuthentication(authentication))
				.map(mapper::of)
		);

	}

	@PutMapping("/{token}")
	public ResponseEntity<String> checkNotification(
		@PathVariable("token") String token,
		Authentication authentication
	) {
		notificationFacade.checkNotification(token, getUserFromAuthentication(authentication));
		return Response.success(HttpStatus.OK);
	}

	private Long getUserFromAuthentication(Authentication authentication) {
		return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_FAILED))
			.getUserSeq();
	}
}
