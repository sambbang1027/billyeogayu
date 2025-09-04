# Billyeogayu

Spring Legacy + JSP + MyBatis 프로젝트

## 📋 프로젝트 개요

이 프로젝트는 Spring Legacy (MVC), JSP, MyBatis를 사용한 웹 애플리케이션입니다.

## 🛠️ 기술 스택

### Backend
- **언어**: Java 17
- **프레임워크**: Spring Legacy (Spring MVC)
- **빌드 도구**: Gradle 8.4
- **DB 연동**: MyBatis 3.5.x
- **데이터베이스**: Oracle 19c
- **커넥션 풀**: HikariCP
- **서버**: Tomcat 9

### Frontend
- **언어**: JSP, HTML5, CSS3, JavaScript
- **라이브러리**: jQuery 3.7.x
- **UI**: 반응형 웹 디자인

### 협업 도구
- **버전 관리**: Git
- **협업 플랫폼**: GitHub
- **문서화**: Notion
- **디자인**: Figma
- **CI/CD**: GitHub Actions

## 🚀 시작하기

### 사전 요구사항

- Java 17 이상
- Oracle Database 19c
- Gradle 8.4 (또는 Gradle Wrapper 사용)


## 📁 프로젝트 구조

```
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/app/
│   │   │       ├── domains/
│   │   │       ├── common/
│   │   ├── resources/
│   │   │   ├── spring/            # Spring 설정
│   │   │   ├── mybatis/           # MyBatis 설정
│   │   │   ├── mapper/            # MyBatis XML 매퍼
│   │   │   └── sql/               # SQL 스크립트
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── jsp/           # JSP 페이지
│   │       │   └── web.xml        # 웹 설정
│   │       └── static/            # 정적 리소스
│   └── test/
│       ├── java/                  # 테스트 코드
│       └── resources/             # 테스트 리소스
├── gradle/                        # Gradle Wrapper
├── build.gradle                   # Gradle 빌드 설정
├── gradlew                        # Gradle Wrapper (Unix)
├── gradlew.bat                    # Gradle Wrapper (Windows)
└── README.md                      # 프로젝트 문서

```



## 🔧 주요 기능

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해 주세요.

---

**Billyeogayu** - Spring Legacy + JSP + MyBatis 프로젝트
