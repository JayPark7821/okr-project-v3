package kr.service.okr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kr.service.okr.Persistence;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
	basePackageClasses = {Persistence.class}
)
public class ProjectJpaConfiguration {
}
