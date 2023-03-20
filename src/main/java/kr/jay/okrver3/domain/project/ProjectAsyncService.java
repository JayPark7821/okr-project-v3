package kr.jay.okrver3.domain.project;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAsyncService {

	private final ProjectRepository projectRepository;

	@Async("asyncTaskExecutor")
	@Transactional
	public void updateProjectProgress(Long projectId) {
		log.info("============ update progress start");
		// try {
		// 	Thread.sleep(10000L);
		// } catch (InterruptedException e) {
		// 	throw new RuntimeException(e);
		// }
		projectRepository.findProjectForUpdateById(projectId)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN))
			.updateProgress(projectRepository.getProjectProgress(projectId));
		log.info("============ update progress inside end");
	}
}
