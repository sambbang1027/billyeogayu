// 공통 옵션
Chart.defaults.font.family = "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif";
Chart.defaults.font.size = 14;
Chart.defaults.color = "#666";
Chart.defaults.responsive = true;
Chart.defaults.maintainAspectRatio = false;

const COMMON_OPTIONS = {
    plugins: { legend: { display: false } }, // datalabels 항목 제거 가능
    interaction: { mode: "index", intersect: false },
    scales: { y: { beginAtZero: true } },
};

// 파이 차트용 공통 옵션
const PIE_COMMON_OPTIONS = {
    plugins: { 
        legend: { 
            display: true,
            position: 'bottom',
            labels: {
                padding: 20,
                usePointStyle: true,
                font: {
                    size: 12
                }
            }
        }
    },
    interaction: { mode: "index", intersect: false }
};

// 유틸: 엘리먼트가 있을 때만 생성
function initChart(id, factory) {
    const el = document.getElementById(id);
    if (!el) {
        console.log(`Canvas element '${id}' not found`);
        return null;
    }
    console.log(`Creating chart for '${id}'`);
    return factory(el.getContext("2d"));
}

// AJAX로 카테고리 데이터 로드
function loadCategoryData() {
    console.log("Loading category data via AJAX...");
    
    fetch('/dashboard/categoryData')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(categoryData => {
            console.log('Category data loaded:', categoryData);
            createResourceTypeChart(categoryData);
        })
        .catch(error => {
            console.error('Error loading category data:', error);
            // 에러 시 기본 차트 표시
            createResourceTypeChart(getDefaultCategoryData());
        });
}

// AJAX로 분포 데이터 로드
function loadDistributionData() {
    console.log("Loading distribution data via AJAX...");
    
    // 스켈레톤 로딩 표시
    showSkeletonLoading("distributionChart");
    
    fetch('/dashboard/distributionData')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(distributionData => {
            console.log('Distribution data loaded:', distributionData);
            hideSkeletonLoading("distributionChart");
            createDistributionChart(distributionData);
        })
        .catch(error => {
            console.error('Error loading distribution data:', error);
            hideSkeletonLoading("distributionChart");
            showErrorMessage("distributionChart", "데이터를 불러오는데 실패했습니다.");
        });
}

// 기본 차트 데이터 (에러 시 사용)
function getDefaultCategoryData() {
    return {
        labels: ["트랙터", "콤바인", "이앙기", "운반차", "로더"],
        totalCounts: [0, 0, 0, 0, 0],
        maintenanceCounts: [0, 0, 0, 0, 0],
        maxCount: 10
    };
}

// 스켈레톤 로딩 표시
function showSkeletonLoading(chartId) {
    const canvas = document.getElementById(chartId);
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const radius = Math.min(centerX, centerY) - 20;
    
    // 파이 차트 스켈레톤 그리기
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // 5개 섹션으로 나누어 그리기
    for (let i = 0; i < 5; i++) {
        const startAngle = (i * 2 * Math.PI) / 5;
        const endAngle = ((i + 1) * 2 * Math.PI) / 5;
        
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radius, startAngle, endAngle);
        ctx.closePath();
        
        // 투명한 배경, 테두리만 표시
        ctx.fillStyle = 'rgba(200, 200, 200, 0.1)';
        ctx.fill();
        ctx.strokeStyle = 'rgba(200, 200, 200, 0.3)';
        ctx.lineWidth = 2;
        ctx.stroke();
    }
}

