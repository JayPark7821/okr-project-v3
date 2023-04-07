package kr.service.okr.persistence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kr.service.okr.persistence.Persistence;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
	basePackageClasses = {Persistence.class}
)
public class JpaConfiguration {
}
