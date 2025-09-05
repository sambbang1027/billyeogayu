<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<header class="u-header">
  <div class="u-header__inner">
    <!-- 로고 -->
    <a href="<c:url value='/resource/list'/>" class="u-logo">
      <img src="<c:url value='/assets/layout/user/logo.svg'/>" alt="빌려가유 로고">
    </a>

    <!-- 검색 + 필터 -->
    <div class="u-tools">
      <!-- 검색 -->
      <form class="u-search" action="<c:url value='/resource/list'/>" method="get">
        <input type="text" name="q" value="${param.q}" placeholder="농기계를 검색하세요." aria-label="검색">
        <button type="submit" aria-label="검색 실행">
          <i class="u-ic u-ic--search" aria-hidden="true"></i>
        </button>
      </form>

      <!-- 필터 -->
      <div class="u-filter">
        <button type="button" id="filterBtnHeader" class="u-filter__btn" aria-haspopup="true" aria-expanded="false">
          <span id="filterLabelHeader">
            <c:out value="${param.filter eq 'available' ? '임대가능만' : '전체'}"/>
          </span>
          <svg class="u-filter__caret" width="18" height="18" viewBox="0 0 24 24" aria-hidden="true">
            <path d="M6 9l6 6 6-6" fill="none" stroke="#222" stroke-width="2"/>
          </svg>
        </button>

        <div class="u-filter__menu" id="filterMenuHeader">
          <a href="<c:url value='/resource/list'><c:param name='q' value='${param.q}'/></c:url>">전체</a>
          <a href="<c:url value='/resource/list'><c:param name='q' value='${param.q}'/><c:param name='filter' value='available'/></c:url>">임대가능만</a>
        </div>
      </div>
    </div>

    <!-- 우측 사용자 메뉴 -->
    <nav class="u-actions">
      <c:if test="${empty sessionScope.loginUser}">
        <a class="u-action" href="<c:url value='/signup'/>" title="회원가입">
          <img src="<c:url value='/assets/layout/user/signup.svg'/>" alt=""><span>회원가입</span>
        </a>
        <a class="u-action" href="<c:url value='/login'/>" title="로그인">
          <img src="<c:url value='/assets/layout/user/login.svg'/>" alt=""><span>로그인</span>
        </a>
        <a class="u-action" href="<c:url value='/mypage'/>" title="마이페이지">
          <img src="<c:url value='/assets/layout/user/mypage.svg'/>" alt=""><span>마이페이지</span>
        </a>
      </c:if>
      <c:if test="${not empty sessionScope.loginUser}">
        <a class="u-action" href="<c:url value='/mypage'/>" title="마이페이지">
          <img src="<c:url value='/assets/layout/user/mypage.svg'/>" alt=""><span>마이페이지</span>
        </a>
        <a class="u-action" href="<c:url value='/logout'/>" title="로그아웃">
          <img src="<c:url value='/assets/layout/user/logout.svg'/>" alt=""><span>로그아웃</span>
        </a>
      </c:if>
    </nav>
  </div>
</header>

<script>
(function(){
  const wrap = document.querySelector('header .u-filter');
  const btn  = document.getElementById('filterBtnHeader');

  function closeMenu(){ wrap?.classList.remove('is-open'); btn?.setAttribute('aria-expanded','false'); }
  function toggleMenu(){ wrap?.classList.toggle('is-open'); btn?.setAttribute('aria-expanded', String(wrap.classList.contains('is-open'))); }

  btn?.addEventListener('click', (e)=>{ e.stopPropagation(); toggleMenu(); });
  document.addEventListener('click', (e)=>{ if(!e.target.closest('header .u-filter')) closeMenu(); });
  document.addEventListener('keydown', (e)=>{ if(e.key === 'Escape') closeMenu(); });

  closeMenu();
})();
</script>
