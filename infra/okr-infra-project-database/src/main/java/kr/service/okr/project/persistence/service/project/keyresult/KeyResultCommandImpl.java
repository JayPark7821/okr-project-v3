package kr.service.okr.project.persistence.service.project.keyresult;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.domain.KeyResult;
import kr.service.okr.project.persistence.entity.project.keyresult.KeyResultJpaEntity;
import kr.service.okr.project.persistence.repository.project.keyresult.KeyResultJpaRepository;
import kr.service.okr.project.repository.KeyResultCommand;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeyResultCommandImpl implements KeyResultCommand {

	private final KeyResultJpaRepository keyResultJpaRepository;

	@Override
	public KeyResult save(final KeyResult keyResult) {
		return keyResultJpaRepository.save(KeyResultJpaEntity.register(keyResult)).toDomain();
	}

}
