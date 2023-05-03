package kr.service.okr.project.repository;

import java.util.List;

import kr.service.okr.project.domain.TeamMember;

public interface TeamMemberCommand {

	List<TeamMember> saveAll(List<TeamMember> teamMember);
}
