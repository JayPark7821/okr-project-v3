package kr.service.okr.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.EntityManager;
import kr.service.okr.domain.user.User;

public class TestHelpUtils {

	public static String getDateString(int calcDays, String pattern) {
		if (calcDays < 0) {
			return LocalDate.now().minusDays(calcDays * -1).format(DateTimeFormatter.ofPattern(pattern));
		} else {
			return LocalDate.now().plusDays(calcDays).format(DateTimeFormatter.ofPattern(pattern));
		}
	}

	public static UsernamePasswordAuthenticationToken getAuthenticationToken(EntityManager em, long value) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", value)
			.getSingleResult();

		return new UsernamePasswordAuthenticationToken(
			user, null, List.of(new SimpleGrantedAuthority(user.getRoleType().getValue())));
	}
}

