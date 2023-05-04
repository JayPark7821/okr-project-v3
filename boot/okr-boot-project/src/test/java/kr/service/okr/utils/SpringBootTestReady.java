package kr.service.okr.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.service.okr.AuthenticationInfo;
import kr.service.okr.user.persistence.entity.user.UserJpaEntity;

@Import(TestConfig.class)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTestReady {

	@LocalServerPort
	int port;

	@Autowired
	public DataLoader dataLoader;

	@PersistenceContext
	public EntityManager em;

	public void setUp() {
		RestAssured.port = port;
	}

	public AuthenticationInfo getAuthenticationInfo(Long userSeq) {
		final UserJpaEntity user = em.createQuery("select u from UserJpaEntity u where u.userSeq = :userSeq",
				UserJpaEntity.class)
			.setParameter("userSeq", userSeq)
			.getSingleResult();
		return new AuthenticationInfo(user.getUserSeq(), user.getEmail(), user.getUsername());
	}
}
