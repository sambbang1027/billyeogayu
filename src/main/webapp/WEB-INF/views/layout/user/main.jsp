<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		  <meta charset="UTF-8">
		  <title>${pageTitle}</title>
		  <link rel="stylesheet" href="<c:url value='/static/css/layout/user/header/style.css'/>">
	</head>
	<body>
	  <jsp:include page="/WEB-INF/views/layout/user/header.jsp" />
	  <main>
	    <jsp:include page="${contentPage}" />
	  </main>
	</body>
</html>
