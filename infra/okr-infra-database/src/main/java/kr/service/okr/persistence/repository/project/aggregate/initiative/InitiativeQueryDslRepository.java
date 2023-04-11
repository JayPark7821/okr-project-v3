package kr.service.okr.persistence.repository.project.aggregate.initiative;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kr.service.okr.persistence.entity.project.aggregate.initiative.InitiativeJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.initiative.QInitiativeJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.keyresult.QKeyResultJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.team.QTeamMemberJpaEntity;
import kr.service.okr.persistence.entity.user.QUserJpaEntity;

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
			.select(QInitiativeJpaEntity.initiativeJpaEntity)
			.from(QInitiativeJpaEntity.initiativeJpaEntity)
			.innerJoin(QInitiativeJpaEntity.initiativeJpaEntity.teamMember, QTeamMemberJpaEntity.teamMemberJpaEntity)
			.fetchJoin()
			.innerJoin(QTeamMemberJpaEntity.teamMemberJpaEntity.user, QUserJpaEntity.userJpaEntity)
			.fetchJoin()
			.innerJoin(QInitiativeJpaEntity.initiativeJpaEntity.keyResult, QKeyResultJpaEntity.keyResultJpaEntity)
			.where(
				QKeyResultJpaEntity.keyResultJpaEntity.keyResultToken.eq(keyResultToken)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(QInitiativeJpaEntity.initiativeJpaEntity.createdDate.asc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(QInitiativeJpaEntity.initiativeJpaEntity.id)
			.from(QInitiativeJpaEntity.initiativeJpaEntity)
			.innerJoin(QInitiativeJpaEntity.initiativeJpaEntity.keyResult, QKeyResultJpaEntity.keyResultJpaEntity)
			.where(
				QKeyResultJpaEntity.keyResultJpaEntity.keyResultToken.eq(keyResultToken)
			);

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}
}
