package kr.service.okr.project.persistence.repository.project.teammeber;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.service.okr.project.persistence.entity.project.team.TeamMemberId;
import kr.service.okr.project.persistence.entity.project.team.TeamMemberJpaEntity;

public interface TeamMemberJpaRepository extends JpaRepository<TeamMemberJpaEntity, TeamMemberId> {
}
