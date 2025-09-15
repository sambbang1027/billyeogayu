<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>빌려가유 - 농기계 목록</title>
    <link rel="stylesheet" href="<c:url value='/static/css/layout/user/resource/list.css'/>">
    <!-- 공통 모달(confirm/ alert) -->
    <link rel="stylesheet" href="<c:url value='/static/css/common/commonModal.css'/>">
</head>
<body>

    <!-- 헤더 -->
	<div class="header">
	    <div class="header-content">
	        <a href="<c:url value='/resource/list'/>" class="logo-link">
	            <img src="<c:url value='/assets/layout/user/logo.svg'/>" alt="로고" class="logo">
	        </a>
	        <div class="header-links">
	            <c:choose>
	                <c:when test="${isLoggedIn}">
	                    <!-- 로그인한 경우 -->
	                    <a href="<c:url value='/resource/list'/>" class="header-link">농기계 목록</a>
	                    <a href="<c:url value='/my/reservations'/>" class="header-link">내 예약</a>
	                    <a href="<c:url value='/my/usage-history'/>" class="header-link">사용 내역</a>
	                    <form action="<c:url value='/logout'/>" method="post" style="display: inline;" id="logoutForm">
	                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                        <button type="submit" class="header-link logout-btn">로그아웃</button>
	                    </form>
	                </c:when>
	                <c:otherwise>
	                    <!-- 로그인하지 않은 경우 -->
	                    <a href="<c:url value='/verification'/>" class="header-link">회원가입</a>
	                    <a href="<c:url value='/login'/>" class="header-link">로그인</a>
	                </c:otherwise>
	            </c:choose>
	        </div>
	    </div>
	</div>


    <!-- 검색 및 필터 영역 -->
    <div class="search-filter-section">
        <div class="search-container">
            <form class="search-form" action="<c:url value='/resource/list'/>" method="get">
                <div class="search-wrapper">
                    <input type="text" name="q" value="${param.q}" placeholder="농기계를 검색하세요." class="search-input">
                    <button type="submit" class="search-btn">검색</button>
                </div>
            </form>
            
            <div class="filter-wrapper">
                <div class="filter-dropdown">
                    <button type="button" id="filterBtn" class="filter-btn">
                        <span id="filterLabel">
                            <c:out value="${param.filter eq 'available' ? '임대가능만' : '전체보기'}" default="전체보기"/>
                        </span>
                        <span class="filter-arrow">▼</span>
                    </button>
                    <ul class="filter-menu" id="filterMenu" style="display: none;">
                        <li><a href="<c:url value='/resource/list'><c:param name='q' value='${param.q}'/></c:url>">전체보기</a></li>
                        <li><a href="<c:url value='/resource/list'><c:param name='q' value='${param.q}'/><c:param name='filter' value='available'/></c:url>">임대가능만</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <!-- 성공 메시지 -->
    <c:if test="${not empty successMessage}">
        <div class="success-message" style="background: #d4edda; color: #155724; padding: 15px; margin: 20px; border-radius: 5px; border: 1px solid #c3e6cb;">
            ${successMessage}
        </div>
    </c:if>

    <section class="resource-wrap">
        <section class="resource-grid">
            <c:choose>
                <c:when test="${not empty items}">
                    <c:forEach var="it" items="${items}">
                        <c:if test="${empty param.filter || param.filter ne 'available' || it.rentable}">
                            <article class="resource-card" data-available="${it.rentable}">
                                <div class="resource-card__badge ${it.rentable ? 'is-ok' : 'is-no'}">
                                    <span>${it.rentable ? '임대가능' : '임대불가'}</span>
                                </div>

                                <c:choose>
                                    <c:when test="${empty it.imagePath}">
                                        <div class="resource-card__img is-empty">이미지 준비중</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="resource-card__img">
                                            <img src="<c:url value='${it.imagePath}'/>" alt="<c:out value='${it.modelName}'/>" loading="lazy">
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                                <div class="resource-card__body">
                                    <h3 class="resource-card__title">
                                        <c:out value="${it.modelName}" />
                                    </h3>
                                    <div class="resource-card__meta">
                                        <span class="resource-card__cat"><c:out value="${it.category}" /></span> 
                                        <span class="resource-card__stock">보유대수 : <fmt:formatNumber value="${it.stock}" pattern="#" />대</span>
                                    </div>
                                    <div class="resource-card__line"></div>

                                    <button class="resource-card__cta" data-id="${it.assetId}"
                                        <c:if test="${!it.rentable}">disabled="disabled" aria-disabled="true" title="현재 임대 불가"</c:if>>
                                        <c:out value="${it.rentable ? '임대신청' : '임대불가'}" />
                                    </button>
                                </div>
                            </article>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div style="grid-column: 1/-1; text-align: center; padding: 40px 0; color: #666;">
                        조건에 맞는 자원이 없습니다.
                        <div style="margin-top: 16px;">
                            <a class="resource-paging__item" href="<c:url value='/resource/list'/>">전체 보기</a> 
                            <a class="resource-paging__item" href="<c:url value='/resource/list'><c:param name='q' value='${param.q}'/></c:url>">필터 초기화</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

  
        <!-- 페이지네이션 -->
        <c:if test="${totalPages >= 1}">
            <div class="pagination" aria-label="페이지 이동">
                <!-- 이전 화살표 -->
                <c:if test="${page > 1}">
                    <a class="arrow prev" href="<c:url value='/resource/list'><c:param name='q' value='${param.q}'/><c:param name='filter' value='${param.filter}'/><c:param name='page' value='${page - 1}'/></c:url>">
                        <img src="<c:url value='/assets/asset/left.svg'/>" alt="이전">
                    </a>
                </c:if>

                <!-- 페이지 번호 (5개 단위 그룹) -->
                <c:set var="pageGroupSize" value="5"/>
                <c:set var="groupStart" value="${((page - 1) / pageGroupSize) * pageGroupSize + 1}"/>
                <c:set var="groupEnd" value="${groupStart + pageGroupSize - 1}"/>
                <c:if test="${groupEnd > totalPages}">
                    <c:set var="groupEnd" value="${totalPages}"/>
                </c:if>

                <c:forEach var="p" begin="${groupStart}" end="${groupEnd}">
                    <a class="${p == page ? 'active' : ''}" href="<c:url value='/resource/list'><c:param name='q' value='${param.q}'/><c:param name='filter' value='${param.filter}'/><c:param name='page' value='${p}'/></c:url>">${p}</a>
                </c:forEach>

                <!-- 다음 화살표 -->
                <c:if test="${page < totalPages}">
                    <a class="arrow next" href="<c:url value='/resource/list'><c:param name='q' value='${param.q}'/><c:param name='filter' value='${param.filter}'/><c:param name='page' value='${page + 1}'/></c:url>">
                        <img src="<c:url value='/assets/asset/right.svg'/>" alt="다음">
                    </a>
                </c:if>
            </div>
        </c:if>
    </section>


    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- 공통 모달 JS -->
    <script src="<c:url value='/static/js/common/commonModal.js'/>"></script>

    <!-- 공통 모달 HTML include -->
    <jsp:include page="/WEB-INF/views/common/commonModal.jsp" />


    <script>
    $(function(){
        // 필터 드롭다운 토글
        $('#filterBtn').on('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            $('#filterMenu').toggle();
        });
        
        // 외부 클릭 시 필터 메뉴 닫기
        $(document).on('click', function() {
            $('#filterMenu').hide();
        });
        
        // 임대신청 버튼 처리
        $('.resource-card__cta').on('click', function(){
            if ($(this).is(':disabled')) return;

            var id = $(this).data('id');
            
            // 서버에서 전달받은 로그인 상태 확인
            var loggedIn = ${isLoggedIn ? 'true' : 'false'};
            
            console.log('로그인 상태:', loggedIn);
            
            if(!loggedIn){

                // showConfirm으로 변경 (공통 모달 사용)
                showConfirm('로그인이 필요합니다. 로그인 페이지로 이동할까요?',
                    function() {  // 확인 버튼 콜백
                        sessionStorage.setItem('returnUrl', '<c:url value="/reservation/apply"/>?assetId=' + id);
                        window.location.href = '<c:url value="/login"/>';
                    },
                    function() {  // 취소 버튼 콜백 (생략 가능)
                        // 취소 시 아무것도 하지 않음
                    }
                );
                return;
            }
            
            // 로그인된 상태면 바로 예약 신청 페이지로 이동
            window.location.href = '<c:url value="/reservation/apply"/>' + '?assetId=' + encodeURIComponent(id);
        });
    });
    </script>
</body>
</html>