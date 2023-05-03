package kr.service.okr.acceptance.project.initiative;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jakarta.persistence.EntityManager;
import kr.service.okr.project.persistence.entity.project.ProjectJpaEntity;
import kr.service.okr.project.persistence.entity.project.initiative.InitiativeJpaEntity;

public class InitiativeAcceptanceTestAssertions {
	static void 행동전략_추가_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

	static void 행동전략_추가_요청_동시성_테스트_검증(EntityManager em) throws Exception {
		Thread.sleep(100L);

		final ProjectJpaEntity project = em.createQuery(
				"select p from ProjectJpaEntity p where p.id = :id", ProjectJpaEntity.class)
			.setParameter("id", 99998L)
			.getSingleResult();

		List<InitiativeJpaEntity> initiatives = em.createQuery(
				"select i from InitiativeJpaEntity i where i.keyResult.id = :id", InitiativeJpaEntity.class)
			.setParameter("id", 99999L)
			.getResultList();

		assertThat(initiatives.size()).isEqualTo(100);
		assertThat(project.getProgress()).isEqualTo(1.0);
	}
}
