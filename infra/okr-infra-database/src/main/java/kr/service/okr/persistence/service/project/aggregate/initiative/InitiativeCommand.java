package kr.service.okr.persistence.service.project.aggregate.initiative;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.entity.project.aggregate.initiative.InitiativeJpaEntity;
import kr.service.okr.persistence.repository.project.aggregate.initiative.InitiativeJpaRepository;
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
