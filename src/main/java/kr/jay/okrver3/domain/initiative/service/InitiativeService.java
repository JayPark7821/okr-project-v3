package kr.jay.okrver3.domain.initiative.service;

import kr.jay.okrver3.application.initiative.InitiativeSaveCommand;
import kr.jay.okrver3.domain.user.User;

public interface InitiativeService {
	String registerInitiative(InitiativeSaveCommand command, long keyResultId, User user);
}
