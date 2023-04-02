package kr.service.okrbatch.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
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
	private final DataSource dataSource;

	public ProjectDoneNotificationConfiguration(
		final JobRepository jobRepository,
		final PlatformTransactionManager transactionManager,
		final DataSource dataSource) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
		this.dataSource = dataSource;
	}

	@Bean
	public Job projectDoneNotificationJob() throws Exception {
		return new JobBuilder("projectDoneNotificationJob", this.jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(this.addProjectDoneNotificationStep())
			//TODO : push 알림
			.build();
	}

	@Bean
	public Step addProjectDoneNotificationStep() throws Exception {
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

	private ItemReader<? extends NotificationEntity> finishedProjectReader() throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("endDate", LocalDate.now());

		final JdbcPagingItemReader<NotificationEntity> projectReader = new JdbcPagingItemReaderBuilder<NotificationEntity>()
			.dataSource(this.dataSource)
			.rowMapper((resultSet, i) -> NotificationEntity.builder()
				.userSeq(resultSet.getLong(1))
				.projectName(resultSet.getString(2))
				.build())
			.pageSize(CHUNK_SIZE)
			.name("finishedProjectReader")
			.selectClause("t1.user_seq, t2.project_name")
			.fromClause("team_member t1 "
				+ "inner join project t2 "
				+ "on t1.project_id = t2.project_id")
			.whereClause("t2.project_edt = :endDate")
			.parameterValues(param)
			.build();

		projectReader.afterPropertiesSet();
		return projectReader;
	}

	private ItemWriter<? super NotificationEntity> notificationProcessor() {
		throw new IllegalStateException(
			"ProjectDoneNotificationConfiguration::notificationProcessor not implemented yet");
	}
}
