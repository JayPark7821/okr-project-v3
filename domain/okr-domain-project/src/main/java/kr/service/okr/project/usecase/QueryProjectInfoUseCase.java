package kr.service.okr.project.usecase;

public interface QueryProjectInfoUseCase {

	ProjectInfo query(Query query);

	record Query(
		String projectToken,
		Long userSeq
	) {
	}

}
