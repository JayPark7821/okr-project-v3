package kr.service.okr.project.persistence.service.project.initiative;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.persistence.entity.project.initiative.InitiativeJpaEntity;
import kr.service.okr.project.persistence.repository.project.initiative.InitiativeJpaRepository;
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
