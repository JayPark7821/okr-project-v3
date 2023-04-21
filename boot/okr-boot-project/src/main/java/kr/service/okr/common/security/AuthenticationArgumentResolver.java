package kr.service.okr.common.security;

import java.lang.annotation.Annotation;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import kr.service.okr.common.security.core.context.AuthenticatedUser;
import kr.service.okr.common.security.core.context.AuthenticatedUserContextHolder;
import kr.service.okr.common.security.core.context.AuthenticatedUserContextHolderStrategy;
import kr.service.okr.common.security.core.context.AuthenticationInfo;

public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

	private AuthenticatedUserContextHolderStrategy securityContextHolderStrategy =
		AuthenticatedUserContextHolder.getContextHolderStrategy();

	@Override
	public boolean supportsParameter(final MethodParameter parameter) {
		return findMethodAnnotation(AuthenticatedUser.class, parameter) != null;

	}

	@Override
	public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
		final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
		AuthenticationInfo authentication = this.securityContextHolderStrategy.getContext().getAuthenticationInfo();

		if (authentication == null || !ClassUtils.isAssignable(parameter.getParameterType(),
			authentication.getClass())) {
			return null;
		}
		return authentication;

	}

	private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass, MethodParameter parameter) {
		T annotation = parameter.getParameterAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
		for (Annotation toSearch : annotationsToSearch) {
			annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(), annotationClass);
			if (annotation != null) {
				return annotation;
			}
		}
		return null;
	}
}
