package kr.jay.okrver3.infrastructure.project.aggregate.feedback;

import static kr.jay.okrver3.domain.project.aggregate.feedback.QFeedback.*;
import static kr.jay.okrver3.domain.project.aggregate.initiative.QInitiative.*;
import static kr.jay.okrver3.domain.project.aggregate.team.QTeamMember.*;
import static kr.jay.okrver3.domain.user.QUser.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;
import kr.jay.okrver3.domain.project.aggregate.feedback.SearchRange;
import kr.jay.okrver3.domain.project.aggregate.team.QTeamMember;

@Repository
public class FeedbackQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public FeedbackQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<Feedback> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable) {
		QTeamMember writerTeamMember = new QTeamMember("writerTeamMember");

		List<Feedback> results = queryFactory
			.selectFrom(feedback)
			.innerJoin(feedback.initiative, initiative).fetchJoin()
			.innerJoin(initiative.teamMember, teamMember)
			.innerJoin(feedback.teamMember, writerTeamMember).fetchJoin()
			.innerJoin(writerTeamMember.user, user).fetchJoin()
			.where(teamMember.userSeq.eq(userSeq)
					.and(initiative.done.isTrue())
				, searchRangeCondition(range))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(feedback.isChecked.asc(), feedback.createdDate.desc())
			.fetch();

		JPAQuery<Feedback> countQuery = queryFactory
			.selectFrom(feedback)
			.innerJoin(feedback.initiative, initiative).fetchJoin()
			.innerJoin(initiative.teamMember, teamMember)
			.innerJoin(feedback.teamMember, writerTeamMember).fetchJoin()
			.innerJoin(writerTeamMember.user, user).fetchJoin()
			.where(teamMember.userSeq.eq(userSeq)
					.and(initiative.done.isTrue())
				, searchRangeCondition(range))
			.orderBy(feedback.isChecked.asc(), feedback.createdDate.desc());

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

	private BooleanExpression searchRangeCondition(SearchRange searchRange) {
		Map<String, LocalDate> range = searchRange.getRange();
		if (range != null) {
			return feedback.createdDate.between(range.get("startDt").atStartOfDay(), range.get("endDt").atStartOfDay());
		} else {
			return null;
		}
	}

}
