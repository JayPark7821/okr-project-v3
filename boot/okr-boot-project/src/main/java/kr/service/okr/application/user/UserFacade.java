package kr.service.okr.application.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.okr.user.api.JoinRequest;
import kr.service.okr.user.enums.JobCategory;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.usecase.guest.RegisterGuestUseCase;
import kr.service.okr.user.usecase.token.GetNewAccessTokenUseCase;
import kr.service.okr.user.usecase.token.TokenInfo;
import kr.service.okr.user.usecase.user.JobInfo;
import kr.service.okr.user.usecase.user.LoginInfo;
import kr.service.okr.user.usecase.user.ProcessLoginUseCase;
import kr.service.okr.user.usecase.user.QueryJobCategoryUseCase;
import kr.service.okr.user.usecase.user.QueryJobFieldsUseCase;
import kr.service.okr.user.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFacade {

	private final ProcessLoginUseCase processLoginUseCase;
	private final RegisterUserUseCase registerUserUseCase;
	private final RegisterGuestUseCase registerGuestUseCase;
	private final QueryJobCategoryUseCase queryJobCategoryUseCase;
	private final QueryJobFieldsUseCase queryJobFieldsUseCase;
	private final GetNewAccessTokenUseCase getNewAccessTokenUseCase;

	public Optional<LoginInfo> getLoginInfoFrom(final OAuth2UserInfo info) {
		return processLoginUseCase.command(new ProcessLoginUseCase.Command(info.email(), info.socialPlatform()));
	}

	public LoginInfo registerGuest(final OAuth2UserInfo info) {
		return registerGuestUseCase.command(toCommand(info));
	}

	public LoginInfo registerUser(final JoinRequest joinRequest) {
		return registerUserUseCase.command(toCommand(joinRequest));
	}

	private RegisterUserUseCase.Command toCommand(final JoinRequest joinRequest) {
		return new RegisterUserUseCase.Command(
			joinRequest.guestUuid(),
			joinRequest.username(),
			joinRequest.email(),
			joinRequest.jobField()
		);
	}

	private RegisterGuestUseCase.Command toCommand(final OAuth2UserInfo info) {
		return new RegisterGuestUseCase.Command(
			info.id(),
			info.username(),
			info.email(),
			info.profileImage(),
			ProviderType.of(info.socialPlatform())
		);
	}

	public List<JobInfo> getJobCategory() {
		return queryJobCategoryUseCase.query();
	}

	public List<JobInfo> getJobField(final JobCategory jobCategory) {
		return queryJobFieldsUseCase.query(jobCategory);
	}

	public TokenInfo getNewAccessTokenFrom(final String email) {
		return getNewAccessTokenUseCase.command(email);
	}
}
