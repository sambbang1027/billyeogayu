<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link rel="stylesheet" href="<c:url value='/static/css/resource/list.css'/>">

<section class="asset-wrap">
  <!-- ↑ 더 이상 우측 상단 01(롤링넘버) 안 씀 -->

  <section class="asset-grid">
    <c:choose>
      <c:when test="${not empty items}">
        <c:forEach var="it" items="${items}">
          <c:if test="${empty param.filter || param.filter ne 'available' || it.rentable}">
            <article class="asset-card" data-available="${it.rentable}">
              <div class="asset-card__badge ${it.rentable ? 'is-ok' : 'is-no'}">
                <span>${it.rentable ? '임대가능' : '임대불가'}</span>
              </div>

              <c:choose>
                <c:when test="${empty it.imageUrl}">
                  <div class="asset-card__img is-empty">이미지 준비중</div>
                </c:when>
                <c:otherwise>
                  <div class="asset-card__img">
                    <img src="<c:url value='${it.imageUrl}'/>" alt="<c:out value='${it.name}'/>" loading="lazy">
                  </div>
                </c:otherwise>
              </c:choose>

              <div class="asset-card__body">
                <h3 class="asset-card__title"><c:out value="${it.name}"/></h3>
                <div class="asset-card__meta">
                  <span class="asset-card__cat"><c:out value="${it.category}"/></span>
                  <span class="asset-card__stock">보유대수 :
                    <fmt:formatNumber value="${it.stock}" pattern="#"/>대
                  </span>
                </div>
                <div class="asset-card__line"></div>

                <button class="asset-card__cta" data-id="${it.id}"
                        <c:if test="${!it.rentable}">disabled="disabled" aria-disabled="true" title="현재 임대 불가"</c:if>>
                  <c:out value="${it.rentable ? '임대신청' : '임대불가'}"/>
                </button>
              </div>
            </article>
          </c:if>
        </c:forEach>
      </c:when>
      <c:otherwise>
        <div style="grid-column: 1 / -1; text-align:center; padding:40px 0; color:#666;">
          조건에 맞는 자원이 없습니다.
        </div>
      </c:otherwise>
    </c:choose>
  </section>

  <!-- 페이지네이션: page 없을 때도 1 노출 -->
  <c:set var="totalPages" value="${page != null ? page.totalPages : 1}" />
  <c:set var="currentPage" value="${page != null ? page.number : 1}" />
  <nav class="asset-paging" aria-label="페이지 이동">
    <c:forEach var="p" begin="1" end="${totalPages}">
      <a class="asset-paging__item ${p == currentPage ? 'is-active' : ''}"
         href="<c:url value='/resource/list'>
                  <c:param name='q' value='${param.q}'/>
                  <c:param name='filter' value='${param.filter}'/>
                  <c:param name='page' value='${p}'/>
               </c:url>">${p}</a>
    </c:forEach>
  </nav>
</section>

<script>
  // 버튼 클릭 로직은 그대로
  document.querySelectorAll('.asset-card__cta').forEach(function(b){
    b.addEventListener('click', function(){
      const loggedIn = !!(${not empty sessionScope.loginUser});
      if(!loggedIn){
        if(confirm('로그인이 필요합니다. 로그인 페이지로 이동할까요?')){
          location.href = '<c:url value="/login"/>';
        }
        return;
      }
      if (this.hasAttribute('disabled')) return;
      const id = this.getAttribute('data-id');
      location.href = '<c:url value="/rent/apply"/>' + '?resourceId=' + encodeURIComponent(id);
    });
  });
</script>
