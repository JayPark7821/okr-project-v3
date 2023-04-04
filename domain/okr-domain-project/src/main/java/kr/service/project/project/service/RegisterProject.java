package kr.service.project.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.persistence.entity.project.ProjectJpaEntity;
import kr.service.persistence.service.project.ProjectCommand;
import kr.service.project.project.domain.Project;
import kr.service.project.project.usecase.RegisterProjectUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterProject implements RegisterProjectUseCase {

	private final ProjectCommand projectCommand;

	@Override
	public Project registerProject(Command command) {
		Project project = projectCommand.save(command);
		project.addLeader(userSeq);
		teamMemberUserSeqs.forEach(project::addTeamMember);
		return new ProjectInfo(project);
	}

	private ProjectJpaEntity toEntity(Command command) {
		return ProjectJpaEntity.builder()
			.name(command.getName())
			.description(command.getDescription())
			.build();
	}

}
