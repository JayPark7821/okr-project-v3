package kr.service.okrcommon.common.utils;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;

public class EnumLookUpUtil {
	public static <E extends Enum<E>> E lookup(Class<E> e, String code, ErrorCode errorCode) {
		E result;
		try {
			result = Enum.valueOf(e, code);
		} catch (Exception exception) {
			throw new OkrApplicationException(errorCode);
		}
		return result;
	}
}