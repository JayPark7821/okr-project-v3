package kr.service.user.domain;

import java.time.LocalDateTime;

import kr.service.model.guset.ProviderType;
import kr.service.model.user.JobField;
import kr.service.model.user.RoleType;

public record User(

	Long userSeq,
	String userId,
	String username,
	String email,
	String profileImage,
	ProviderType providerType,
	RoleType roleType,
	String password,
	JobField jobField,
	String createdBy,
	String lastModifiedBy,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate

) {
}
