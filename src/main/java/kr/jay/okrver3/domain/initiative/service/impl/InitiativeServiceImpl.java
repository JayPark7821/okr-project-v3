package kr.jay.okrver3.domain.initiative.service.impl;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.project.service.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiativeServiceImpl {
	
	private final ProjectRepository projectRepository;
}
