package kr.jay.okrver3.domain.user.info;

import kr.jay.okrver3.domain.user.JobField;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.User;

public record UserInfo(Long userSeq, String id, String name, String email, String profileImageUrl, ProviderType providerType, JobField jobField){

	public UserInfo(User user) {
		this(user.getUserSeq(), user.getUserId(), user.getUsername(), user.getEmail(), user.getProfileImage(), user.getProviderType(), user.getJobField());
	}
}
