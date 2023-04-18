package kr.service.okr.project.persistence.repository.project;

import static kr.service.okr.persistence.entity.project.QProjectJpaEntity.*;
import static kr.service.okr.persistence.entity.project.initiative.QInitiativeJpaEntity.*;
import static kr.service.okr.persistence.entity.project.keyresult.QKeyResultJpaEntity.*;
import static kr.service.okr.persistence.entity.project.team.QTeamMemberJpaEntity.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import kr.service.okr.project.domain.enums.ProjectType;
import kr.service.okr.project.domain.enums.SortType;
import kr.service.okr.project.persistence.entity.project.ProjectJpaEntity;

@Repository
public class ProjectQueryDslRepository {

	private final EntityManager em;
	private final JPAQueryFactory queryFactory;

	public ProjectQueryDslRepository(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	public Page<ProjectJpaEntity> getDetailProjectList(
		SortType sortType,
		ProjectType projectType,
		String includeFinishedProjectYN,
		Long requestUserSeq,
		Pageable pageable
	) {

		List<ProjectJpaEntity> results = queryFactory
			.select(projectJpaEntity)
			.from(projectJpaEntity)
			.innerJoin(projectJpaEntity.teamMember, teamMemberJpaEntity)
			.where(teamMemberJpaEntity.userSeq.eq(requestUserSeq),
				includeFinishedProject(includeFinishedProjectYN),
				projectTypeOption(projectType)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(getSortType(sortType))
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(projectJpaEntity.id)
			.from(projectJpaEntity)
			.innerJoin(projectJpaEntity.teamMember, teamMemberJpaEntity)
			.where(teamMemberJpaEntity.userSeq.eq(requestUserSeq),
				includeFinishedProject(includeFinishedProjectYN),
				projectTypeOption(projectType)
			);

		return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());
	}

	private BooleanExpression projectTypeOption(ProjectType projectType) {
		return projectType == ProjectType.ALL ? null : projectJpaEntity.type.eq(projectType);
	}

	private OrderSpecifier<?> getSortType(SortType sortType) {

		switch (sortType) {
			case DEADLINE_CLOSE:
				return projectJpaEntity.endDate.asc();
			case PROGRESS_HIGH:
				return projectJpaEntity.progress.desc();
			case PROGRESS_LOW:
				return projectJpaEntity.progress.asc();
			default:
				return projectJpaEntity.createdDate.desc();
		}
	}

	private BooleanExpression includeFinishedProject(String YN) {
		return Objects.equals(YN, "Y") ? null : projectJpaEntity.progress.lt(100);
	}

	public double getProjectProgress(Long projectId) {
		Double progress = queryFactory
			.select(new CaseBuilder().when(initiativeJpaEntity.count().eq(0L)).then(0D)
				.otherwise(
					(
						new CaseBuilder()
							.when(initiativeJpaEntity.done.isTrue()).then(1D)
							.otherwise(0D)
							.sum()
					).divide(initiativeJpaEntity.count()).multiply(100)
				))
			.from(projectJpaEntity)
			.leftJoin(projectJpaEntity.keyResults, keyResultJpaEntity)
			.leftJoin(keyResultJpaEntity.initiative, initiativeJpaEntity)
			.where(projectJpaEntity.id.eq(projectId))
			.fetchOne();

		return progress == null ? 0.0d : progress;
	}
}
