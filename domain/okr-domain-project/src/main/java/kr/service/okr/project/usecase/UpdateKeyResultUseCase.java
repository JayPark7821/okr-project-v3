package kr.service.okr.project.usecase;

public interface UpdateKeyResultUseCase {

	void command(Command command);

	record Command(
		String keyResultToken,
		String keyResultName,
		Long requesterSeq
	) {
	}
}
