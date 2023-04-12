package kr.service.okr.persistence.repository.project.feedback;

import static kr.service.okr.persistence.entity.project.feedback.QFeedbackJpaEntity.*;
import static kr.service.okr.persistence.entity.project.initiative.QInitiativeJpaEntity.*;
import static kr.service.okr.persistence.entity.project.team.QTeamMemberJpaEntity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kr.service.okr.persistence.entity.project.feedback.FeedbackJpaEntity;
import kr.service.okr.persistence.entity.project.team.QTeamMemberJpaEntity;
import kr.service.okr.project.domain.enums.SearchRange;

@Repository
public class FeedbackQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public FeedbackQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<FeedbackJpaEntity> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable) {
		QTeamMemberJpaEntity writerTeamMember = new QTeamMemberJpaEntity("writerTeamMember");

		List<FeedbackJpaEntity> results = queryFactory
			.selectFrom(feedbackJpaEntity)
			.innerJoin(feedbackJpaEntity.initiative, initiativeJpaEntity)
			.fetchJoin()
			.innerJoin(initiativeJpaEntity.teamMember, teamMemberJpaEntity)
			.innerJoin(feedbackJpaEntity.teamMember, writerTeamMember)
			.fetchJoin()
			.where(teamMemberJpaEntity.userSeq.eq(userSeq)
					.and(initiativeJpaEntity.done.isTrue())
				, searchRangeCondition(range))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(feedbackJpaEntity.checked.asc(),
				feedbackJpaEntity.createdDate.desc())
			.fetch();

		JPAQuery<FeedbackJpaEntity> countQuery = queryFactory
			.selectFrom(feedbackJpaEntity)
			.innerJoin(feedbackJpaEntity.initiative, initiativeJpaEntity)
			.fetchJoin()
			.innerJoin(initiativeJpaEntity.teamMember, teamMemberJpaEntity)
			.innerJoin(feedbackJpaEntity.teamMember, writerTeamMember)
			.fetchJoin()
			.where(teamMemberJpaEntity.userSeq.eq(userSeq)
					.and(initiativeJpaEntity.done.isTrue())
				, searchRangeCondition(range))
			.orderBy(feedbackJpaEntity.checked.asc(),
				feedbackJpaEntity.createdDate.desc());

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

	private BooleanExpression searchRangeCondition(SearchRange searchRange) {
		Map<String, LocalDate> range = searchRange.getRange();
		if (range != null) {
			return feedbackJpaEntity.createdDate.between(range.get("startDt").atStartOfDay(),
				range.get("endDt").atStartOfDay());
		} else {
			return null;
		}
	}

}
