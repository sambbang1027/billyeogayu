<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!-- 점검완료 모달 -->
<div id="inspectionEditModal" class="inspection-modal hidden">
  <div class="inspection-modal-content">
    <div class="inspection-modal-header">
      <h2>점검 기록 수정</h2>
    </div>

    <div class="inspection-modal-body">
		<div class="inspection-info">
		  <!-- 왼쪽 -->
		  <div>
		    <div class="inspection-row">
		      <span class="inspection-label">점검일시</span>
		      <span class="inspection-value">2025.09.03 10:45:99</span>
		    </div>
		    <div class="inspection-row">
		      <span class="inspection-label">자원종류</span>
		      <span class="inspection-value">트랙터</span>
		    </div>
		  </div>
		
		  <!-- 오른쪽 -->
		  <div>
		    <div class="inspection-row">
		      <span class="inspection-label">점검자</span>
		      <span class="inspection-value">이전문</span>
		    </div>
		    <div class="inspection-row">
		      <span class="inspection-label">점검유형</span>
		      <span class="inspection-value">정기 점검</span>
		    </div>
		  </div>
		</div>
		
		<div class="inspection-row column">
		  <span class="inspection-label">점검항목</span>
		  <div class="inspection-tags">
		    <span class="tag">타이어</span>
		    <span class="tag">엔진</span>
		  </div>
		</div>


      <div class="inspection-row column" >
        <span class="inspection-label">조치내역</span>
        <div class="inspection-record-textarea" contenteditable="false">
		  엔진에서 심한 잡음이 들려 엔진을 교체하려했으나 여분의 엔진이 없어 교체하지 못함.
		엔진 재고가 들어오면 교체 요망
		</div>
      </div>

      <div class="inspection-row column">
        <span class="inspection-label">비고</span>
        <textarea class="inspection-textarea"></textarea>
      </div>
    </div>

    <div class="inspection-modal-footer">
      <button class="inspection-btn-complete">저장</button>
      <button class="inspection-btn-cancel close-btn">닫기</button>
    </div>
  </div>
</div>
