package kr.jay.okrver3.infrastructure.project;

import static kr.jay.okrver3.domain.project.QProject.*;
import static kr.jay.okrver3.domain.team.QTeamMember.*;
import static kr.jay.okrver3.domain.user.QUser.*;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.user.User;

@Repository
public class ProjectQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public ProjectQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<Project> getDetailProjectList(SortType sortType, ProjectType projectType,
		String includeFinishedProjectYN, User requestUser, Pageable pageable) {

		List<Project> results = queryFactory
			.select(project)
			.from(project)
			.innerJoin(project.teamMember, teamMember)
			.innerJoin(teamMember.user, user)
			.where(user.eq(requestUser),
				includeFinishedProject(includeFinishedProjectYN),
				projectTypeOption(projectType)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(getSortType(sortType))
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(project.id)
			.from(project)
			.innerJoin(project.teamMember, teamMember)
			.innerJoin(teamMember.user, user)
			.where(user.eq(requestUser),
				includeFinishedProject(includeFinishedProjectYN),
				projectTypeOption(projectType)
			);

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
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

}
