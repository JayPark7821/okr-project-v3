package kr.service.okr.common.security.core.context;

import java.io.Serializable;

public interface AuthenticationContext extends Serializable {

	AuthenticationInfo getAuthenticationInfo();

	void setAuthenticationInfo(AuthenticationInfo authenticationInfo);
}
