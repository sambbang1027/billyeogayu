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

- **회원 관리**: CRUD 기능
- **데이터베이스 연동**: Oracle + MyBatis
- **웹 인터페이스**: JSP + jQuery
- **에러 처리**: 404, 500 에러 페이지
- **로깅**: Logback 설정
- **테스트**: JUnit 5 통합 테스트

## 🧪 테스트

```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "com.app.service.MemberServiceTest"
```

## 📝 API 엔드포인트

### 홈
- `GET /` - 홈 페이지
- `GET /health` - 헬스 체크

### 회원 관리
- `GET /members` - 회원 목록
- `GET /members/{id}` - 회원 상세
- `GET /members/new` - 회원 등록 폼
- `POST /members` - 회원 등록
- `GET /members/{id}/edit` - 회원 수정 폼
- `POST /members/{id}` - 회원 수정
- `POST /members/{id}/delete` - 회원 삭제
- `GET /members/api/health` - DB 연결 테스트

## 🔒 보안

- 환경 변수를 통한 데이터베이스 인증 정보 관리
- SQL 인젝션 방지를 위한 MyBatis 파라미터 바인딩 사용
- XSS 방지를 위한 JSTL 사용

## 📊 모니터링

- **로깅**: Logback을 통한 구조화된 로깅
- **헬스 체크**: `/health` 엔드포인트
- **DB 연결 상태**: `/members/api/health` 엔드포인트

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 `LICENSE` 파일을 참조하세요.

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해 주세요.

---

**Billyeogayu** - Spring Legacy + JSP + MyBatis 프로젝트
