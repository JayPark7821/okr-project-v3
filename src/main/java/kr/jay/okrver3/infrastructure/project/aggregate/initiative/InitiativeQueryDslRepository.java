package kr.jay.okrver3.infrastructure.project.aggregate.initiative;

import static kr.jay.okrver3.domain.project.aggregate.initiative.QInitiative.*;
import static kr.jay.okrver3.domain.project.aggregate.keyresult.QKeyResult.*;
import static kr.jay.okrver3.domain.project.aggregate.team.QTeamMember.*;
import static kr.jay.okrver3.domain.user.QUser.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;

@Repository
public class InitiativeQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public InitiativeQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<Initiative> findInitiativeByKeyResultTokenAndUserSeq(String keyResultToken, Long searchUserSeq,
		Pageable pageable) {
		List<Initiative> results = queryFactory
			.select(initiative)
			.from(initiative)
			.innerJoin(initiative.teamMember, teamMember).fetchJoin()
			.innerJoin(teamMember.user, user).fetchJoin()
			.innerJoin(initiative.keyResult, keyResult)
			.where(
				keyResult.keyResultToken.eq(keyResultToken)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(initiative.createdDate.asc())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(initiative.iniId)
			.from(initiative)
			.innerJoin(initiative.keyResult, keyResult)
			.where(
				keyResult.keyResultToken.eq(keyResultToken)
			);

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}
}
