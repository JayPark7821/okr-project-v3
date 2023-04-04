package kr.service.okrcommonutil.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class TokenGenerator {
	private static final int TOKEN_LENGTH = 20;

	public static String randomCharacter(int length) {
		UUID uuid = UUID.randomUUID();
		long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
		return Long.toString(l, length);
	}

	public static String randomCharacterWithPrefix(String prefix) {
		return prefix + randomCharacter(TOKEN_LENGTH - prefix.length());
	}

}
