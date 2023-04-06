package kr.service.okr.project.usecase;

import kr.service.okr.project.domain.Project;

public interface QueryProjectByProjectTokenAndUserSeqUseCase {

	Project findProjectBy(Query query);

	record Query(
		String projectToken,
		Long userSeq
	) {
	}
}
