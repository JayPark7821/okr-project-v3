# FLAG Project

## 1. 프로젝트 설계 & 구조

* ATDD 인수테스트를 통한 기능 구현
* 리팩터링 내성을 향상시키기 위해 기능이 요청에 대해 기대하는 대로 응답을 하는지 검증  
  <br />

* 도메인 주도 설계(by 에릭 에반스)에서 소개된 계층형 구조 참조 설계
* 계층 간의 참조 관계는 단방향을 유지, 계층 간 호출에서는 인터페이스를 통해 호출 되도록 함.

### 1-1 프로젝트 구조

![image](https://user-images.githubusercontent.com/60100532/219010787-00064210-d093-49ab-ba80-2f726f33d15a.png)

### 1-2 계층형 구조

* `사용자 인터페이스 계층`, `응용 계층`, `도메인 계층`, `인프라스트럭처 계층`으로 구성.
* 1-2-1 사용자 인터페이스 계층 (interface)
    * Controller에 해당하는 계층 사용자에게 입력을 받고 결과를 return 함.    
      <br />
* 1-2-2 응용 계층 (application)
    * 디자인 패턴의 Facade를 사용함 (GoF 디자인 패턴)
    * 각 도메인 service간 참조 관계를 가지지 않도록 하기 위해 모든 의존성을 가져옴   
      -> 응용 계층을 사용하는 클라이언트 사용자 인터페이스 계층에서는 다른 의존성이 필요 없이 응용 계층만 의존하면 됨.
    * 세부적인 작업은 도메인 계층에 위임, aggregation 담당함.
    * 사용자 인터페이스 계층에서 도메인의 service를 직접 호출하지 못하도록   
      접근 제한하고 싶은데 package 구조를 어떻게 가져갈지는 고민임....    
      <br />
* 1-2-3 도메인 계층 (domain)
    * 변수명, 메소드명을 활용하여 코드를 보고 어떤 역할을 하는지 알 수 있도록 시도.
    * 상태 저장과 영속과 관련된 세부적인 기술이나, 업무 로직을 구현하기 위해 별도로 필요한 기술들은 인프라스트럭처 계층에 위임    
      <br />
* 1-2-4 인프라스트럭처 계층 (infrastructure)
    * 도메인 계층의 로직 구현에 필요한 세부적인 기술 & 기능 구현체 제공    
      <br />

## 2. 프로젝트 구현

### 2-1 외부 공개용 별도 키

* 시스템 내부에서는 Entity의 식별자로 Long타입의 id를 사용하고(AUTO_INCREMENT),   
  외부 공개용으로 별도의 키(UUID 기반 토큰)를 생성해 사용하도록 구현    
  <br />

* Entity의 식별자를 외부에 오픈하지 않는것이 좋다고 판단한 이유.

    1. 보안상 이유
        * 프로젝트 단건 조회 url이 만약에 "/api/v1/project/{id}"라면,   
          사용자가 임의의 숫자를 넣어 현재 서비스에 등록된 프로젝트가 몇 건인지 알 수 있을 것이다.
        * 인증 과정이 없는 Api의 경우 주소에 AUTO_INCREMENT 식별자 값이 들어간다면
          얼마든지 사용자가 임의의 숫자를 넣어 조회해 볼 수 있게 된다.    
          <br />

    2. 식별자 값이 변경되는 경우
        * 식별자 값이 변경되면, 기존에 사용하던 주소가 더 이상 유효하지 않게 된다.   
          만약 외부 서비스와 연동되어 있다면 많은 공수가 발생할 것이다.
          <br />

* 성능
    * MySql 기준으로 약 1천만 건 이상 넘어가기 전까지는 UUID를 Key로 사용해도 성능에 큰 문제가 없다.
      ![image](https://user-images.githubusercontent.com/60100532/219006803-4112cbf4-0800-4005-ab1e-237ff07322e5.png)

      https://www.percona.com/blog/store-uuid-optimized-way/

### 2-2 bulk insert (JDBC batchUpdate)

* 프로젝트에 새로운 팀원을 초대하면 기존에 등록된 모든 팀원에게 새로운 팀원이 합류하였다는 알림을 보내는 기능이 있다.
* 그런대 이때 Jpa를 통해 저장하게 된다면 결국 한 건씩 팀원 수만큼 알림 테이블에 데이터를 넣게 된다. (Jpa의 saveAll도 결국 안에서는 save 호출)
  ![image](https://user-images.githubusercontent.com/60100532/219232605-ed44a114-ba75-4226-8052-4d320447b8cb.png)

* 이를 조금이나마 개선하기 위해 bulk insert를 고려해 볼 수 있는데  
  아쉽게도 Mysql에 id 채번을 auto_increment 전략을 사용하고 Jpa를 사용한다면 bulk insert를 사용할 수 없다.  
  https://stackoverflow.com/questions/27697810/why-does-hibernate-disable-insert-batching-when-using-an-identity-identifier-gen
* id 전략을 Sequence나 Table 전략을 사용하면 bulk insert를 사용할 수 있지만 Mysql에서는 Sequence 전략은 사용할 수 없고 table 전략은 키를 위한 테이블을 추가로 만든다는 것이
  부담스럽다.
* 그래서 결국 JDBC batchUpdate를 사용하여 구연하였다. (spring data jpa도 결국 jdbc를 사용하기 때문에 가능 별도 의존성 추가 필요 없음)

### 2-3 소셜 IdToken 인증 - TokenVerifyProcessor

(https://app.diagrams.net/#G1gH04HWyWNztDXyJcMkPEIfy_aaFNWKRc)
![image](https://user-images.githubusercontent.com/60100532/218805078-5d4cfae9-8004-47f2-8c08-257f44ba4620.png)

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

## 3. 프로젝트 개선 방안.

### 개선 방안 1

* 현재 API 요청 이 들어올 때마다 JwtTokenFilter에서 jwt를 검증하기 위해 DB에서 사용자 정보를 조회한다.
* 사용자 정보는 자주 변경되는 데이터가 아니다
* 이를 개선하기 위해, 사용자 정보 캐싱 해 BD 부하를 줄이는 시도를 해볼 수 있을 것 같다.

### 개선 방안 2

* 현재 팀원을 초대하면 팀원과, 알림을 모두 저장해야만 API 호출 이 끝난다.
* 팀원 저장에 대한 요청을 할 때 유저는 팀원이 저장되길 기대한다, 알림은 부가적인 기능임.
* 사용자가 팀원 초대에 대한 API 호출했을때 팀원 저장이 완료되면 바로 팀원 초대 완료를 리턴하고     
  알림 저장은 message Queue에 publish해 담아 놓고 비동기적으로 처리하여 응답 속도를 개선할 수 있을 것 같다.

 