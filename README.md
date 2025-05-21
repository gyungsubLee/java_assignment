# [ 과제 ]  Spring Boot 기반 JWT 인증/인가 및 AWS 배포

> - 바로 인턴 12기 -  백엔드 개발 과제(java)<br>
> - 기간: 25.05.12(수)  ~ 25.05.15(목)

### 🔧 사용 기술 & 버전

![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen?logo=springboot)
![Gradle](https://img.shields.io/badge/Gradle-8.7-blue?logo=gradle)
![Swagger](https://img.shields.io/badge/Swagger-2.2.0-yellow?logo=swagger)
![JWT](https://img.shields.io/badge/JWT-0.11.5-orange)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.2.2-green?logo=springsecurity)
![JUnit](https://img.shields.io/badge/JUnit-5-red?logo=junit5)

---


### 📑 API 명세서 및 베포 ( 중단 ) 

- #### API 문서: http://15.165.3.241/api-test

- #### EC2 엔드포인트:  http://15.165.3.241

---

### 📌 요구사항

- Spring Boot를 이용한 JWT 인증/인가 로직과 API구현
- Junit 기반 테스트 코드 작성
- Swagger 를 통한 API 문서화
-  AWS EC2 베포

---


### 💻 개발

#### 1. 기능 개발 - 사용자 인증/인가

- 일반 사용자(Member) 및 관리자(Admin) 회원가입, 로그인 API 개발
- JWT를 이용하여 Access Token 발급 및 검증
-  요구사항에 맞게 성공 및 실패 케이스 응답 포맷 설정
- 일반 사용자(Member) 및 관리자(Admin) n) 역할(Role)을 구분하여 특정 API 접근을 제한
    - 예) 관리자 권한 부여 API

#### 2. 테스트
- JwtProvider 단위 테스트
- 서비스 계층을 Mock 객체로 테스트
- 관리자 권한에 따른 API 테스트

#### 3. API 명세서
- Swagger를 사용한 API 문서화

#### 4. 배포
-  AWS EC2 인스턴스 생성 및 기본 환경 구축
-  CI/CD - github actions를 통한 자동 베포 


<br/>
