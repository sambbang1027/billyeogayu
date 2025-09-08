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

document.addEventListener("DOMContentLoaded", () => {
    console.log("Dashboard script loaded");

    if (typeof Chart === "undefined") {
        console.error("Chart.js not loaded");
        return;
    }

    console.log("Chart.js is available");

    initChart(
        "resourceTypeChart",
        (ctx) =>
            new Chart(ctx, {
                type: "bar",
                // TODO: data 연결
                data: {
                    labels: ["트랙터", "운반차", "논두렁 조성기", "이앙기", "로더", "안녕"],
                    datasets: [
                        {
                            label: "총 자원수",
                            data: [45, 38, 28, 42, 35, 50],
                            backgroundColor: "#92BEA9",
                            borderColor: "#92BEA9",
                            borderWidth: 1,
                        },
                        {
                            label: "점검 필요",
                            data: [8, 12, 5, 15, 7, 34],
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
                        x: { ticks: { font: { size: 12 } } }, // 축 글꼴 명시(선택)
                        y: { ...COMMON_OPTIONS.scales.y, max: 50, ticks: { stepSize: 10 } },
                    },
                },
            })
    );

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

    // 필터 버튼(현재 주석이지만, 안전하게 closest 범위 확장)
    document.querySelectorAll(".filter-btn").forEach((btn) => {
        btn.addEventListener("click", function () {
            const section = this.closest(".first-chart-section, .second-chart-section") || document;
            section.querySelectorAll(".filter-btn").forEach((b) => b.classList.remove("active"));
            this.classList.add("active");
        });
    });
});
