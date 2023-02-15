package kr.jay.okrver3.application.team;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.team.TeamMemberInviteRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberFacade {

	public String inviteTeamMember(TeamMemberInviteRequestDto requestDto, User user) {
		throw new UnsupportedOperationException("kr.jay.okrver3.application.team.TeamMemberFacade.inviteTeamMember()");
	}
}
