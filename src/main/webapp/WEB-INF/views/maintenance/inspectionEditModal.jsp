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
		  <!-- 상단 -->
	
		  	<div class="inspection-row">
		      <span class="inspection-label">점검유형</span>
		      <span class="inspection-value type"></span>
		    </div>
		    <div class="inspection-row">
		    	<span class="inspection-label">점검자</span>
		      	<span class="inspection-value inspector"></span>
		    </div>
		  </div>

		  <!-- 하단  -->
		  <div class="inspection-info">
		    <div class="inspection-row">
		      <span class="inspection-label">자원종류</span>
		      <span class="inspection-value assetType"></span>
		    </div>
		    <div class="inspection-row">
		      <span class="inspection-label">모델명</span>
		      <span class="inspection-value assetName"></span>
		    </div>
		  </div>
	
	 <div class="inspection-info">
		    <div class="inspection-row">
		      <span class="inspection-label">소유주</span>
		      <span class="inspection-value owner"></span>
		    </div>
		  </div>
		  
		<div class="inspection-row column">
		  <span class="inspection-label">점검항목</span>
		  <div class="inspection-tags">
		  </div>
		</div>


      <div class="inspection-row column" >
        <span class="inspection-label">조치내역</span>
        <div class="inspection-record-textarea" contenteditable="false"></div>
      </div>

      <div class="inspection-row column">
        <span class="inspection-label">비고</span>
        <textarea class="inspection-textarea note"></textarea>
      </div>
    </div>

    <div class="inspection-modal-footer">
      <button class="inspection-btn-complete edit" type="button">저장</button>
      <button class="inspection-btn-cancel close-btn">닫기</button>
    </div>
  </div>
</div>
