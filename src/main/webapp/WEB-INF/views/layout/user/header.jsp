<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 작성자 : 이해든 --%>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- 경로 변수(필요시 여기만 수정) --%>
<c:url var="listUrl"    value="/resource/list"/>
<c:url var="loginUrl"   value="/login"/>
<c:url var="joinUrl"    value="/signup"/>
<c:url var="logoutUrl"  value="/logout"/>
<c:url var="mypageUrl"  value="/mypage"/>
<c:url var="historyUrl" value="/reservation/history"/>

<header class="u-header">
  <div class="u-header__inner">
    <!-- 왼쪽: 로고 -->
    <a href="${listUrl}" class="u-logo" aria-label="메인으로 이동">
      <img src="<c:url value='/assets/layout/user/logo.svg'/>" alt="빌려가유 로고">
    </a>

    <!-- 가운데: 검색 + 필터 -->
    <div class="u-tools">
      <!-- 검색 -->
      <form class="u-search" action="${listUrl}" method="get" role="search">
        <input type="text" name="q" value="${param.q}" placeholder="농기계를 검색하세요." aria-label="검색어 입력">
        <button type="submit" class="u-search__btn" aria-label="검색 실행">
          <span class="u-ic--search" aria-hidden="true"></span>
        </button>
      </form>

      <!-- 필터 -->
      <div class="u-filter">
        <button type="button" id="filterBtnHeader" class="u-filter__btn" aria-haspopup="true" aria-expanded="false">
          <span id="filterLabelHeader">
            <c:out value="${param.filter eq 'available' ? '임대가능만' : '전체'}" />
          </span>
          <svg class="u-filter__caret" width="18" height="18" viewBox="0 0 24 24" aria-hidden="true">
            <path d="M6 9l6 6 6-6" fill="none" stroke="#222" stroke-width="2" />
          </svg>
        </button>

        <c:url var="allUrl" value="/resource/list">
          <c:param name="q" value="${param.q}" />
        </c:url>
        <c:url var="availUrl" value="/resource/list">
          <c:param name="q" value="${param.q}" />
          <c:param name="filter" value="available" />
        </c:url>

        <div class="u-filter__menu" id="filterMenuHeader">
          <a href="${allUrl}">전체</a>
          <a href="${availUrl}">임대가능만</a>
        </div>
      </div>
    </div>

    <!-- 오른쪽: 사용자 메뉴(텍스트만) -->
    <nav class="u-actions" aria-label="사용자 메뉴">
      <c:choose>
        <%-- 비로그인: 로그인, 회원가입 --%>
        <c:when test="${empty sessionScope.loginUser}">
          <a class="u-action" href="${loginUrl}">로그인</a>
          <a class="u-action" href="${joinUrl}">회원가입</a>
        </c:when>
          <%-- 로그인: 로그아웃, 신청내역, 내정보 --%>
        <c:otherwise>
          <a class="u-action" href="${logoutUrl}">로그아웃</a>
          <a class="u-action" href="${historyUrl}">신청내역</a>
          <a class="u-action" href="${mypageUrl}">내정보</a>
        </c:otherwise>
      </c:choose>
    </nav>
  </div>
</header>

<script>
(function(){
  const wrap = document.querySelector('header .u-filter');
  const btn  = document.getElementById('filterBtnHeader');

  function closeMenu(){
    wrap?.classList.remove('is-open');
    btn?.setAttribute('aria-expanded','false');
  }
  function toggleMenu(){
    const open = !wrap.classList.contains('is-open');
    wrap.classList.toggle('is-open', open);
    btn?.setAttribute('aria-expanded', String(open));
  }

  btn?.addEventListener('click', (e)=>{ e.stopPropagation(); toggleMenu(); });
  document.addEventListener('click', (e)=>{ if(!e.target.closest('header .u-filter')) closeMenu(); });
  document.addEventListener('keydown', (e)=>{ if(e.key === 'Escape') closeMenu(); });
  closeMenu();
})();
</script>

<!-- 공통 플래시 알림 -->
<c:if test="${not empty flashMsg}">
  <script> alert(`${fn:escapeXml(flashMsg)}`); </script>
</c:if>
