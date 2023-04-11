package kr.service.okr.persistence.repository.project.aggregate.feedback;

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
import kr.service.okr.persistence.entity.project.aggregate.feedback.QFeedbackJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.initiative.QInitiativeJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.team.QTeamMemberJpaEntity;
import kr.service.okr.persistence.entity.user.QUserJpaEntity;

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
			.selectFrom(QFeedbackJpaEntity.feedbackJpaEntity)
			.innerJoin(QFeedbackJpaEntity.feedbackJpaEntity.initiative, QInitiativeJpaEntity.initiativeJpaEntity)
			.fetchJoin()
			.innerJoin(QInitiativeJpaEntity.initiativeJpaEntity.teamMember, QTeamMemberJpaEntity.teamMemberJpaEntity)
			.innerJoin(QFeedbackJpaEntity.feedbackJpaEntity.teamMember, writerTeamMember)
			.fetchJoin()
			.innerJoin(writerTeamMember.user, QUserJpaEntity.userJpaEntity)
			.fetchJoin()
			.where(QTeamMemberJpaEntity.teamMemberJpaEntity.userSeq.eq(userSeq)
					.and(QInitiativeJpaEntity.initiativeJpaEntity.done.isTrue())
				, searchRangeCondition(range))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(QFeedbackJpaEntity.feedbackJpaEntity.checked.asc(),
				QFeedbackJpaEntity.feedbackJpaEntity.createdDate.desc())
			.fetch();

		JPAQuery<FeedbackJpaEntity> countQuery = queryFactory
			.selectFrom(QFeedbackJpaEntity.feedbackJpaEntity)
			.innerJoin(QFeedbackJpaEntity.feedbackJpaEntity.initiative, QInitiativeJpaEntity.initiativeJpaEntity)
			.fetchJoin()
			.innerJoin(QInitiativeJpaEntity.initiativeJpaEntity.teamMember, QTeamMemberJpaEntity.teamMemberJpaEntity)
			.innerJoin(QFeedbackJpaEntity.feedbackJpaEntity.teamMember, writerTeamMember)
			.fetchJoin()
			.innerJoin(writerTeamMember.user, QUserJpaEntity.userJpaEntity)
			.fetchJoin()
			.where(QTeamMemberJpaEntity.teamMemberJpaEntity.userSeq.eq(userSeq)
					.and(QInitiativeJpaEntity.initiativeJpaEntity.done.isTrue())
				, searchRangeCondition(range))
			.orderBy(QFeedbackJpaEntity.feedbackJpaEntity.checked.asc(),
				QFeedbackJpaEntity.feedbackJpaEntity.createdDate.desc());

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

	private BooleanExpression searchRangeCondition(SearchRange searchRange) {
		Map<String, LocalDate> range = searchRange.getRange();
		if (range != null) {
			return QFeedbackJpaEntity.feedbackJpaEntity.createdDate.between(range.get("startDt").atStartOfDay(),
				range.get("endDt").atStartOfDay());
		} else {
			return null;
		}
	}

}
