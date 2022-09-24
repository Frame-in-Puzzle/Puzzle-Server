# Puzzle-Server

## Introduce

퍼즐은 공동의 목표를 수행하기 위한
팀원을 모집할 때 사용할 수 있는 서비스입니다

## Index

1. [Tech Stack](#tech-stack)
2. [ERD](#erd)
3. [API 명세서](#api-명세서)
4. [기능 명세서](#기능-명세서)
    1. [회원](#회원)
    2. [게시글](#게시글)
5. [프로젝트 UI](#프로젝트-ui)
    1. [프로모션](#프로모션-페이지)
    2. [로그인 모달](#로그인-모달)
    3. [프로필 조회](#프로필-조회)
    4. [프로필 수정](#프로필-수정)
    5. [메인화면](#메인화면)
    6. [게시글 조회 필터 모달](#메인화면---게시글-조회-필터-모달)
    7. [게시글 상세조회](#게시글-상세조회---합류-신청)
    8. [게시글 작성 페이지](#게시글-작성-페이지)

## Tech Stack

- Java Version : Java11
- Compiler : Gradle(2.5.6)
- IDE : IntellijIDEA
- Framework : Spring Boot
- DB : MariaDB
- Library : JPA(QueryDsl), Spring Security, Swagger, Lombok, Junit, JaCoCo, AWS Service.. etc

## ERD

![ERD](/src/main/resources/images/erd.png)


## 기능 명세서

### 회원
- oauth 로그인
- 로그아웃
- 회원 탈퇴
- 회원 조회
- 회원 글 조회

### 프로필
- 유저 작성글 조회
- 프로필 조회
- 프로필 변

### 게시글
- 게시글 작성
- 게시글 수정
- 게시글 삭제
- 게시글 전체 조회
- 게시글 상세 조회
- 게시글 태그 조회
- 게시글 상태 변경
- 게시글 참가 요청 리스트

자세한 내용은 해당 [링크](https://docs.google.com/spreadsheets/d/1gj5E9yI-YTUHkKQe25XOmJePPNLsqJeavXUmPhSPP4Y/edit#gid=0)를 참고해주세요!


## API 명세서

API 명세서는 노션으로 작성되어 해당 [링크](https://honghyunin.notion.site/API-6d4285d9dec94e228fad2ecfc9875172)를 통해 확인해보실 수 있습니다!


## 프로젝트 UI

### 프로모션 페이지

![promotion](/src/main/resources/images/promotion.png)

### 로그인 모달

![login-modal](/src/main/resources/images/login-modal.png)

### 프로필 조회

![profile-lookup](/src/main/resources/images/profile-lookup.png)

### 프로필 수정

![profile-update](/src/main/resources/images/profile-update.png)

### 메인화면

![mainpage](/src/main/resources/images/mainpage.png)

### 메인화면 - 게시글 조회 필터 모달

![board-filter-modal](/src/main/resources/images/board-filter-modal.png)

### 게시글 상세조회 - 합류 신청

![board-lookup-attend](/src/main/resources/images/board-lookup-attend.png)

### 게시글 작성 페이지

![board-write-page](/src/main/resources/images/board-write-page.png)
