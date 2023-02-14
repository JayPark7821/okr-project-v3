# FLAG Project

## 1. 프로젝트 설계

* ATDD 인수테스트를 통한 기능 구현
* 도메인 주도 설계(by 에릭 에반스)에서 소개된 계층형 구조 참조 하여  
  `사용자 인터페이스 계층`, `응용 계층`, `도메인 계층`, `인프라스트럭처 계층`으로 구성함.

### 1-1 각 계층의 역할

* 1-1-1 사용자 인터페이스 계층 (interface)
    * Controller에 해당하는 계층 사용자에게 입력을 받고 결과를 return함.    
      <br />
* 1-1-2 응용 계층 (application)
    * xxxxFacade에 해당함
    * 각 도메인 service간 참조 관계를 가지지 않도록 하기위해 모든 의존성을 가져옴   
      -> 응용계층을 사용하는 클라이언트 사용자 인터페이스 계층에서는 다른 의존성이 필요없이 응용 계층만 의존하면 됨.
    * 세부적인 작업은 도메인 계층에 위임, 오케스트레이션만 담당함.
    * Facade 패턴으로 구현한 했으니 사용자 인터페이스 계층에서 도메인의 service를 직접 호출하지 못하도록   
      접근 제한하고 싶은데 package 구조를 어떻게 가져갈지는 고민임....    
      <br />
* 1-1-3 도메인 계층 (domain)
    * 변수명, 메소드명을 활용하여 코드를 보고 어떤 역할을 하는지 알 수 있도록 시도.
    * 상태 저장과 영속과 관련된 세부적인 기술이나, 업무 로직을 구현하기 위해 별도로 필요한 기술들은 인프라스트럭처 계층에 위임    
      <br />
* 1-1-4 인프라스트럭처 계층 (infrastucture)
    * 도메인 계층의 도직 구현에 필요한 세부적인 기술 & 기능 구현체 제공    
      <br />  


