package kr.jay.okrver3.domain.project;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.info.FeedbackInfo;
import kr.jay.okrver3.domain.project.info.InitiativeInfo;
import kr.jay.okrver3.domain.project.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.info.ProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.info.ProjectTeamMembersInfo;

public interface ProjectService {
	ProjectInfo registerProject(ProjectSaveCommand command, Long userSeq, List<Long> teamMemberUserSeqs);

	ProjectInfo getProjectInfoBy(String projectToken, Long userSeq);

	ProjectTeamMembersInfo inviteTeamMember(String projectToken, Long invitedUserSeq, Long inviterSeq);

	void validateUserToInvite(String projectToken, Long invitedUserSeq, Long userSeq);

	Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command, Long userSeq);

	ProjectSideMenuInfo getProjectSideMenuDetails(String projectToken, Long userSeq);

	String registerKeyResult(ProjectKeyResultSaveCommand command, Long userSeq);

	String registerInitiative(ProjectInitiativeSaveCommand command, Long userSeq);

	String initiativeFinished(String initiativeToken, Long userSeq);

	Page<InitiativeInfo> getInitiativeByKeyResultToken(String keyResultToken, Long userSeq, Pageable pageable);

	FeedbackInfo registerFeedback(FeedbackSaveCommand command, Long requesterSeq);
}

