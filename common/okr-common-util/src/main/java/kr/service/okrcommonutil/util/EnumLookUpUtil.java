package kr.service.okrcommonutil.util;

public class EnumLookUpUtil {
	public static <E extends Enum<E>> E lookup(Class<E> e, String code, String errorMsg) {
		E result;
		try {
			result = Enum.valueOf(e, code);
		} catch (Exception exception) {
			throw new IllegalArgumentException(errorMsg);
		}
		return result;
	}
}