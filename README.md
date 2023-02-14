# FLAG Project

## 1. 프로젝트 구조

* ATDD 인수테스트를 통한 기능 구현
* 도메인 주도 설계(by 에릭 에반스)에서 소개된 계층형 구조 참조 하여  
  `사용자 인터페이스 계층`, `응용 계층`, `도메인 계층`, `인프라스트럭처 계층`으로 구성함.
* 계층 간의 참조 관계는 단방향을 유지, 계층 간 호출에서는 인터페이스를 통해 호출 되도록 함.

### 1-1 각 계층의 역할

* 1-1-1 사용자 인터페이스 계층 (interface)
    * Controller에 해당하는 계층 사용자에게 입력을 받고 결과를 return 함.    
      <br />
* 1-1-2 응용 계층 (application)
    * xxxxFacade에 해당함
    * 각 도메인 service간 참조 관계를 가지지 않도록 하기 위해 모든 의존성을 가져옴   
      -> 응용 계층을 사용하는 클라이언트 사용자 인터페이스 계층에서는 다른 의존성이 필요 없이 응용 계층만 의존하면 됨.
    * 세부적인 작업은 도메인 계층에 위임, 오케스트레이션만 담당함.
    * Facade 패턴으로 구현한 했으니 사용자 인터페이스 계층에서 도메인의 service를 직접 호출하지 못하도록   
      접근 제한하고 싶은데 package 구조를 어떻게 가져갈지는 고민임....    
      <br />
* 1-1-3 도메인 계층 (domain)
    * 변수명, 메소드명을 활용하여 코드를 보고 어떤 역할을 하는지 알 수 있도록 시도.
    * 상태 저장과 영속과 관련된 세부적인 기술이나, 업무 로직을 구현하기 위해 별도로 필요한 기술들은 인프라스트럭처 계층에 위임    
      <br />
* 1-1-4 인프라스트럭처 계층 (infrastucture)
    * 도메인 계층의 로직 구현에 필요한 세부적인 기술 & 기능 구현체 제공    
      <br />

## 2. 프로젝트 구현

* 소셜 IdToken 인증 - TokenVerifyProcessor  
  (https://app.diagrams.net/#G1gH04HWyWNztDXyJcMkPEIfy_aaFNWKRc)
  ![image](https://user-images.githubusercontent.com/60100532/218805078-5d4cfae9-8004-47f2-8c08-257f44ba4620.png)

* OCP원칙을 고려하여 구현
* TokenVerifier의 구현체들을 리스트로 주입받아 사용
* 추후 다른 소셜 로그인을 추가 시,   
  기존 코드를 수정하지 않고 내부적으로 어떻게 idToken을 검증하는지와 관계없이  
  TokenVerifier 인터페이스를 구현하기만 하면 된다.

```java

@Component
@RequiredArgsConstructor
public class TokenVerifyProcessorImpl implements TokenVerifyProcessor {

	private final List<TokenVerifier> tokenVerifierList;

	@Override
	public OAuth2UserInfo verifyIdToken(ProviderType provider, String token) {
		TokenVerifier tokenVerifier = routingVerifierCaller(provider);
		return tokenVerifier.verifyIdToken(token);
	}

	private TokenVerifier routingVerifierCaller(ProviderType provider) {
		return tokenVerifierList.stream()
			.filter(tokenVerifier -> tokenVerifier.support(provider))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unsupported provider type"));
	}
}

```
