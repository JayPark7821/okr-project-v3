package kr.jay.okrver3.common.utils;

import java.util.Arrays;
import java.util.Optional;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;

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
