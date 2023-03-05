package kr.jay.okrver3.domain.project.service;

import kr.jay.okrver3.domain.user.service.UserInfo;

public record ProjectInitiativeInfo(String initiativeToken, String initiativeName, boolean done, UserInfo user){
}
