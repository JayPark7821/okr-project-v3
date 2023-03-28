package kr.service.okrcommon.common.utils;

import java.util.Arrays;
import java.util.Optional;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;

public class ClassUtils {
	public static <T> Optional<T> getSafeCastInstance(Object o, Class<T> clazz) {
		return clazz != null && clazz.isInstance(o) ? Optional.of(clazz.cast(o)) : Optional.empty();
	}

	public static <T> T getSafeCastInstance(Object[] args, Class<T> clazz) {
		return Arrays.stream(args)
			.filter(clazz::isInstance)
			.findFirst()
			.map(clazz::cast)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_FAILED));
	}

}
