<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>


<!-- 알람 모달 -->
<div id="alarmModal" class="modal">
    <div class="modal-content">
        <!-- 헤더 -->
        <div class="modal-header">
            <h2>알람</h2>
            <span class="modal-close">&times;</span>
        </div>

        <!-- 알람 리스트 -->
        <ul class="alarm-list">
        </ul>
    </div>
</div>