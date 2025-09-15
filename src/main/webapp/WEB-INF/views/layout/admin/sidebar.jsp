<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
         <link rel="stylesheet" href="<c:url value='/static/css/layout/admin/sidebar/style.css'/>">
        <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    </head>
    <body>
        <div class="logo-box">
                <img class="logo" src="<c:url value='/assets/layout/admin/logo.svg'/>"
                alt="로고"
                style="max-width:100%; height:auto;" />
        </div>
        <div class="navi-container">
            <div class="dashboard-container ${activePage eq 'dashboard' ? 'active' : ''}">
                <div class="select-img-box">
                    <c:if test="${activePage eq 'dashboard'}">
                        <img class="select-img"
                             src="<c:url value='/assets/layout/admin/selected.svg'/>"
                             alt="선택" />
                    </c:if>
                </div>
                <div class="img-box">
                    <img class="dashboard-img"
                         src="<c:url value='/assets/layout/admin/${activePage eq "dashboard" ? "dashboard-select.svg" : "dashboard.svg"}'/>"
                         alt="대시보드" />
                </div>
                <div class="dashboard-textbox">대시보드</div>
            </div>

            <div class="asset-container ${activePage eq 'asset' ? 'active' : ''}">
                <div class="select-img-box">
                    <c:if test="${activePage eq 'asset'}">
                        <img class="select-img"
                             src="<c:url value='/assets/layout/admin/selected.svg'/>"
                             alt="선택" />
                    </c:if>
                </div>
                <div class="img-box">
                    <img class="asset-img"
                         src="<c:url value='/assets/layout/admin/${activePage eq "asset" ? "asset-select.svg" : "asset.svg"}'/>"
                         alt="자산관리" />
                </div>
                <div class="asset-textbox">자산관리</div>
            </div>

            <div class="maintenance-container ${activePage eq 'maintenance' ? 'active' : ''}">
                <div class="select-img-box">
                    <c:if test="${activePage eq 'maintenance'}">
                        <img class="select-img"
                             src="<c:url value='/assets/layout/admin/selected.svg'/>"
                             alt="선택" />
                    </c:if>
                </div>
                <div class="img-box">
                    <img class="maintenance-img"
                         src="<c:url value='/assets/layout/admin/${activePage eq "maintenance" ? "maintenance-select.svg" : "maintenance.svg"}'/>"
                         alt="점검관리" />
                </div>
                <div class="maintenance-textbox">점검관리</div>
            </div>

            <div class="reservation-container ${activePage eq 'reservation' ? 'active' : ''}">
                <div class="select-img-box">
                    <c:if test="${activePage eq 'reservation'}">
                        <img class="select-img"
                             src="<c:url value='/assets/layout/admin/selected.svg'/>"
                             alt="선택" />
                    </c:if>
                </div>
                <div class="img-box">
                    <img class="reservation-img"
                         src="<c:url value='/assets/layout/admin/${activePage eq "reservation" ? "reservation-select.svg" : "reservation.svg"}'/>"
                         alt="예약관리" />
                </div>
                <div class="reservation-textbox">예약관리</div>
            </div>
        </div>

        <script>
        $(document).ready(function() {
            $(".navi-container > div").on("click", function() {
                if ($(this).hasClass("dashboard-container")) {
                    window.location.href = "<c:url value='/admin/dashboard'/>";
                } else if ($(this).hasClass("asset-container")) {
                    window.location.href = "<c:url value='/admin/asset'/>";
                } else if ($(this).hasClass("maintenance-container")) {
                    window.location.href = "<c:url value='/admin/maintenance'/>";
                } else if ($(this).hasClass("reservation-container")) {
                    window.location.href = "<c:url value='/admin/reservations'/>";
                }
            });

            $(".logo").on("click", function() {
                window.location.href = "<c:url value='/admin/dashboard'/>";
            });
        });
        </script>
    </body>
</html>