package kr.service.okr.project.repository;

import kr.service.okr.project.domain.Initiative;

public interface InitiativeCommand {

	Initiative save(Initiative initiative);
}
