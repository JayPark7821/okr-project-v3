package kr.service.okrcommonutil.util;

import java.util.Arrays;
import java.util.Optional;

public class ClassUtils {
	public static <T> Optional<T> getSafeCastInstance(Object o, Class<T> clazz) {
		return clazz != null && clazz.isInstance(o) ? Optional.of(clazz.cast(o)) : Optional.empty();
	}

	public static <T> T getSafeCastInstance(Object[] args, Class<T> clazz) {
		return Arrays.stream(args)
			.filter(clazz::isInstance)
			.findFirst()
			.map(clazz::cast)
			.orElseThrow(() -> new ClassCastException("Casting failed"));
	}

}
