package kr.service.okr.interfaces.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import kr.service.okr.domain.project.ProjectType;
import kr.service.okr.domain.project.SortType;
import kr.service.okr.domain.project.command.FeedbackSaveCommand;
import kr.service.okr.domain.project.command.ProjectDetailRetrieveCommand;
import kr.service.okr.domain.project.command.ProjectInitiativeSaveCommand;
import kr.service.okr.domain.project.command.ProjectKeyResultSaveCommand;
import kr.service.okr.domain.project.command.ProjectSaveCommand;
import kr.service.okr.domain.project.command.TeamMemberInviteCommand;
import kr.service.okr.domain.project.info.FeedbackDetailInfo;
import kr.service.okr.domain.project.info.IniFeedbackInfo;
import kr.service.okr.domain.project.info.InitiativeDetailInfo;
import kr.service.okr.domain.project.info.InitiativeForCalendarInfo;
import kr.service.okr.domain.project.info.InitiativeInfo;
import kr.service.okr.domain.project.info.KeyResultInfo;
import kr.service.okr.domain.project.info.ParticipateProjectInfo;
import kr.service.okr.domain.project.info.ProjectDetailInfo;
import kr.service.okr.domain.project.info.ProjectInfo;
import kr.service.okr.domain.project.info.ProjectSideMenuInfo;
import kr.service.okr.domain.project.info.TeamMemberUserInfo;
import kr.service.okr.interfaces.project.request.FeedbackSaveRequest;
import kr.service.okr.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.service.okr.interfaces.project.request.ProjectKeyResultSaveRequest;
import kr.service.okr.interfaces.project.request.ProjectSaveRequest;
import kr.service.okr.interfaces.project.request.TeamMemberInviteRequest;
import kr.service.okr.interfaces.project.response.FeedbackDetailResponse;
import kr.service.okr.interfaces.project.response.IniFeedbackResponse;
import kr.service.okr.interfaces.project.response.InitiativeDetailResponse;
import kr.service.okr.interfaces.project.response.InitiativeForCalendarResponse;
import kr.service.okr.interfaces.project.response.KeyResultResponse;
import kr.service.okr.interfaces.project.response.ParticipateProjectResponse;
import kr.service.okr.interfaces.project.response.ProjectDetailResponse;
import kr.service.okr.interfaces.project.response.ProjectInfoResponse;
import kr.service.okr.interfaces.project.response.ProjectInitiativeResponse;
import kr.service.okr.interfaces.project.response.ProjectSideMenuResponse;
import kr.service.okr.interfaces.project.response.ProjectTeamMemberResponse;

@Component
public class ProjectDtoMapper {

	ProjectSaveCommand of(ProjectSaveRequest dto) {
		return new ProjectSaveCommand(
			dto.objective(),
			dto.sdt(),
			dto.edt(),
			dto.teamMembers()
		);
	}

	ProjectInfoResponse of(ProjectInfo info) {
		return new ProjectInfoResponse(
			info.projectToken(),
			info.objective(),
			info.startDate(),
			info.endDate(),
			info.projectType(),
			info.keyResultInfos().stream().map(this::of).toList(),
			info.teamMembersCount()
		);
	}

	KeyResultResponse of(KeyResultInfo info) {
		return new KeyResultResponse(info.name(), info.keyResultToken(), info.keyResultIndex());
	}

	ProjectDetailRetrieveCommand of(SortType sortType, ProjectType projectType, String includeFinishedProjectYN,
		Pageable pageable) {
		return new ProjectDetailRetrieveCommand(sortType, projectType, includeFinishedProjectYN, pageable);
	}

	ProjectDetailResponse of(ProjectDetailInfo info) {
		return new ProjectDetailResponse(
			info.objective(),
			info.projectToken(),
			info.newProject(),
			info.progress(),
			info.sdt(),
			info.edt(),
			info.teamMemberCount(),
			info.projectType());
	}

	TeamMemberInviteCommand of(TeamMemberInviteRequest dto) {
		return new TeamMemberInviteCommand(dto.projectToken(), dto.email());
	}

	ProjectSideMenuResponse of(ProjectSideMenuInfo info) {
		return new ProjectSideMenuResponse(
			info.progress(),
			info.teamMembers().stream().map(this::of).toList()
		);
	}

	ProjectTeamMemberResponse of(TeamMemberUserInfo info) {
		return new ProjectTeamMemberResponse(
			info.userEmail(),
			info.userName(),
			info.profileImage(),
			info.jobField()
		);
	}

	ProjectKeyResultSaveCommand of(ProjectKeyResultSaveRequest dto) {
		return new ProjectKeyResultSaveCommand(
			dto.projectToken(),
			dto.keyResultName()
		);
	}

	ProjectInitiativeSaveCommand of(ProjectInitiativeSaveRequest dto) {
		return new ProjectInitiativeSaveCommand(
			dto.keyResultToken(),
			dto.name(),
			LocalDate.parse(dto.sdt(), DateTimeFormatter.ISO_DATE),
			LocalDate.parse(dto.edt(), DateTimeFormatter.ISO_DATE),
			dto.detail()
		);
	}

	ProjectInitiativeResponse of(InitiativeInfo info) {
		return new ProjectInitiativeResponse(
			info.initiativeToken(), info.initiativeName(), info.done(),
			this.of(info.user())
		);

	}

	FeedbackSaveCommand of(FeedbackSaveRequest dto) {
		return new FeedbackSaveCommand(
			dto.opinion(),
			dto.grade(),
			dto.initiativeToken()
		);
	}

	InitiativeDetailResponse of(InitiativeDetailInfo info) {
		return new InitiativeDetailResponse(
			info.done(),
			info.user(),
			info.startDate(),
			info.endDate(),
			info.initiativeToken(),
			info.initiativeName(),
			info.initiativeDetail(),
			info.myInitiative()
		);
	}

	InitiativeForCalendarResponse of(InitiativeForCalendarInfo info) {
		return new InitiativeForCalendarResponse(
			info.initiativeToken(),
			info.initiativeName(),
			info.startDate(),
			info.endDate()
		);
	}

	IniFeedbackResponse of(IniFeedbackInfo info) {
		return new IniFeedbackResponse(
			info.myInitiative(),
			info.wroteFeedback(),
			info.gradeCount(),
			info.feedback().stream().map(this::of).toList()
		);
	}

	FeedbackDetailResponse of(FeedbackDetailInfo info) {
		return new FeedbackDetailResponse(
			info.initiativeToken(),
			info.feedbackToken(),
			info.opinion(),
			info.grade(),
			info.writerName(),
			info.writerJob(),
			info.profileImage()
		);
	}

	ParticipateProjectResponse of(ParticipateProjectInfo info) {
		return new ParticipateProjectResponse(
			info.projectToken(),
			info.objective(),
			info.projectType(),
			info.roleType()
		);
	}
}

