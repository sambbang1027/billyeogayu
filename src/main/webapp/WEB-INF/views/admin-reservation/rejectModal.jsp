<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 작성자 : 서샘이 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- 오버레이 -->
<div id="rv-rejectOverlay" class="rv-overlay"></div>

<!-- 모달 -->
<div id="rv-rejectModal" class="rv-reject-modal">
	<div class="reject-header">
		<h3> 예약 반려</h3>
	</div>

  <h4 class="reject-title">반려 사유를 입력해주세요.</h4>
  
  <label class="reject-label" for="rejectReason">반려사유</label>
  <textarea id="rejectReason" class="reject-textarea"></textarea>
  
  <div class="reject-actions">
    <button class="btn-reject-confirm">반 려</button>
    <button class="btn-reject-cancel">취 소</button>
  </div>
</div>
