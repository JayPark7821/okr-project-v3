package kr.jay.okrver3.domain.user.service;

import kr.jay.okrver3.domain.user.JobFieldDetail;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.User;

public record UserInfo(Long userSeq, String id, String name, String email, String profileImageUrl, ProviderType providerType, JobFieldDetail jobFieldDetail){

	public UserInfo(User user) {
		this(user.getUserSeq(), user.getUserId(), user.getUsername(), user.getEmail(), user.getProfileImage(), user.getProviderType(), user.getJobField());
	}
}
