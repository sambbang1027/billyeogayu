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
    
    fetch('/admin/dashboard/categoryData')
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
    
    
    fetch('/admin/dashboard/distributionData')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(distributionData => {
            console.log('Distribution data loaded:', distributionData);
            createDistributionChart(distributionData);
        })
        .catch(error => {
            console.error('Error loading distribution data:', error);
            showErrorMessage("distributionChart", "데이터를 불러오는데 실패했습니다.");
        });
}

// AJAX로 사용량 차트 필터 데이터 로드
function loadUsageFilters() {
    console.log("Loading usage filters via AJAX...");
    
    fetch('/admin/dashboard/usageFilters')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(filterData => {
            console.log('Usage filters loaded:', filterData);
            renderUsageFilters(filterData);
        })
        .catch(error => {
            console.error('Error loading usage filters:', error);
        });
}

// 사용량 차트 필터 렌더링
function renderUsageFilters(filterData) {
    const usageFilters = document.querySelector('.asset-usage .chart-filters');
    if (!usageFilters) return;
    
    const categoryModels = filterData.categoryModels;
    const addresses = filterData.addresses;
    
    // 드롭다운 HTML 생성
    usageFilters.innerHTML = `
        <div class="filter-dropdown">
            <select id="categoryFilter" class="filter-select">
                <option value="">전체 카테고리</option>
                ${Object.keys(categoryModels).map(cat => 
                    `<option value="${cat}">${cat}</option>`
                ).join('')}
            </select>
        </div>
        <div class="filter-dropdown">
            <select id="modelFilter" class="filter-select">
                <option value="">전체 모델</option>
            </select>
        </div>
        <div class="filter-dropdown">
            <select id="addressFilter" class="filter-select">
                <option value="">전체 지역</option>
                ${addresses.map(addr => 
                    `<option value="${addr}">${addr}</option>`
                ).join('')}
            </select>
        </div>
    `;
    
    // 카테고리-모델 연동 설정
    setupCategoryModelFilter(categoryModels);
    setupUsageFilterEvents();
}

// 카테고리별 모델 필터 연동
function setupCategoryModelFilter(categoryModels) {
    const categoryFilter = document.getElementById('categoryFilter');
    const modelFilter = document.getElementById('modelFilter');
    
    if (!categoryFilter || !modelFilter) return;
    
    categoryFilter.addEventListener('change', function() {
        const selectedCategory = this.value;
        
        // 선택된 카테고리에 해당하는 모델들만 표시
        if (selectedCategory && categoryModels[selectedCategory]) {
            const models = categoryModels[selectedCategory];
            modelFilter.innerHTML = '<option value="">전체 모델</option>' +
                models.map(model => `<option value="${model}">${model}</option>`).join('');
        } else {
            // 전체 카테고리 선택 시 모든 모델 표시
            const allModels = Object.values(categoryModels).flat();
            modelFilter.innerHTML = '<option value="">전체 모델</option>' +
                allModels.map(model => `<option value="${model}">${model}</option>`).join('');
        }
        
        // 모델 필터 초기화
        modelFilter.value = '';
        
        // 차트 데이터 로드
        loadUsageChartData();
    });
}

// 사용량 차트 필터 이벤트 설정
function setupUsageFilterEvents() {
    const categoryFilter = document.getElementById('categoryFilter');
    const modelFilter = document.getElementById('modelFilter');
    const addressFilter = document.getElementById('addressFilter');
    
    if (categoryFilter) {
        categoryFilter.addEventListener('change', loadUsageChartData);
    }
    if (modelFilter) {
        modelFilter.addEventListener('change', loadUsageChartData);
    }
    if (addressFilter) {
        addressFilter.addEventListener('change', loadUsageChartData);
    }
}

