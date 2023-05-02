package kr.service.okr.project.persistence.service.project.initiative;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.domain.Initiative;
import kr.service.okr.project.persistence.entity.project.initiative.InitiativeJpaEntity;
import kr.service.okr.project.persistence.repository.project.initiative.InitiativeJpaRepository;
import kr.service.okr.project.repository.InitiativeCommand;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InitiativeCommandImpl implements InitiativeCommand {

	private final InitiativeJpaRepository initiativeJpaRepository;

	@Override
	public Initiative save(final Initiative initiative) {
		return initiativeJpaRepository.save(InitiativeJpaEntity.register(initiative))
			.toDomain(initiative.getTeamMember());
	}
}
