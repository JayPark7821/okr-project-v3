package kr.jay.okrver3.infrastructure.project.aggregate.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;

public interface FeedbackJpaRepository extends JpaRepository<Feedback, Long> {
}
