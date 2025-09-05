<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="<c:url value='/static/css/style.css'/>">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
    <div class="container">
        <header class="header">
            <h1>${title}</h1>
            <p class="subtitle">Spring Legacy + JSP + MyBatis 프로젝트</p>
        </header>
        
        <main class="main">
            <div class="welcome-card">
                <h2>환영합니다!</h2>
                <p class="message">${message}</p>
                <div class="info">
                    <p><strong>현재 시간:</strong> ${currentTime}</p>
                    <p><strong>서버 상태:</strong> <span class="status-ok">정상</span></p>
                </div>
            </div>
            
            <div class="features">
                <h3>기술 스택</h3>
                <div class="tech-stack">
                    <div class="tech-item">
                        <h4>Backend</h4>
                        <ul>
                            <li>Spring Legacy (MVC)</li>
                            <li>Java 17</li>
                            <li>Gradle</li>
                            <li>Tomcat 9</li>
                        </ul>
                    </div>
                    <div class="tech-item">
                        <h4>Database</h4>
                        <ul>
                            <li>Oracle 19c</li>
                            <li>MyBatis</li>
                            <li>HikariCP</li>
                        </ul>
                    </div>
                    <div class="tech-item">
                        <h4>Frontend</h4>
                        <ul>
                            <li>JSP</li>
                            <li>HTML5</li>
                            <li>CSS3</li>
                            <li>JavaScript</li>
                            <li>jQuery 3.7.x</li>
                        </ul>
                    </div>
                </div>
            </div>
        </main>
        
        <footer class="footer">
            <p>&copy; 2024 Billyeogayu. All rights reserved.</p>
        </footer>
    </div>
    
    <script>
        $(document).ready(function() {
            console.log('Billyeogayu 프로젝트가 성공적으로 로드되었습니다.');
            
            // 헬스 체크
            $.get('/health', function(data) {
                console.log('헬스 체크 결과:', data);
            });
        });
    </script>
</body>
</html>