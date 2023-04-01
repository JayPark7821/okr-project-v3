package kr.service.okrbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import kr.service.okrbatch.repository.NotificationEntity;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ProjectDoneNotificationConfiguration {

	private final int CHUNK_SIZE = 10;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	public ProjectDoneNotificationConfiguration(
		final JobRepository jobRepository,
		final PlatformTransactionManager transactionManager
	) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
	}

	@Bean
	public Job projectDoneNotificationJob() {
		return new JobBuilder("projectDoneNotificationJob", this.jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(this.addProjectDoneNotificationStep())
			//TODO : push 알림
			.build();
	}

	@Bean
	public Step addProjectDoneNotificationStep() {
		return new StepBuilder("sendProjectDoneNotificationStep", this.jobRepository)
			.<NotificationEntity, NotificationEntity>chunk(CHUNK_SIZE)
			.reader(this.finishedProjectReader())
			.processor(this.finishedProjectNotificationProcessor())
			.writer(this.notificationProcessor())
			.build();
	}

	private ItemProcessor<? super NotificationEntity, ? extends NotificationEntity> finishedProjectNotificationProcessor() {
		throw new IllegalStateException(
			"ProjectDoneNotificationConfiguration::finishedProjectNotificationProcessor not implemented yet");
	}

	private ItemReader<? extends NotificationEntity> finishedProjectReader() {
		throw new IllegalStateException(
			"ProjectDoneNotificationConfiguration::finishedProjectReader not implemented yet");
	}

	private ItemWriter<? super NotificationEntity> notificationProcessor() {
		throw new IllegalStateException(
			"ProjectDoneNotificationConfiguration::notificationProcessor not implemented yet");
	}
}
