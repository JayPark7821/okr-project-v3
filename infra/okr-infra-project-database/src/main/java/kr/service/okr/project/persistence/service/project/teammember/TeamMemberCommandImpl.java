package kr.service.okr.project.persistence.service.project.teammember;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.domain.TeamMember;
import kr.service.okr.project.persistence.entity.project.team.TeamMemberJpaEntity;
import kr.service.okr.project.persistence.repository.project.teammeber.TeamMemberJpaRepository;
import kr.service.okr.project.repository.TeamMemberCommand;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class TeamMemberCommandImpl implements TeamMemberCommand {

	private final TeamMemberJpaRepository teamMemberJpaRepository;

	@Override
	public List<TeamMember> saveAll(final List<TeamMember> teamMember) {
		return teamMember.stream()
			.map(teamMember1 -> teamMemberJpaRepository.save(new TeamMemberJpaEntity(teamMember1)))
			.map(TeamMemberJpaEntity::toDomain)
			.toList();
	}
}
