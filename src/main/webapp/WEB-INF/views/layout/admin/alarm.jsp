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
        <%-- TODO: alarm 엔티티 조회 후 데이터 렌더링 --%>
        <ul class="alarm-list">
            <li class="alarm-item">
                <div class="alarm-icon">
                    <img src="<c:url value='/assets/layout/admin/alarm-detail.svg'/>" alt="알람"/>
                </div>
                <div class="alarm-body">
                    <div class="alarm-message">
                        트랙터 TRAC-123 ID 8564의 점검 주기가 5일 남았습니다. 점검 요망
                    </div>
                    <div class="alarm-date">2025.09.11</div>
                </div>
            </li>
            <li class="alarm-item">
                <div class="alarm-icon">
                    <img src="<c:url value='/assets/layout/admin/alarm-detail.svg'/>" alt="알람"/>
                </div>
                <div class="alarm-body">
                    <div class="alarm-message">
                        트랙터 TRAC-456 ID 2345의 점검 주기가 3일 남았습니다. 점검 요망
                    </div>
                    <div class="alarm-date">2025.09.11</div>
                </div>
            </li>
            <li class="alarm-item">
                <div class="alarm-icon">
                    <img src="<c:url value='/assets/layout/admin/alarm-detail.svg'/>" alt="알람"/>
                </div>
                <div class="alarm-body">
                    <div class="alarm-message">
                        트랙터 TRAC-789 ID 9876의 점검 주기가 1일 남았습니다. 점검 요망
                    </div>
                    <div class="alarm-date">2025.09.11</div>
                </div>
            </li>
        </ul>
    </div>
</div>