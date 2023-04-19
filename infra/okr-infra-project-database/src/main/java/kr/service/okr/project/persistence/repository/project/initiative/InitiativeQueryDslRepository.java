package kr.service.okr.project.persistence.repository.project.initiative;

import static kr.service.okr.project.persistence.entity.project.initiative.QInitiativeJpaEntity.*;
import static kr.service.okr.project.persistence.entity.project.keyresult.QKeyResultJpaEntity.*;
import static kr.service.okr.project.persistence.entity.project.team.QTeamMemberJpaEntity.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kr.service.okr.project.persistence.entity.project.initiative.InitiativeJpaEntity;

@Repository
public class InitiativeQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public InitiativeQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<InitiativeJpaEntity> findInitiativeByKeyResultTokenAndUserSeq(String keyResultToken, Long searchUserSeq,
		Pageable pageable) {

		List<InitiativeJpaEntity> results = queryFactory
			.select(initiativeJpaEntity)
			.from(initiativeJpaEntity)
			.innerJoin(initiativeJpaEntity.teamMember, teamMemberJpaEntity)
			.fetchJoin()
			.innerJoin(initiativeJpaEntity.keyResult, keyResultJpaEntity)
			.where(
				keyResultJpaEntity.keyResultToken.eq(keyResultToken)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(initiativeJpaEntity.createdDate.asc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(initiativeJpaEntity.id)
			.from(initiativeJpaEntity)
			.innerJoin(initiativeJpaEntity.keyResult, keyResultJpaEntity)
			.where(
				keyResultJpaEntity.keyResultToken.eq(keyResultToken)
			);

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}
}
