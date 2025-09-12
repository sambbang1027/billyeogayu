<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!-- 점검중 모달 -->
<div id="inspectionModal" class="inspection-modal hidden">
  <div class="inspection-modal-content">
    <div class="inspection-modal-header">
      <h2>점검 기록 등록</h2>
    </div>

    <div class="inspection-modal-body">
		<div class="inspection-info">
		  <!-- 왼쪽 -->
		  <div>
		  	<div class="inspection-row">
		      <span class="inspection-label">점검유형</span>
		      <span class="inspection-value type"></span>
		    </div>
		    <div class="inspection-row">
		      <span class="inspection-label">자원종류</span>
		      <span class="inspection-value assetType"></span>
		    </div>
		  </div>
		
		  <!-- 오른쪽 -->
		  <div>
		    <div class="inspection-row">
		      <span class="inspection-label">점검자</span>
		      <span class="inspection-value inspector"></span>
		    </div>
		    <div class="inspection-row">
		      <span class="inspection-label">모델명</span>
		      <span class="inspection-value assetName"></span>
		    </div>
		  </div>
		</div>
		
		<div class="inspection-row column">
		  <span class="inspection-label">점검항목</span>
		  <div class="inspection-tags"></div>
		</div>


      <div class="inspection-row column" >
        <span class="inspection-label">조치내역</span>
		<textarea class="inspection-textarea action"></textarea>
      </div>

      <div class="inspection-row column">
        <span class="inspection-label">비고</span>
        <textarea class="inspection-textarea note"></textarea>
      </div>
    </div>

    <div class="inspection-modal-footer">
      <button class="inspection-btn-complete create" type="button">점검 완료</button>
      <button class="inspection-btn-cancel close-btn">취소</button>
    </div>
  </div>
</div>

