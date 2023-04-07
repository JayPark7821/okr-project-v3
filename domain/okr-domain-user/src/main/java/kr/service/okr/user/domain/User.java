package kr.service.okr.user.domain;

import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.JobField;
import kr.service.okr.model.user.RoleType;

public record User(

	Long userSeq,
	String userId,
	String username,
	String email,
	String profileImage,
	ProviderType providerType,
	RoleType roleType,
	String password,
	JobField jobField

) {
}
