package kr.service.okr.project.usecase;

public interface RegisterKeyResultUseCase {

	String command(Command command);

	record Command(
		String projectToken,
		String keyResultName,
		Long requesterSeq
	) {
	}
}
