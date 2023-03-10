package kr.jay.okrver3.infrastructure.project;

import static kr.jay.okrver3.domain.project.QProject.*;
import static kr.jay.okrver3.domain.project.aggregate.initiative.QInitiative.*;
import static kr.jay.okrver3.domain.project.aggregate.keyresult.QKeyResult.*;
import static kr.jay.okrver3.domain.project.aggregate.team.QTeamMember.*;
import static kr.jay.okrver3.domain.user.QUser.*;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;

@Repository
public class ProjectQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public ProjectQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<Project> getDetailProjectList(ProjectDetailRetrieveCommand command, Long requestUserSeq) {

		List<Project> results = queryFactory
			.select(project)
			.from(project)
			.innerJoin(project.teamMember, teamMember)
			.innerJoin(teamMember.user, user)
			.where(user.userSeq.eq(requestUserSeq),
				includeFinishedProject(command.includeFinishedProjectYN()),
				projectTypeOption(command.projectType())
			)
			.offset(command.pageable().getOffset())
			.limit(command.pageable().getPageSize())
			.orderBy(getSortType(command.sortType()))
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(project.id)
			.from(project)
			.innerJoin(project.teamMember, teamMember)
			.innerJoin(teamMember.user, user)
			.where(user.userSeq.eq(requestUserSeq),
				includeFinishedProject(command.includeFinishedProjectYN()),
				projectTypeOption(command.projectType())
			);

		return PageableExecutionUtils.getPage(results, command.pageable(), () -> countQuery.fetch().size());
	}

	private BooleanExpression projectTypeOption(ProjectType projectType) {
		return projectType == ProjectType.ALL ? null : project.type.eq(projectType);
	}

	private OrderSpecifier<?> getSortType(SortType sortType) {

		switch (sortType) {
			case DEADLINE_CLOSE:
				return project.endDate.asc();
			case PROGRESS_HIGH:
				return project.progress.desc();
			case PROGRESS_LOW:
				return project.progress.asc();
			default:
				return project.createdDate.desc();
		}
	}

	private BooleanExpression includeFinishedProject(String YN) {
		return Objects.equals(YN, "Y") ? null : project.progress.lt(100);
	}

	public double getProjectProgress(Long projectId) {
		return queryFactory
			.select(new CaseBuilder().when(initiative.count().eq(0L)).then(0D)
				.otherwise(
					(new CaseBuilder()
						.when(initiative.done.isTrue()).then(1D)
						.otherwise(0D)
						.sum()).divide(initiative.count()).multiply(100)
				))
			.from(project)
			.innerJoin(project.keyResults, keyResult)
			.innerJoin(keyResult.initiative, initiative)
			.where(project.id.eq(projectId))
			.fetchOne();
	}
}
