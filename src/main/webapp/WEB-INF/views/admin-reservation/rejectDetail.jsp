<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- 오버레이 -->
<div id="rv-rejectDetailOverlay" class="rv-overlay"></div>
	
	<!-- 모달 -->
	<div id="rv-rejectDetailModal" class="rv-reject-modal">
	  <div class="reject-header">
	    <h3>반려 사유 상세</h3>
	  </div>
	
	  <label class="reject-label" for="rv-rejectDetailReason">반려사유</label>
	  <textarea id="rv-rejectDetailReason" class="reject-textarea" readonly></textarea>
	
	  <div class="reject-actions">
	    <button class="btn-reject-detail-close">확 인</button>
	  </div>
</div>
