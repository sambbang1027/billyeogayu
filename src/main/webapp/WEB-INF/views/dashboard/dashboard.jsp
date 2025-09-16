<%-- 작성자 : 이원석 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="dashboard-container">
    <div class="main-content">
        <!-- 상단 지표 카드 섹션 -->
        <div class="metrics-section">
            <h2 class="section-title">자원 현황</h2>
            <div class="metrics-grid-first">
                <div class="metric-card">
                    <div class="metric-title">총 자원 수</div>
                    <div class="metric-value">${metrics.total}</div>
                </div>
                <div class="metric-card">
                    <div class="metric-title">가용 자원 수</div>
                    <div class="metric-value">${metrics.available}</div>
                </div>
                <div class="metric-card">
                    <div class="metric-title">점검 필요 자원 수</div>
                    <div class="metric-value">${metrics.maintenanceRequired}</div>
                </div>
                <div class="metric-card">
                    <div class="metric-title">사용중인 자원 수</div>
                    <div class="metric-value">${metrics.using}</div>
                </div>
                <div class="metric-card">
                    <div class="metric-title">점검중인 자원 수</div>
                    <div class="metric-value">${metrics.maintaining}</div>
                </div>
            </div>
        </div>

        <!-- 중간 차트 섹션 -->
        <div class="first-chart-section">
            <!-- 막대 차트 -->
            <div class="chart-wrapper">
                <h3 class="section-title">자원 종류별 현황</h3>
                <div class="chart-container">
                    <canvas id="resourceTypeChart"></canvas>
                </div>
                <div class="chart-legend">
                    <div class="legend-item">
                        <div class="legend-color legend-color-green"></div>
                        <span class="legend-text">총 자원수</span>
                    </div>
                    <div class="legend-item">
                        <div class="legend-color legend-color-red"></div>
                        <span class="legend-text">점검 필요</span>
                    </div>
                </div>
            </div>
            
            <!-- 파이 차트 -->
            <div class="chart-wrapper">
                <h3 class="section-title">자원 분포 현황</h3>
                <div class="chart-container">
                    <canvas id="distributionChart"></canvas>
                </div>
            </div>
        </div>

        <!--            하단 선 차트 섹션 -->
        <div class="second-chart-section">
            <!-- 왼쪽: 자원 종류별 사용량 -->
            <div class="asset-usage">
                <h2 class="section-title">자원 종류별 사용량</h2>
                <div class="chart-filters">
                    <!-- 동적으로 생성됨 -->
                </div>
                <div class="chart-container">
                    <canvas id="usageChart"></canvas>
                </div>
            </div>

            <!-- 오른쪽: 자원 종류별 점검율  -->
            <div class="asset-maintenance">
                <h2 class="section-title">자원 종류별 점검율</h2>
                <div class="chart-filters">
                    <button class="filter-btn active">트랙터</button>
                    <button class="filter-btn">TRAC-123</button>
                    <button class="filter-btn">연도별</button>
                    <button class="filter-btn">지역별</button>
                </div>
                <div class="chart-container">
                    <canvas id="inspectionChart"></canvas>
                </div>
            </div>

        </div>
    </div>
</div>