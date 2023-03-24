package kr.service.okr.domain.project;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.service.okr.domain.project.aggregate.feedback.SearchRange;
import kr.service.okr.domain.project.command.FeedbackSaveCommand;
import kr.service.okr.domain.project.command.ProjectDetailRetrieveCommand;
import kr.service.okr.domain.project.command.ProjectInitiativeSaveCommand;
import kr.service.okr.domain.project.command.ProjectKeyResultSaveCommand;
import kr.service.okr.domain.project.command.ProjectSaveCommand;
import kr.service.okr.domain.project.info.FeedbackDetailInfo;
import kr.service.okr.domain.project.info.FeedbackInfo;
import kr.service.okr.domain.project.info.IniFeedbackInfo;
import kr.service.okr.domain.project.info.InitiativeDetailInfo;
import kr.service.okr.domain.project.info.InitiativeDoneInfo;
import kr.service.okr.domain.project.info.InitiativeForCalendarInfo;
import kr.service.okr.domain.project.info.InitiativeInfo;
import kr.service.okr.domain.project.info.InitiativeSavedInfo;
import kr.service.okr.domain.project.info.ParticipateProjectInfo;
import kr.service.okr.domain.project.info.ProjectDetailInfo;
import kr.service.okr.domain.project.info.ProjectInfo;
import kr.service.okr.domain.project.info.ProjectSideMenuInfo;
import kr.service.okr.domain.project.info.ProjectTeamMembersInfo;

public interface ProjectService {
	ProjectInfo registerProject(ProjectSaveCommand command, Long userSeq, List<Long> teamMemberUserSeqs);

	ProjectInfo getProjectInfoBy(String projectToken, Long userSeq);

	ProjectTeamMembersInfo inviteTeamMember(String projectToken, Long invitedUserSeq, Long inviterSeq);

	void validateUserToInvite(String projectToken, Long invitedUserSeq, Long userSeq);

	Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command, Long userSeq);

	ProjectSideMenuInfo getProjectSideMenuDetails(String projectToken, Long userSeq);

	String registerKeyResult(ProjectKeyResultSaveCommand command, Long userSeq);

	InitiativeSavedInfo registerInitiative(ProjectInitiativeSaveCommand command, Long userSeq);

	InitiativeDoneInfo initiativeFinished(String initiativeToken, Long userSeq);

	Page<InitiativeInfo> getInitiativeByKeyResultToken(String keyResultToken, Long userSeq, Pageable pageable);

	FeedbackInfo registerFeedback(FeedbackSaveCommand command, Long requesterSeq);

	InitiativeDetailInfo getInitiativeBy(String initiativeToken, Long userSeq);

	List<InitiativeForCalendarInfo> getInitiativeByDate(LocalDate searchDate, Long userSeq);

	List<String> getInitiativeDatesBy(YearMonth yearMonth, Long userSeq);

	IniFeedbackInfo getInitiativeFeedbacksBy(String initiativeToken, Long userSeq);

	Integer getCountOfInitiativeToGiveFeedback(Long userSeq);

	Page<FeedbackDetailInfo> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable);

	List<ParticipateProjectInfo> getParticipateProjects(Long userSeq);

	void promoteNextProjectLeaderOrDeleteProject(Long userSeq);
}

