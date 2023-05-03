package kr.service.okr.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import kr.service.okr.common.RejectedExecutionHandlerImpl;

@EnableAsync
@Configuration
public class AsyncConfig {

	@Value("${task-executor.core-pool-size}")
	private int corePoolSize;

	@Value("${task-executor.max-pool-size}")
	private int maxPoolSize;

	@Value("${task-executor.queue-capacity}")
	private int queueCapacity;

	@Bean(name = "asyncTaskExecutor")
	public ThreadPoolTaskExecutor asyncTaskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setRejectedExecutionHandler(new RejectedExecutionHandlerImpl());
		executor.setThreadNamePrefix("ASYNC-");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		// executor.setAwaitTerminationSeconds(60);
		return executor;
	}
}

