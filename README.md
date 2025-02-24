# S1JIN DILIVERY

## 프로젝트 소개
     💡 주문 관리 플랫폼 백엔드 프로젝트

### 1-1) 프로젝트 목적
- 사용자가 다양한 주문을 쉽고 효율적으로 관리할 수 있는 시스템 구축
- 빠르고 정확한 주문 처리를 통해 음식점의 운영의 효율성을 높이고 고객에게 원활한 서비스 제공
- 고객과 판매자 간 원활한 주문 및 결제 프로세스를 지원하는 백엔스 서비스 구축

### 1-2) 주요 기능

<img width="550" alt="image" src="https://github.com/user-attachments/assets/e8079dbd-62cf-44cb-b7f5-3694ce9de6c9" />


<br><br>

## 개발 환경 소개

|분류|상세|
|:--|:--|
|IDE|IntelliJ|
|Language|Java 17|
|Framework|Spring Boot 3.4.2|
|Repository|PostgreSQL 16.3|
|Build Tool|Gradle 8.12.1|
|DevOps - dev | EC2, RDS(PostreSQL), S3, Docker, Gihub Actions|


<br><br>

## 프로젝트 실행 방법

### 1. git clone 하기
```shell
git clone https://github.com/4S1JIN/s1jin-delivery.git
```

### 2. 로컬 DB 세팅을 위해 docker-compose up하기
- docker를 미리 설치해야한다.
- 프로젝트의 루트 폴더에 존재하는 `docker-compose.yml`를 들어가서 service 를 up 해준다.
- (참고) 현재는 postgresql만 존재하고 포트는 `5432:5432` 이다.

### 3. 애플리케이션 정상 실행 확인
- 스프링 애플리케이션 (`DeliveryApplication`)이 정상 실행되는지 확인한다.

<br><br>

