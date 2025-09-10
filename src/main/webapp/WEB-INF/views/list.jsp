<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<link rel="stylesheet"
	href="<c:url value='/static/css/layout/user/resource/list.css'/>">
<link rel="stylesheet"
	href="<c:url value='/static/css/layout/user/header/style.css'/>" />

<c:if test="${not empty successMessage}">
	<div class="success-message"
		style="background: #d4edda; color: #155724; padding: 15px; margin: 20px; border-radius: 5px; border: 1px solid #c3e6cb;">
		${successMessage}</div>
</c:if>

<jsp:include page="/WEB-INF/views/layout/user/header.jsp" />

<section class="asset-wrap">
	<section class="asset-grid">
		<c:choose>
			<c:when test="${not empty items}">
				<c:forEach var="it" items="${items}">
					<c:if
						test="${empty param.filter || param.filter ne 'available' || it.rentable}">
						<article class="asset-card" data-available="${it.rentable}">
							<div class="asset-card__badge ${it.rentable ? 'is-ok' : 'is-no'}">
								<span>${it.rentable ? '임대가능' : '임대불가'}</span>
							</div>

							<c:choose>
								<c:when test="${empty it.imagePath}">
									<div class="asset-card__img is-empty">이미지 준비중</div>
								</c:when>
								<c:otherwise>
									<div class="asset-card__img">
										<img src="<c:url value='${it.imagePath}'/>"
											alt="<c:out value='${it.modelName}'/>" loading="lazy">
									</div>
								</c:otherwise>
							</c:choose>

							<div class="asset-card__body">
								<h3 class="asset-card__title">
									<c:out value="${it.modelName}" />
								</h3>
								<div class="asset-card__meta">
									<span class="asset-card__cat"><c:out
											value="${it.category}" /></span> <span class="asset-card__stock">보유대수
										: <fmt:formatNumber value="${it.stock}" pattern="#" />대
									</span>
								</div>
								<div class="asset-card__line"></div>

								<button class="asset-card__cta" data-id="${it.assetId}"
									<c:if test="${!it.rentable}">disabled="disabled" aria-disabled="true" title="현재 임대 불가"</c:if>>
									<c:out value="${it.rentable ? '임대신청' : '임대불가'}" />
								</button>
							</div>
						</article>
					</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div
					style="grid-column: 1/-1; text-align: center; padding: 40px 0; color: #666;">
					조건에 맞는 자원이 없습니다.
					<div style="margin-top: 16px;">
						<a class="asset-paging__item"
							href="<c:url value='/resource/list'/>">전체 보기</a> <a
							class="asset-paging__item"
							href="<c:url value='/resource/list'>
              <c:param name='q' value='${param.q}'/>
            </c:url>">필터
							초기화</a>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</section>

	<!-- 페이지네이션 -->
	<c:if test="${totalPages >= 1}">
		<nav class="asset-paging" aria-label="페이지 이동">

			<!-- 처음(«) -->
			<c:choose>
				<c:when test="${page > 1}">
					<a class="asset-paging__item"
						href="<c:url value='/resource/list'>
                     <c:param name='q' value='${param.q}'/>
                     <c:param name='filter' value='${param.filter}'/>
                     <c:param name='page' value='1'/>
                   </c:url>">&laquo;</a>
				</c:when>
				<c:otherwise>
					<span class="asset-paging__item" aria-disabled="true">&laquo;</span>
				</c:otherwise>
			</c:choose>

			<!-- 이전(‹) -->
			<c:choose>
				<c:when test="${page > 1}">
					<a class="asset-paging__item"
						href="<c:url value='/resource/list'>
                     <c:param name='q' value='${param.q}'/>
                     <c:param name='filter' value='${param.filter}'/>
                     <c:param name='page' value='${prevPage}'/>
                   </c:url>">&lsaquo;</a>
				</c:when>
				<c:otherwise>
					<span class="asset-paging__item" aria-disabled="true">&lsaquo;</span>
				</c:otherwise>
			</c:choose>

			<!-- 이전 블록 -->
			<c:if test="${hasPrevBlock}">
				<a class="asset-paging__item"
					href="<c:url value='/resource/list'>
                   <c:param name='q' value='${param.q}'/>
                   <c:param name='filter' value='${param.filter}'/>
                   <c:param name='page' value='${prevBlockPage}'/>
                 </c:url>">...</a>
			</c:if>

			<!-- 현재 블록의 페이지들 -->
			<c:forEach var="p" begin="${startPage}" end="${endPage}">
				<a class="asset-paging__item ${p == page ? 'is-active' : ''}"
					href="<c:url value='/resource/list'>
                   <c:param name='q' value='${param.q}'/>
                   <c:param name='filter' value='${param.filter}'/>
                   <c:param name='page' value='${p}'/>
                 </c:url>">${p}</a>
			</c:forEach>

			<!-- 다음 블록 -->
			<c:if test="${hasNextBlock}">
				<a class="asset-paging__item"
					href="<c:url value='/resource/list'>
                   <c:param name='q' value='${param.q}'/>
                   <c:param name='filter' value='${param.filter}'/>
                   <c:param name='page' value='${nextBlockPage}'/>
                 </c:url>">...</a>
			</c:if>

			<!-- 다음(›) -->
			<c:choose>
				<c:when test="${page < totalPages}">
					<a class="asset-paging__item"
						href="<c:url value='/resource/list'>
                     <c:param name='q' value='${param.q}'/>
                     <c:param name='filter' value='${param.filter}'/>
                     <c:param name='page' value='${nextPage}'/>
                   </c:url>">&rsaquo;</a>
				</c:when>
				<c:otherwise>
					<span class="asset-paging__item" aria-disabled="true">&rsaquo;</span>
				</c:otherwise>
			</c:choose>

			<!-- 마지막(») -->
			<c:choose>
				<c:when test="${page < totalPages}">
					<a class="asset-paging__item"
						href="<c:url value='/resource/list'>
                     <c:param name='q' value='${param.q}'/>
                     <c:param name='filter' value='${param.filter}'/>
                     <c:param name='page' value='${totalPages}'/>
                   </c:url>">&raquo;</a>
				</c:when>
				<c:otherwise>
					<span class="asset-paging__item" aria-disabled="true">&raquo;</span>
				</c:otherwise>
			</c:choose>

		</nav>
	</c:if>
</section>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- 세션 기반 로그인 체크로 수정된 스크립트 -->
<script>
  $(function(){
    $('.asset-card__cta').on('click', function(){
      if ($(this).is(':disabled')) return;

      var id = $(this).data('id'); // data-id
      
      // 서버에서 전달받은 로그인 상태를 확인
      var loggedIn = ${isLoggedIn ? 'true' : 'false'};
      
      
      console.log('로그인 상태:', loggedIn); // 디버깅용
      
      if(!loggedIn){
        if(confirm('로그인이 필요합니다. 로그인 페이지로 이동할까요?')){
          // 로그인 후 돌아올 페이지를 세션에 저장
          sessionStorage.setItem('returnUrl', '<c:url value="/reservation/apply"/>?assetId=' + id);
          window.location.href = '<c:url value="/login"/>';
        }
        return;
      }
      
      // 로그인된 상태면 바로 예약 신청 페이지로 이동
      window.location.href = '<c:url value="/reservation/apply"/>' + '?assetId=' + encodeURIComponent(id);
    });
  });
</script>