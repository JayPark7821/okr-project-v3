package kr.service.okr.common.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class TokenGeneratorTest {

	@Test
	void randomCharacterWithPrefixTest() {
		assertThat(TokenGenerator.randomCharacterWithPrefix("prefix-")).containsPattern(
			Pattern.compile("prefix-[a-zA-Z0-9]{13}"));
	}
}