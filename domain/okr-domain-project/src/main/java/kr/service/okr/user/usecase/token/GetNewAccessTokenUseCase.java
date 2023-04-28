package kr.service.okr.user.usecase.token;

public interface GetNewAccessTokenUseCase {

	TokenInfo command(String email);

}