## 설계 산출물
- [API 명세서](https://github.com/4S1JIN/s1jin-delivery/wiki/API-%EB%AA%85%EC%84%B8%EC%84%9C)
- [테이블 설계서](https://github.com/4S1JIN/s1jin-delivery/wiki/%ED%85%8C%EC%9D%B4%EB%B8%94-%EB%AA%85%EC%84%B8%EC%84%9C)
- [ERD](https://github.com/4S1JIN/s1jin-delivery/wiki/ERD)
- [인프라 설계서](https://github.com/4S1JIN/s1jin-delivery/wiki/%EC%9D%B8%ED%94%84%EB%9D%BC-%EC%84%A4%EA%B3%84%EC%84%9C)
- [Convention](https://github.com/4S1JIN/s1jin-delivery/wiki/Git-Convention)
- [GitHub Project URL](https://github.com/4S1JIN/s1jin-delivery)

<br><br>

## 개발 산출물

- [트러블 슈팅](https://github.com/4S1JIN/s1jin-delivery/wiki/%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85)
- 공통 관심 사항
  - 권한 별 기능 분기 처리
- 테스트 코드
  - 전체 작성된 테스트 코드 수 : 
  - [계층별 테스트 코드 작성 전략](https://github.com/4S1JIN/s1jin-delivery/wiki/%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C-%EC%9E%91%EC%84%B1-%EC%A0%84%EB%9E%B5)
- [API 문서화](http://13.211.250.65/api/api-docs)

<br>

<details>
  <summary>설계 대비 구현률</summary>

  - 설계 API 수 : <br>
  - 구현 API 수 : 50 <br>
  - 설계 대비 구현률 : <br>
  
  ```java
  API Count Summary
==========================
  - OrderOwnerController.java
    ├── GET APIs:        2
    ├── POST APIs:        3
  - OrderCustomerController.java
    ├── GET APIs:        2
    ├── POST APIs:        2
    └── DELETE APIs:        1
  - PaymentHtmlController.java
    ├── GET APIs:        3
  - PaymentController.java
    ├── GET APIs:        4
    ├── POST APIs:        1
    ├── PUT APIs:        1
    └── DELETE APIs:        1
  - ShopController.java
    ├── GET APIs:        3
    ├── POST APIs:        1
    ├── PUT APIs:        1
    └── DELETE APIs:        1
  - AddressController.java
    ├── GET APIs:        1
    ├── POST APIs:        1
    ├── PUT APIs:        1
    └── DELETE APIs:        1
  - MemberController.java
    ├── GET APIs:        2
    ├── PUT APIs:        1
    └── DELETE APIs:        1
  - GeminiController.java
    ├── POST APIs:        1
  - ReviewController.java
    ├── GET APIs:        1
    ├── POST APIs:        1
    ├── PUT APIs:        1
    └── DELETE APIs:        1
  - ShopReviewController.java
    ├── GET APIs:        1
  - MenuController.java
    ├── GET APIs:        3
    ├── POST APIs:        1
    ├── PUT APIs:        1
    └── DELETE APIs:        1
  - AuthController.java
    ├── POST APIs:        4
==========================
API Statistics
==========================
- GET APIs: 22
- POST APIs: 15
- PUT APIs: 6
- DELETE APIs: 7
- Total APIs: 50
==========================
```

</details>

<br><br>

## 시스템을 발전 시키기 위해 더 해본다면?
### 1) S3 이미지 업로드 속도 개선
- 문제점 : 이미지 업로드 시 느린 속도와 성능 저하
- 개선 계획
  - CDN 도입
  - Nginx, MinIO등 별도의 파일 서버 구축

### 2) Redis를 활용한 Refesh Token 관리
- 문제점 : 현재는 Refrest Token을 서버에 저장하고 있지 않아 발행 후 관리에 문제가 생길 수 있음
- 개선 계획
  - TTL(자동 만료), In-Memory DB의 강점(보안, 속도), 분산 서버 환경에서도 공유된 Redis를 사용한 토큰 관리 등의 장점이 있는 Redis를 활용해 Refresh Token을 관리 

### 3) 통합테스트
- 문제점 : 단위 테스트는 충분하나 시스템 전체 흐름에 대한 테스트 부족
- 개선 계획
  - Spring Boot Test, TestContainers, Postman 등을 활용하여 시스템 간 통합 테스트를 자동화하고 CI/CD 파이프라인에 통합

### 4) MSA 전환
- 문제점 : 모놀리틱 아키텍쳐로 구현하였으나 추후 유지보수와 확장성에 어려움이 발생할 수 있음
- 개선 계획
  - MSA로 전환하여 서비스의 독립성을 확보하고 각 서비스의 확장성과 배포 유연성을 향상
  - Spring Cloud, Docker, Kubernetes 등을 활용하여 서비스 분리 및 관리, 배포 자동화
<br><br>

## 협업 시 우리조가 잘한 것들
- Docker Compose를 사용해 로컬 DB 구축
- Runtime Exception 기반의 공통 예외 처리 정책
- 페이지 크기 값 검증을 Argument Resolver로 처리
- 도메인별 Fixture 클래스 생성
- soft-delete 처리를 위한 삭제 정책
- toss payment와 연동

<br><br>

## 협업 시 아쉽거나 부족했던 부분
- 코드 스타일 불일치  / 커밋 메세지 불명확 ⇒ **컨벤션**을 작성하고 준수하기로 함
- 각자가 별도로 API 테스트 진행함 ⇒ **Postman 팀 워크스페이스** 생성

<br><br>

## 팀원 소개

| <img src="https://img.shields.io/badge/Leader-%2310069F%20" /> | <img src="https://img.shields.io/badge/Sub_Leader-%2300264B" /> |   |   |   |
| :--------------------------------------------------------------: | :--------------------------------------------------------------: | :--------------------------------------------------------------------------: | :-----------------------------------------------------------: |:-----------------------------------------------------------: |
|      <img src="https://avatars.githubusercontent.com/u/137313172?v=4" width="120px;" alt=""/>      |      <img src="https://avatars.githubusercontent.com/u/68311264?v=4" width="120px;" alt=""/>      |            <img src="https://avatars.githubusercontent.com/u/104823900?v=4" width="120px;" alt=""/>            |    <img src="https://avatars.githubusercontent.com/u/109949465?v=4" width="120px;" alt=""/>     |    <img src="https://avatars.githubusercontent.com/u/85011923?v=4" width="120px;" alt=""/>     |
|           [박소해](https://github.com/S2gamzaS2)           |           [최진영](https://github.com/cchoijjinyoung)           |                 [김소민](https://github.com/ss0ming)                 |         [임승택](https://github.com/lime1st)          |         [김승수](https://github.com/kss123456789)          |
|                            BE / 리뷰 / 회원                           |                            BE / 주문                          |                                  BE / 상점 / 메뉴                                 |                         BE / 인증·인가                          |                          BE / 결제                          |


