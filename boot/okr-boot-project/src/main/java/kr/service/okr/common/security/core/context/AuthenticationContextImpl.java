package kr.service.okr.common.security.core.context;

import kr.service.okr.AuthenticationInfo;

public class AuthenticationContextImpl implements AuthenticationContext {

	private AuthenticationInfo authenticationInfo;

	public AuthenticationContextImpl() {
	}

	public AuthenticationContextImpl(final AuthenticationInfo authenticationInfo) {
		this.authenticationInfo = authenticationInfo;
	}

	@Override
	public AuthenticationInfo getAuthenticationInfo() {
		return this.authenticationInfo;
	}

	@Override
	public void setAuthenticationInfo(final AuthenticationInfo authenticationInfo) {
		this.authenticationInfo = authenticationInfo;
	}
}
