package kr.jay.okrver3.domain.token.service.impl;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	@Override
	public AuthTokenInfo generateTokenSet(String userEmail) {
		return null;
	}
}
