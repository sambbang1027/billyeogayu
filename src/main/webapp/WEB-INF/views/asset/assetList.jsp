<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="asset-container">
    <div class="title-container">
        <h2>자산관리</h2>
    </div>

    <div class="middle-container">
        <div class="search-container">
            <div class="select-box">
                <select class="search-select">
                    <option value="all">전체</option>
                    <option value="name">종류</option>
                    <option value="type">제조사</option>
                    <option value="location">부품</option>
                </select>
            </div>
            <div class="input-box">
                <input class="search-input" type="text" placeholder="Search" />
            </div>
            <div class="search-btn">
                <img src="<c:url value='/assets/asset/search.svg'/>" alt="검색" />
            </div>
        </div>
        <div class="add-btn">등록</div>
    </div>

    <div class="filter-container">
        <div class="selection-container">
            <div class="filter-select-box">
                <div class="filter-label">종류</div>
                <select class="filter-search-select">
                    <option value="all">전체</option>
                    <option value="name">종류</option>
                    <option value="type">제조사</option>
                    <option value="location">부품</option>
                </select>
            </div>
           <div class="filter-select-box">
                <div class="filter-label">제조사</div>
                <select class="filter-search-select">
                    <option value="all">전체</option>
                    <option value="name">종류</option>
                    <option value="type">제조사</option>
                    <option value="location">부품</option>
                </select>
            </div>
           <div class="filter-select-box">
                <div class="filter-label">위치</div>
                <select class="filter-search-select">
                    <option value="all">전체</option>
                    <option value="name">종류</option>
                    <option value="type">제조사</option>
                    <option value="location">부품</option>
                </select>
            </div>
           <div class="filter-select-box">
                <div class="filter-label">상태</div>
                <select class="filter-search-select">
                    <option value="all">전체</option>
                    <option value="name">종류</option>
                    <option value="type">제조사</option>
                    <option value="location">부품</option>
                </select>
            </div>
        </div>
        <div class="filter-tag-container">
            <div class="filter-text-box">
                <div>적용된 필터</div>
                <div class="filter-text-data">2</div>
            </div>
            <div>
               <img src="<c:url value='/assets/asset/reset.svg'/>" alt="리셋" />
            </div>
             <div class="filter-tag">
                 <span class="filter-text">트랙터</span>
                 <img class="xbtn" src="<c:url value='/assets/asset/xbtn.svg'/>" alt="필터 삭제" />
             </div>
             <div class="filter-tag">
                 <span class="filter-text">사용중</span>
                 <img class="xbtn" src="<c:url value='/assets/asset/xbtn.svg'/>" alt="필터 삭제" />
             </div>
        </div>
    </div>



    <div class="list-container">
        <div class="list-middle-container">
            <div class="list-count-box">
                <div>총 개수</div>
                <div class="list-count-data">1,252</div>
                <div>건</div>
            </div>
                <div class="download-btn">
                    <img class="download-img" src="<c:url value='/assets/asset/download.svg'/>" alt="다운로드" />
                </div>
            </div>
        </div>
        <table class="asset-table">
            <thead>
                <tr>
                    <th>No</th>
                    <th>종류</th>
                    <th>제조사</th>
                    <th>모델명</th>
                    <th>부품</th>
                    <th>위치</th>
                    <th>점검 예정일</th>
                    <th>상태</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1</td>
                    <td>트랙터</td>
                    <td>kosa</td>
                    <td>MCS-102</td>
                    <td>타이어</td>
                    <td>경기도 이천시 132-11</td>
                    <td>2025.10.22</td>
                    <td>
                        <span class="status active">
                            <img src="<c:url value='/assets/asset/using.svg'/>" alt="사용중" />
                            사용중
                        </span>
                    </td>
                    <td class="table-img-container">
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/edit.svg'/>" alt="수정" /></button>
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/delete.svg'/>" alt="삭제" /></button>
                    </td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>트랙터</td>
                    <td>kosa</td>
                    <td>MCS-102</td>
                    <td>타이어</td>
                    <td>경기도 이천시 132-11</td>
                    <td>2025.10.22</td>
                    <td>
                        <span class="status active">✔ 사용중</span>
                    </td>
                    <td class="table-img-container">
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/edit.svg'/>" alt="수정" /></button>
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/delete.svg'/>" alt="삭제" /></button>
                    </td>
                </tr>
                <tr>
                    <td>1</td>
                    <td>트랙터</td>
                    <td>kosa</td>
                    <td>MCS-101</td>
                    <td>엔진</td>
                    <td>경기도 수원시 45-12</td>
                    <td>2025.09.10</td>
                    <td>
                        <span class="status active">✔ 사용중</span>
                    </td>
                    <td class="table-img-container">
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/edit.svg'/>" alt="수정" /></button>
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/delete.svg'/>" alt="삭제" /></button>
                    </td>
                </tr>
                <tr>
                    <td>2</td>
                    <td>트랙터</td>
                    <td>kosa</td>
                    <td>MCS-102</td>
                    <td>타이어</td>
                    <td>경기도 이천시 132-11</td>
                    <td>2025.10.22</td>
                    <td>
                        <span class="status active">✔ 사용중</span>
                    </td>
                    <td class="table-img-container">
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/edit.svg'/>" alt="수정" /></button>
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/delete.svg'/>" alt="삭제" /></button>
                    </td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>콤바인</td>
                    <td>LG</td>
                    <td>CB-210</td>
                    <td>커터날</td>
                    <td>충청북도 청주시 23-89</td>
                    <td>2026.01.15</td>
                    <td>
                        <span class="status active">✔ 사용중</span>
                    </td>
                    <td class="table-img-container">
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/edit.svg'/>" alt="수정" /></button>
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/delete.svg'/>" alt="삭제" /></button>
                    </td>
                </tr>
                <tr>
                    <td>4</td>
                    <td>경운기</td>
                    <td>Daewoo</td>
                    <td>DW-500</td>
                    <td>체인</td>
                    <td>전라남도 순천시 77-45</td>
                    <td>2024.12.01</td>
                    <td>
                        <span class="status active">✔ 사용중</span>
                    </td>
                    <td class="table-img-container">
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/edit.svg'/>" alt="수정" /></button>
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/delete.svg'/>" alt="삭제" /></button>
                    </td>
                </tr>
                <tr>
                    <td>5</td>
                    <td>트랙터</td>
                    <td>Hyundai</td>
                    <td>HY-700</td>
                    <td>브레이크</td>
                    <td>강원도 원주시 56-22</td>
                    <td>2025.11.30</td>
                    <td>
                        <span class="status active">✔ 사용중</span>
                    </td>
                    <td class="table-img-container">
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/edit.svg'/>" alt="수정" /></button>
                        <button class="icon-btn"><img src="<c:url value='/assets/asset/delete.svg'/>" alt="삭제" /></button>
                    </td>
                </tr>

            </tbody>
        </table>
    </div>

    <div class="pagination">
        <div class="page-img-box">
            <img src="<c:url value='/assets/asset/left.svg'/>" alt="이전" />
        </div>
        <span class="page-num">1</span>
        <span class="page-num">2</span>
        <span class="page-num active">3</span>
        <span class="page-num">4</span>
        <span class="page-num">5</span>
        <div class="page-img-box">
            <img src="<c:url value='/assets/asset/right.svg'/>" alt="다음" />
        </div>
    </div>
</div>

