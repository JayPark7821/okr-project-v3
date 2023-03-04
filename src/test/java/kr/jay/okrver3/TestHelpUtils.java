package kr.jay.okrver3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestHelpUtils {

	public static String getDateString(int calcDays, String pattern) {
		if (calcDays < 0) {
			return LocalDate.now().minusDays(calcDays * -1).format(DateTimeFormatter.ofPattern(pattern));
		} else {
			return LocalDate.now().plusDays(calcDays).format(DateTimeFormatter.ofPattern(pattern));
		}
	}
}
