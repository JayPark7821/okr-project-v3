package kr.service.okr.common.security.core.context;

import java.io.Serializable;

import kr.service.okr.AuthenticationInfo;

public interface AuthenticationContext extends Serializable {

	AuthenticationInfo getAuthenticationInfo();

	void setAuthenticationInfo(AuthenticationInfo authenticationInfo);
}
