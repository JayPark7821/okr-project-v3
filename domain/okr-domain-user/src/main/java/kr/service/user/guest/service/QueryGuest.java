package kr.service.user.guest.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.user.guest.usecase.QueryGuestUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueryGuest implements QueryGuestUseCase {

}
