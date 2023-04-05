package kr.service.okr.persistence.repository.project.aggregate.feedback;

import static kr.service.okr.persistence.entity.project.aggregate.feedback.QFeedbackJpaEntity.*;
import static kr.service.okr.persistence.entity.project.aggregate.initiative.QInitiativeJpaEntity.*;
import static kr.service.okr.persistence.entity.project.aggregate.team.QTeamMemberJpaEntity.*;
import static kr.service.okr.persistence.entity.user.QUserJpaEntity.*;

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
import kr.service.okr.model.project.SearchRange;
import kr.service.okr.persistence.entity.project.aggregate.feedback.FeedbackJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.team.QTeamMemberJpaEntity;

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
			.innerJoin(feedbackJpaEntity.initiative, initiativeJpaEntity).fetchJoin()
			.innerJoin(initiativeJpaEntity.teamMember, teamMemberJpaEntity)
			.innerJoin(feedbackJpaEntity.teamMember, writerTeamMember).fetchJoin()
			.innerJoin(writerTeamMember.user, userJpaEntity).fetchJoin()
			.where(teamMemberJpaEntity.userSeq.eq(userSeq)
					.and(initiativeJpaEntity.done.isTrue())
				, searchRangeCondition(range))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(feedbackJpaEntity.isChecked.asc(), feedbackJpaEntity.createdDate.desc())
			.fetch();

		JPAQuery<FeedbackJpaEntity> countQuery = queryFactory
			.selectFrom(feedbackJpaEntity)
			.innerJoin(feedbackJpaEntity.initiative, initiativeJpaEntity).fetchJoin()
			.innerJoin(initiativeJpaEntity.teamMember, teamMemberJpaEntity)
			.innerJoin(feedbackJpaEntity.teamMember, writerTeamMember).fetchJoin()
			.innerJoin(writerTeamMember.user, userJpaEntity).fetchJoin()
			.where(teamMemberJpaEntity.userSeq.eq(userSeq)
					.and(initiativeJpaEntity.done.isTrue())
				, searchRangeCondition(range))
			.orderBy(feedbackJpaEntity.isChecked.asc(), feedbackJpaEntity.createdDate.desc());

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
