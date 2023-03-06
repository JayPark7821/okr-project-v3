package kr.jay.okrver3.infrastructure.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.feedback.Feedback;

public interface FeedbackJpaRepository extends JpaRepository<Feedback, Long> {
}
