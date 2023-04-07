package kr.service.okr.project.usecase;

import kr.service.okr.project.domain.Project;

public interface QueryProjectUseCase {

	Project findProjectBy(QueryProjectBy query);

	record QueryProjectBy(
		String projectToken,
		Long userSeq
	) {
	}

}
