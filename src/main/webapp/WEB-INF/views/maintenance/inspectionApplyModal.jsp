<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>


<!-- 점검 신청 모달 -->
<div id="inspectionApplyModal" class="inspection-apply-modal hidden">
  <div class="inspection-apply-modal-content">
    <!-- 헤더 -->
    <div class="inspection-apply-modal-header">
      <h2>점검 신청</h2>
    </div>

    <!-- 바디 -->
    <div class="inspection-apply-modal-body">
      <!-- 신청 정보 -->
      <div class="apply-section">
        <h3 class="apply-section-title">신청 정보</h3>

        <div class="apply-info">
 <%--          <div class="apply-row">
            <label class="apply-label">신청일자</label>
	            <div class="apply-input-wrapper">
			          <input type="text" id="inspectionDate" class="date-input" placeholder="날짜 선택">
					  <span class="calendar-icon">
					    <img src="<c:url value='/assets/maintenance/calendar.svg'/>" alt="달력">
					  </span>
	            </div>
          </div> --%>

          <div class="apply-row">
            <label class="apply-label">점검자</label>
            <input type="text" class="apply-input" placeholder="점검자 이름 입력">
          </div>
        </div>
      </div>

      <!-- 점검 정보 -->
      <div class="apply-section">
        <h3 class="apply-section-title">점검 정보</h3>

        <div class="apply-info">
          <div class="apply-row">
            <label class="apply-label">자원명</label>
            <div class="apply-input-wrapper">
              <input type="text" class="apply-input" placeholder="자원명 검색">
              <img alt="검색" class="input-icon" src="<c:url value='/assets/maintenance/non-back-search.svg'/>">
            </div>
          </div>
          
          <div class="custom-dropdown apply">
						<label class="custom-dropdown-name">점검유형</label>
						<div class="dropdown-box apply">
										<button class="dropdown-toggle apply">
								    			<span class="dropdown-label apply">전체</span>
								    			<img src="<c:url value='/assets/maintenance/arrow-down.svg'/>">
								  		</button>
								  		<ul class="dropdown-menu apply">
								  <!-- >>유형들  list로 불러와서 for문으로 뿌려야함 -->
										    	<li class="active">전체</li>
										    	<li>정기점검</li>
										    	<li>긴급점검</li>
								  </ul>
						</div>
					</div>
          
          
        </div>

        <div class="apply-row column">
         <label for="inspectionItems">점검 항목</label>
			<select id="inspectionItems" multiple>
			  <option value="타이어" selected>타이어</option>
			  <option value="엔진" selected>엔진</option>
			  <option value="브레이크" selected>브레이크</option>
			  <option value="에어크리너">에어크리너</option>
			  <option value="냉각수">냉각수</option>
			</select>
        </div>
      </div>
    </div>

    <!-- 푸터 -->
    <div class="inspection-apply-modal-footer">
      <button class="apply-btn-submit">등록</button>
      <button class="apply-btn-cancel close-btn">취소</button>
    </div>
  </div>
</div>

<style>
.choices__list--multiple .choices__item .choices__button {
  background-image: url('<c:url value="/assets/asset/xbtn.svg"/>');
}
</style>