// 스켈레톤 로딩 숨기기
function hideSkeletonLoading(chartId) {
    const canvas = document.getElementById(chartId);
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

// 에러 메시지 표시
function showErrorMessage(chartId, message) {
    const canvas = document.getElementById(chartId);
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // 에러 메시지 그리기
    ctx.fillStyle = '#666';
    ctx.font = '14px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText(message, canvas.width / 2, canvas.height / 2);
}

// 자원 종류별 현황 차트 생성
function createResourceTypeChart(categoryData) {
    initChart(
        "resourceTypeChart",
        (ctx) =>
            new Chart(ctx, {
                type: "bar",
                data: {
                    labels: categoryData.labels,
                    datasets: [
                        {
                            label: "총 자원수",
                            data: categoryData.totalCounts,
                            backgroundColor: "#92BEA9",
                            borderColor: "#92BEA9",
                            borderWidth: 1,
                        },
                        {
                            label: "점검 필요",
                            data: categoryData.maintenanceCounts,
                            backgroundColor: "#FF82AC",
                            borderColor: "#FF82AC",
                            borderWidth: 1,
                        },
                    ],
                },
                options: {
                    ...COMMON_OPTIONS,
                    datasets: {
                        bar: { categoryPercentage: 0.9, barPercentage: 0.9, maxBarThickness: 48, borderRadius: 6 },
                    },
                    scales: {
                        x: { ticks: { font: { size: 12 } } },
                        y: { 
                            ...COMMON_OPTIONS.scales.y, 
                            max: Math.ceil(categoryData.maxCount * 1.1), 
                            ticks: { stepSize: Math.ceil(categoryData.maxCount / 5) } 
                        },
                    },
                },
            })
    );
}

// 자원 분포 현황 차트 생성
function createDistributionChart(distributionData) {
    initChart(
        "distributionChart",
        (ctx) =>
            new Chart(ctx, {
                type: "pie",
                data: {
                    labels: distributionData.labels,
                    datasets: [{
                        data: distributionData.data,
                        backgroundColor: [
                            "#F5A7A6",  // 트랙터 - 파스텔 핑크
                            "#F5CF9F",  // 콤바인 - 파스텔 오렌지
                            "#F3F5A9",  // 이앙기 - 파스텔 옐로우
                            "#D0E4EE",  // 운반차 - 파스텔 블루
                            "#D5D1E9"   // 로더 - 파스텔 퍼플
                        ],
                        borderWidth: 2,
                        borderColor: "#fff",
                        hoverOffset: 4
                    }]
                },
                options: {
                    ...PIE_COMMON_OPTIONS,
                    plugins: {
                        ...PIE_COMMON_OPTIONS.plugins,
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    const label = context.label || '';
                                    const value = context.parsed;
                                    const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                    const percentage = ((value / total) * 100).toFixed(1);
                                    return `${label}: ${value}개 (${percentage}%)`;
                                }
                            }
                        }
                    }
                }
            })
    );
}

document.addEventListener("DOMContentLoaded", () => {
    console.log("Dashboard script loaded");

    if (typeof Chart === "undefined") {
        console.error("Chart.js not loaded");
        return;
    }

    console.log("Chart.js is available");

    // AJAX로 카테고리 데이터 로드 후 차트 생성
    loadCategoryData();
    
    // AJAX로 분포 데이터 로드 후 차트 생성
    loadDistributionData();

    initChart(
        "usageChart",
        (ctx) =>
            new Chart(ctx, {
                type: "line",
                data: {
                    labels: ["2016", "2017", "2018", "2019", "2020", "2021"],
                    datasets: [
                        {
                            label: "사용량",
                            data: [15000, 22000, 35000, 28000, 18000, 32000],
                            borderColor: "#17a2b8",
                            backgroundColor: "rgba(23,162,184,0.1)",
                            borderWidth: 3,
                            fill: true,
                            tension: 0.4,
                        },
                    ],
                },
                options: {
                    ...COMMON_OPTIONS,
                    scales: {
                        y: {
                            ...COMMON_OPTIONS.scales.y,
                            max: 40000,
                            ticks: { stepSize: 10000, callback: (v) => "$" + Number(v).toLocaleString() },
                        },
                    },
                },
            })
    );

    initChart(
        "inspectionChart",
        (ctx) =>
            new Chart(ctx, {
                type: "line",
                data: {
                    labels: ["Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan"],
                    datasets: [
                        {
                            label: "점검율",
                            data: [75, 85, 70, 90, 80, 95, 88],
                            borderColor: "#007bff",
                            backgroundColor: "rgba(0,123,255,0.1)",
                            borderWidth: 3,
                            fill: true,
                            tension: 0.4,
                        },
                    ],
                },
                options: {
                    ...COMMON_OPTIONS,
                    scales: {
                        y: { ...COMMON_OPTIONS.scales.y, max: 100, ticks: { stepSize: 25, callback: (v) => v + "%" } },
                    },
                },
            })
    );

    // 파이 차트는 AJAX로 동적 로드됨 (loadDistributionData 함수에서 처리)

    // 필터 버튼(현재 주석이지만, 안전하게 closest 범위 확장)
    document.querySelectorAll(".filter-btn").forEach((btn) => {
        btn.addEventListener("click", function () {
            const section = this.closest(".first-chart-section, .second-chart-section") || document;
            section.querySelectorAll(".filter-btn").forEach((b) => b.classList.remove("active"));
            this.classList.add("active");
        });
    });
});