// 사용량 차트 데이터 로드 (필터 적용)
function loadUsageChartData() {
    const category = document.getElementById('categoryFilter')?.value || '';
    const model = document.getElementById('modelFilter')?.value || '';
    const address = document.getElementById('addressFilter')?.value || '';
    
    console.log('Loading usage chart data with filters:', { category, model, address });
    
    fetch(`/admin/dashboard/usageData?category=${encodeURIComponent(category)}&model=${encodeURIComponent(model)}&address=${encodeURIComponent(address)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(usageData => {
            console.log('Usage data loaded:', usageData);
            updateUsageChart(usageData);
        })
        .catch(error => {
            console.error('Error loading usage data:', error);
        });
}

function updateUsageChart(usageData) {
    const chart = Chart.getChart("usageChart");
    if (chart) {
        chart.data.labels = usageData.labels;
        chart.data.datasets[0].data = usageData.data;
        
        // Y축 최대값을 데이터에 맞게 동적 조정
        const maxValue = Math.max(...usageData.data);
        const adjustedMax = Math.ceil(maxValue * 1.2); // 20% 여유분 추가
        const stepSize = Math.ceil(adjustedMax / 5); // 5단계로 나누기
        
        chart.options.scales.y.max = adjustedMax;
        chart.options.scales.y.ticks.stepSize = stepSize;
        
        chart.update();
    }
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

// 에러 메시지 표시
function showErrorMessage(chartId, message) {
    const canvas = document.getElementById(chartId);
    if (!canvas) return;
    
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    // 에러 메시지 그리기
    ctx.fillStyle = '#666';
    ctx.font = '14px Arial';l
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
    
    // AJAX로 사용량 차트 필터 로드
    loadUsageFilters();
    
    // AJAX로 점검 차트 필터 로드
    loadInspectionFilters();
    
    // 초기 사용량 차트 데이터 로드
    loadUsageChartData();
    
    // 초기 점검 차트 데이터 로드
    loadInspectionChartData();

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
                            max: 100, // 초기값 설정 (데이터 로드 시 동적으로 조정됨)
                            ticks: { 
                                stepSize: 20, 
                                callback: (v) => Number(v).toLocaleString() + "건" 
                            },
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
                            label: "점검 수",
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
                        y: { ...COMMON_OPTIONS.scales.y, max: 100, ticks: { stepSize: 20, callback: (v) => Number(v).toLocaleString() + "건" } },
                    },
                },
            })
    );

    // 파이 차트는 AJAX로 동적 로드됨 (loadDistributionData 함수에서 처리)
})

// AJAX로 점검 차트 필터 데이터 로드
function loadInspectionFilters() {  
    console.log("Loading inspection filters via AJAX...");
    
    fetch('/admin/dashboard/inspectionFilters')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(filterData => {
            console.log('Inspection filters loaded:', filterData);
            renderInspectionFilters(filterData);
        })
        .catch(error => {
            console.error('Error loading inspection filters:', error);
        });
}

// 점검 차트 필터 렌더링
function renderInspectionFilters(filterData) {
    const inspectionFilters = document.querySelector('.asset-maintenance .chart-filters');
    if (!inspectionFilters) return;
    
    const categoryModels = filterData.categoryModels;
    
    // 드롭다운 HTML 생성 (주소 필터 제외)
    inspectionFilters.innerHTML = `
        <div class="filter-dropdown">
            <select id="inspectionCategoryFilter" class="filter-select">
                <option value="">전체 카테고리</option>
                ${Object.keys(categoryModels).map(cat => 
                    `<option value="${cat}">${cat}</option>`
                ).join('')}
            </select>
        </div>
        <div class="filter-dropdown">
            <select id="inspectionModelFilter" class="filter-select">
                <option value="">전체 모델</option>
            </select>
        </div>
    `;
    
    // 카테고리-모델 연동 설정
    setupInspectionCategoryModelFilter(categoryModels);
    setupInspectionFilterEvents();
}

// 점검 차트 카테고리별 모델 필터 연동
function setupInspectionCategoryModelFilter(categoryModels) {
    const categoryFilter = document.getElementById('inspectionCategoryFilter');
    const modelFilter = document.getElementById('inspectionModelFilter');
    
    if (!categoryFilter || !modelFilter) return;
    
    categoryFilter.addEventListener('change', function() {
        const selectedCategory = this.value;
        
        // 선택된 카테고리에 해당하는 모델들만 표시
        if (selectedCategory && categoryModels[selectedCategory]) {
            const models = categoryModels[selectedCategory];
            modelFilter.innerHTML = '<option value="">전체 모델</option>' +
                models.map(model => `<option value="${model}">${model}</option>`).join('');
        } else {
            // 전체 카테고리 선택 시 모든 모델 표시
            const allModels = Object.values(categoryModels).flat();
            modelFilter.innerHTML = '<option value="">전체 모델</option>' +
                allModels.map(model => `<option value="${model}">${model}</option>`).join('');
        }
        
        // 모델 필터 초기화
        modelFilter.value = '';
        
        // 차트 데이터 로드
        loadInspectionChartData();
    });
}

// 점검 차트 필터 이벤트 설정
function setupInspectionFilterEvents() {
    const categoryFilter = document.getElementById('inspectionCategoryFilter');
    const modelFilter = document.getElementById('inspectionModelFilter');
    
    if (categoryFilter) {
        categoryFilter.addEventListener('change', loadInspectionChartData);
    }
    if (modelFilter) {
        modelFilter.addEventListener('change', loadInspectionChartData);
    }
}

// 점검 차트 데이터 로드 (필터 적용)
function loadInspectionChartData() {
    const category = document.getElementById('inspectionCategoryFilter')?.value || '';
    const model = document.getElementById('inspectionModelFilter')?.value || '';
    
    console.log('Loading inspection chart data with filters:', { category, model });
    
    fetch(`/admin/dashboard/inspectionData?category=${encodeURIComponent(category)}&model=${encodeURIComponent(model)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(inspectionData => {
            console.log('Inspection data loaded:', inspectionData);
            updateInspectionChart(inspectionData);
        })
        .catch(error => {
            console.error('Error loading inspection data:', error);
        });
}

function updateInspectionChart(inspectionData) {
    const chart = Chart.getChart("inspectionChart");
    if (chart) {
        chart.data.labels = inspectionData.labels;
        chart.data.datasets[0].data = inspectionData.data;
        
        // Y축 최대값을 데이터에 맞게 동적 조정
        const maxValue = Math.max(...inspectionData.data);
        const adjustedMax = Math.ceil(maxValue * 1.2); // 20% 여유분 추가
        const stepSize = Math.ceil(adjustedMax / 5); // 5단계로 나누기
        
        chart.options.scales.y.max = adjustedMax;
        chart.options.scales.y.ticks.stepSize = stepSize;
        chart.options.scales.y.ticks.callback = (v) => Number(v).toLocaleString() + "건";
        
        chart.update();
    }
}

    // 필터 버튼(현재 주석이지만, 안전하게 closest 범위 확장)
    document.querySelectorAll(".filter-btn").forEach((btn) => {
        btn.addEventListener("click", function () {
            const section = this.closest(".first-chart-section, .second-chart-section") || document;
            section.querySelectorAll(".filter-btn").forEach((b) => b.classList.remove("active"));
            this.classList.add("active");
    });
});
