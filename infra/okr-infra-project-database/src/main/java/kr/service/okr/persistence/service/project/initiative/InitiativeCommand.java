package kr.service.okr.persistence.service.project.initiative;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.entity.project.initiative.InitiativeJpaEntity;
import kr.service.okr.persistence.repository.project.initiative.InitiativeJpaRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InitiativeCommand {

	private final InitiativeJpaRepository initiativeJpaRepository;

	public InitiativeJpaEntity saveAndFlush(InitiativeJpaEntity initiative) {
		return initiativeJpaRepository.saveAndFlush(initiative);
	}
}
