// package kr.service.okr.common.config;
//
// import java.util.Optional;
//
// import org.springframework.data.domain.AuditorAware;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Component;
// import org.springframework.util.ObjectUtils;
//
// import kr.service.okr.user.user.domain.User;
// import kr.service.okr.util.ClassUtils;
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// @Component
// public class AuditorProvider implements AuditorAware<String> {
// 	@Override
// 	public Optional<String> getCurrentAuditor() {
// 		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
// 		if (ObjectUtils.isEmpty(authentication) || !authentication.isAuthenticated()) {
// 			return Optional.empty();
// 		}
//
// 		return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).map(User::getUserId);
// 	}
// }