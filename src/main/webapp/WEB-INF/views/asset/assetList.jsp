<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

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
        <div class="add-btn" id="openRegisterModal">등록</div>
    </div>

    <div class="filter-container">
        <div class="selection-container">
            <div class="filter-select-box">
                <div class="filter-label">종류</div>
                    <select class="filter-search-select" id="filter-category">
                        <option value="all">전체</option>
                          <c:forEach var="cat" items="${categories}">
                            <option value="${cat}">${cat}</option>
                          </c:forEach>
                    </select>
                </div>
           <div class="filter-select-box">
                <div class="filter-label">제조사</div>
                <select class="filter-search-select" id="filter-company">
                    <option value="all">전체</option>
                      <c:forEach var="com" items="${companies}">
                        <option value="${com}">${com}</option>
                      </c:forEach>
                </select>
            </div>
           <div class="filter-select-box">
                <div class="filter-label">위치</div>
                <select class="filter-search-select" id="filter-location">
                    <option value="all">전체</option>
                      <c:forEach var="loc" items="${locations}">
                        <option value="${loc}">${loc}</option>
                      </c:forEach>
                </select>
            </div>
           <div class="filter-select-box">
                <div class="filter-label">상태</div>
                <select class="filter-search-select" id="filter-status">
                  <option value="all">전체</option>
                  <option value="AVAILABLE">사용가능</option>
                  <option value="USING">사용중</option>
                  <option value="MAINTENANCE_REQUIRED">정비필요</option>
                  <option value="MAINTAINING">정비중</option>
                </select>
            </div>
        </div>
       <c:set var="appliedCount" value="${
         (empty paramValues.category    ? 0 : fn:length(paramValues.category)) +
         (empty paramValues.company     ? 0 : fn:length(paramValues.company)) +
         (empty paramValues.location    ? 0 : fn:length(paramValues.location)) +
         (empty paramValues.assetStatus ? 0 : fn:length(paramValues.assetStatus))
       }"/>

       <div class="filter-tag-container" id="filter-tags">
         <div class="filter-text-box">
           <div>적용된 필터</div>
           <div class="filter-text-data">${appliedCount}</div>
         </div>

         <button type="button" id="resetFiltersBtn">
           <img class="reset-btn" src="<c:url value='/assets/asset/reset.svg'/>" alt="리셋" />
         </button>

         <!-- 종류 -->
         <c:forEach var="v" items="${paramValues.category}">
           <div class="filter-tag" data-key="category" data-label="${v}">
             <span class="filter-text">${v}</span>
             <img class="xbtn" src="<c:url value='/assets/asset/xbtn.svg'/>" alt="필터 삭제" />
           </div>
         </c:forEach>

         <!-- 제조사 -->
         <c:forEach var="v" items="${paramValues.company}">
           <div class="filter-tag" data-key="company" data-label="${v}">
             <span class="filter-text">${v}</span>
             <img class="xbtn" src="<c:url value='/assets/asset/xbtn.svg'/>" alt="필터 삭제" />
           </div>
         </c:forEach>

         <!-- 위치(컬럼 없으면 보여만 주고 SQL은 무시) -->
         <c:forEach var="v" items="${paramValues.location}">
           <div class="filter-tag" data-key="location" data-label="${v}">
             <span class="filter-text">${v}</span>
             <img class="xbtn" src="<c:url value='/assets/asset/xbtn.svg'/>" alt="필터 삭제" />
           </div>
         </c:forEach>

         <!-- 상태(라벨 매핑) -->
         <c:forEach var="v" items="${paramValues.assetStatus}">
           <c:set var="statusLabel"
                  value="${v=='AVAILABLE'?'사용가능':(v=='USING'?'사용중':(v=='MAINTENANCE_REQUIRED'?'정비필요':(v=='MAINTAINING'?'정비중':v)))}"/>
           <div class="filter-tag" data-key="assetStatus" data-label="${v}">
             <span class="filter-text">${statusLabel}</span>
             <img class="xbtn" src="<c:url value='/assets/asset/xbtn.svg'/>" alt="필터 삭제" />
           </div>
         </c:forEach>
       </div>
    </div>

    <div class="list-container">
        <div class="list-middle-container">
            <div class="list-count-box">
                <div>총 개수</div>
                <div class="list-count-data">
                    <fmt:formatNumber value="${totalCount}" pattern="#,###"/>
                </div>
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
                    <th>사용시간</th>
                    <th>위치</th>
                    <th>점검 예정일</th>
                    <th>상태</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:set var="rowBase" value="${(page - 1) * pageSize}" />
                <c:forEach var="asset" items="${assets}" varStatus="status">
                    <tr data-id="${asset.assetId}">
                        <td>${rowBase + status.index + 1}</td>
                        <td>${asset.category}</td>
                        <td>${asset.company}</td>
                        <td>${asset.modelName}</td>
                        <td>${asset.usageTime}시간</td>
                        <td>농기계공사</td> <!-- 추후에 값 바꿔야함 -->
                        <td>
                          <c:choose>
                            <c:when test="${not empty asset.expectedMaintenanceDate}">
                              <c:out value="${fn:replace(fn:substringBefore(asset.expectedMaintenanceDate, 'T'), '-', '.')}"/>
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <c:set var="sv" value="${fn:toUpperCase(asset.assetStatus)}"/>
                          <c:choose>
                            <c:when test="${sv == 'AVAILABLE'}">
                                <div class="status AVAILABLE">
                                  <img src="<c:url value='/assets/asset/canuse.svg'/>" alt="사용가능" />
                                  <span>사용가능</span>
                                </div>
                            </c:when>
                            <c:when test="${sv == 'USING'}">
                                <div class="status USING">
                                  <img src="<c:url value='/assets/asset/maintain.svg'/>" alt="사용중" />
                                  <span>사용중</span>
                                </div>
                            </c:when>
                            <c:when test="${sv == 'MAINTAINING' || sv == 'MAINTENANCE_REQUIRED'}">
                              <div class="status UNAVAILABLE">
                                <img src="<c:url value='/assets/asset/using.svg'/>" alt="사용불가" />
                                <span>사용불가</span>
                              </div>
                            </c:when>
                            <c:otherwise>
                              <span class="status UNAVAILABLE">${asset.assetStatus}</span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td class="table-img-container">
                            <button type="button" class="detail-asset-btn">
                                <img src="<c:url value='/assets/asset/detail.svg'/>" alt="상세" />
                            </button>

                            <button type="button" class="edit-asset-btn">
                                <img src="<c:url value='/assets/asset/edit.svg'/>" alt="수정" />
                            </button>

                            <form action="/admin/asset/delete/${asset.assetId}" method="post" style="display:inline;">
                                <input type="hidden" name="deletedBy" value="admin" />
                                <button type="submit" class="icon-btn">
                                    <img src="<c:url value='/assets/asset/delete.svg'/>" alt="삭제" />
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

<c:set var="pageSize"  value="${empty pageSize ? 10 : pageSize}" />
<c:set var="blockSize" value="5" />

<fmt:parseNumber var="totalPages" value="${(totalCount + pageSize - 1) / pageSize}" integerOnly="true" />
<c:if test="${totalPages lt 1}">
  <c:set var="totalPages" value="1"/>
</c:if>

<fmt:parseNumber var="currentPage" value="${page}" integerOnly="true" />
<c:if test="${empty currentPage or currentPage lt 1}">
  <c:set var="currentPage" value="1" />
</c:if>
<c:if test="${currentPage gt totalPages}">
  <c:set var="currentPage" value="${totalPages}" />
</c:if>

<fmt:parseNumber var="blockIndex" value="${(currentPage - 1) / blockSize}" integerOnly="true" />
<fmt:parseNumber var="startPage" value="${blockIndex * blockSize + 1}" integerOnly="true" />
<c:set var="endPageRaw" value="${startPage + blockSize - 1}" />
<c:if test="${endPageRaw > totalPages}">
  <c:set var="endPageRaw" value="${totalPages}" />
</c:if>
<fmt:parseNumber var="endPage" value="${endPageRaw}" integerOnly="true" />

<fmt:parseNumber var="prevPage" value="${currentPage - 1}" integerOnly="true" />
<fmt:parseNumber var="nextPage" value="${currentPage + 1}" integerOnly="true" />

<div class="pagination">
 <c:choose>
   <c:when test="${currentPage le 1}">
     <div class="page-img-box disabled" aria-disabled="true" role="button">
       <img src="<c:url value='/assets/asset/left.svg'/>" alt="" aria-hidden="true" />
     </div>
   </c:when>
   <c:otherwise>
        <c:url var="prevUrl" value="/admin/asset/list">
          <c:param name="page" value="${prevPage}"/>
          <c:forEach var="v" items="${paramValues.category}"><c:param name="category" value="${v}"/></c:forEach>
          <c:forEach var="v" items="${paramValues.company}"><c:param name="company" value="${v}"/></c:forEach>
          <c:forEach var="v" items="${paramValues.location}"><c:param name="location" value="${v}"/></c:forEach>
          <c:forEach var="v" items="${paramValues.assetStatus}"><c:param name="assetStatus" value="${v}"/></c:forEach>
          <c:if test="${not empty param.field}"><c:param name="field" value="${param.field}"/></c:if>
          <c:if test="${not empty param.keyword}"><c:param name="keyword" value="${param.keyword}"/></c:if>
          <c:if test="${not empty pageSize}"><c:param name="pageSize" value="${pageSize}"/></c:if>
        </c:url>
     <a class="page-img-box" href="${prevUrl}" aria-label="이전 페이지">
       <img src="<c:url value='/assets/asset/left.svg'/>" alt="이전" />
     </a>
   </c:otherwise>
 </c:choose>

  <c:forEach var="i" begin="${startPage}" end="${endPage}">
    <c:url var="pageUrl" value="/admin/asset/list">
      <c:param name="page" value="${i}"/>
      <c:forEach var="v" items="${paramValues.category}"><c:param name="category" value="${v}"/></c:forEach>
      <c:forEach var="v" items="${paramValues.company}"><c:param name="company" value="${v}"/></c:forEach>
      <c:forEach var="v" items="${paramValues.location}"><c:param name="location" value="${v}"/></c:forEach>
      <c:forEach var="v" items="${paramValues.assetStatus}"><c:param name="assetStatus" value="${v}"/></c:forEach>
      <c:if test="${not empty param.field}"><c:param name="field" value="${param.field}"/></c:if>
      <c:if test="${not empty param.keyword}"><c:param name="keyword" value="${param.keyword}"/></c:if>
      <c:if test="${not empty pageSize}"><c:param name="pageSize" value="${pageSize}"/></c:if>
    </c:url>
    <c:choose>
      <c:when test="${i == currentPage}">
        <span class="page-num active" aria-current="page">${i}</span>
      </c:when>
      <c:otherwise>
        <a class="page-num" href="${pageUrl}">${i}</a>
      </c:otherwise>
    </c:choose>
  </c:forEach>

  <c:choose>
    <c:when test="${currentPage ge totalPages}">
      <!-- 클릭 불가하지만 자리 유지 -->
      <div class="page-img-box disabled" aria-disabled="true" role="button">
        <img src="<c:url value='/assets/asset/right.svg'/>" alt="" aria-hidden="true" />
      </div>
    </c:when>
    <c:otherwise>
      <c:url var="nextUrl" value="/admin/asset/list">
        <c:param name="page" value="${nextPage}"/>
          <c:forEach var="v" items="${paramValues.category}"><c:param name="category" value="${v}"/></c:forEach>
          <c:forEach var="v" items="${paramValues.company}"><c:param name="company" value="${v}"/></c:forEach>
          <c:forEach var="v" items="${paramValues.location}"><c:param name="location" value="${v}"/></c:forEach>
          <c:forEach var="v" items="${paramValues.assetStatus}"><c:param name="assetStatus" value="${v}"/></c:forEach>
          <c:if test="${not empty param.field}"><c:param name="field" value="${param.field}"/></c:if>
          <c:if test="${not empty param.keyword}"><c:param name="keyword" value="${param.keyword}"/></c:if>
          <c:if test="${not empty pageSize}"><c:param name="pageSize" value="${pageSize}"/></c:if>
          </c:url>
          <a class="page-img-box" href="${nextUrl}" aria-label="다음 페이지">
            <img src="<c:url value='/assets/asset/right.svg'/>" alt="다음" />
          </a>
    </c:otherwise>
  </c:choose>

</div>



<!-- 자산등록 모달 -->
<div class="modal" id="assetRegisterModal">
    <div class="modal-content">
        <div class="modal-title-container">
            <h2 class="modal-title">자산등록</h2>
        </div>

        <div class="asset-info-container">
            <div class="asset-info-top-container">
                 <div class="modal-category-container">
                        <label>종류</label>
                        <input class="modal-input" placeholder="전체" />
                    </div>

                    <div class="modal-company-container">
                        <label>제조사</label>
                        <input class="modal-input" placeholder="전체"/>
                    </div>

                    <div class="modal-model-container">
                        <label>모델</label>
                        <input class="modal-input" placeholder="전체" />
                    </div>
                </div>

                 <div class="asset-info-bottom-container">
                     <div class="modal-lifecycle-container">
                            <label>점검주기</label>
                            <input class="modal-input" placeholder="ex) 1일, 1개월, 1년" />
                        </div>

                        <div class="modal-image-container">
                            <div class="modal-image-box">
                                <label>사진첨부</label>
                                <img id="openImagePicker"
                                     src="<c:url value='/assets/asset/plus.svg'/>"
                                     alt="추가" style="cursor:pointer" />
                            </div>

                            <input class="modal-image-input" id="assetImageName" placeholder="파일을 선택하세요" readonly />
                            <!-- 서버가 돌려준 접근 URL 저장(필요 시 폼 제출할 값) -->
                            <input type="hidden" id="assetImageUrl" name="imageUrl" />

                            <!-- 실제 파일 인풋(숨김) -->
                            <input type="file" id="assetImageInput" accept="image/*" style="display:none" />
                        </div>
                </div>
        </div>

        <div class="modal-part-container">
            <div class="modal-part-title">
                <h3>부품 정보</h3>
            </div>
            <div class="modal-part-add-container" id="addPartBtn">
               <img src="<c:url value='/assets/asset/plus.svg'/>" alt="추가" />
               <div>부품 추가</div>
            </div>
            <div class="modal-part-list" id="partList">

                <div class="modal-part-info-container">
                    <div class="modal-part-category-container">
                        <label>종류</label>
                        <input class="modal-part-input" placeholder="전체" />
                    </div>
                    <div class="modal-part-lifecycle-container">
                        <div>점검주기</div>
                        <input class="modal-part-lifecycle-input" placeholder="ex) 1일, 1개월, 1년" />
                    </div>
                </div>
            </div>
        </div>

        <div class="modal-btn-container">
            <div class="confirm-btn">등록</div>
            <div class="cancel-btn" id="cancelRegisterBtn">취소</div>
        </div>
    </div>
</div>

<!-- 자산상세 모달 -->
<div class="detail-modal">
    <div class="detail-modal-content">
        <div class="modal-title-container">
            <h2 class="modal-title">자산상세</h2>
        </div>
        <div class="detail-info-wrapper">
            <div class="detail-info-container">
                <div>
                    <img class="detail-image" data-field="imagePath" alt="자산 이미지" />
                </div>
                <div class="detail-info-list">
                    <div class="detail-info-box">
                        <label>종류</label>
                        <div data-field="category"></div>
                    </div>
                    <div class="detail-info-box">
                        <label>모델</label>
                        <div data-field="model"></div>
                    </div>
                    <div class="detail-info-box">
                        <label>제조사</label>
                        <div data-field="company"></div>
                    </div>
                    <div class="detail-info-box">
                        <label>사용기간</label>
                        <div data-field="usageTime"></div>
                    </div>
                    <div class="detail-info-box">
                        <label>점검주기</label>
                        <div data-field="maintenanceCycle"></div>
                    </div>
                    <div class="detail-info-box">
                        <label>점검예정일</label>
                        <div data-field="expectedDate"></div>
                    </div>
                    <div class="detail-info-box">
                        <label>최근점검일</label>
                        <div data-field="lastDate"></div>
                    </div>
                </div>
            </div>
        </div>


        <div class="detail-part-container">
          <div class="detail-part-title-box">
            <h3>부품</h3>
          </div>

          <c:forEach var="part" items="${parts}">
            <div class="detail-part-info-container collapsed">
              <div class="detail-part-info-title">
                <strong>${part.name}</strong>
                <span class="subtitle">종류</span>
              </div>

              <!-- Body (처음에는 숨김) -->
              <div class="detail-part-info-body">
                <div class="detail-part-top-container">
                  <div>
                    <label>점검주기</label>
                    <div>${formatHoursToYMDH(part.maintenanceCycle)}</div>
                  </div>
                  <div>
                    <label>사용시간</label>
                    <div>${part.usageTime}</div>
                  </div>
                </div>
                <div class="detail-part-bottom-container">
                  <div>
                    <label>점검예정일</label>
                    <div>${part.expectedDate}</div>
                  </div>
                  <div>
                    <label>최근점검일</label>
                    <div>${part.lastDate}</div>
                  </div>
                </div>
              </div>

              <div class="detail-part-image-box">
                <img src="<c:url value='/assets/asset/down.svg'/>"
                     alt="toggle" class="toggle-icon"/>
              </div>
            </div>
          </c:forEach>

          <div class="detail-part-btn">
            <div class="detail-part-request-btn">점검요청</div>
            <div class="detail-part-cancel">닫기</div>
          </div>
       </div>
    </div>
</div>


<!-- 자산수정 모달 -->
<div class="edit-modal" id="assetEditModal">
  <div class="edit-modal-content">
    <div class="modal-title-container">
      <h2 class="modal-title">자산수정</h2>
    </div>
    <div class="edit-info-wrapper">
        <div class="edit-info-container">
            <div class="edit-info-top-container">
                <div class="edit-info-img-box">
                    <img class="edit-image" data-edit-field="imagePath" alt="자산 사진">
                </div>
                <div class="edit-info-list">
                   <div class="edit-info-box"><label>종류</label><div data-edit-field="category"></div></div>
                   <div class="edit-info-box"><label>모델</label><div data-edit-field="model"></div></div>
                   <div class="edit-info-box"><label>제조사</label><div data-edit-field="company"></div></div>
                   <div class="edit-info-box"><label>사용시간</label><div data-edit-field="usageTime"></div></div>
                   <div class="edit-info-box"><label>점검예정일</label><div data-edit-field="expectedDate"></div></div>
                   <div class="edit-info-box"><label>최근점검일</label><div data-edit-field="lastDate"></div></div>
                </div>
            </div>

            <div class="edit-info-bottom-container">
                <div class="edit-info-bottom-left-box" id="editImagePicker">
                    <label class="edit-image-label">사진변경</label>
                    <div class="edit-image-div"><img src="<c:url value='/assets/asset/edit-img.svg'/>" alt="사진 변경" /></div>
                </div>

                <input type="file" id="editImageInput" accept="image/*" style="display:none" />
                <input type="hidden" id="editImageUrl" name="imageUrl" />

                <div class="edit-info-bottom-right-box">
                    <label>점검주기</label>
                    <input class="edit-info-input" data-edit-input="maintenanceCycle" placeholder="ex) 1일, 1개월, 1년" />
                </div>
            </div>
        </div>
    </div>

    <div class="edit-part-container">
        <div class="edit-part-title-box">
            <h3>부품</h3>
            <div class="edit-part-add-box">
                <img src="<c:url value='/assets/asset/plus.svg'/>" alt="추가" />
                <label>부품추가</label>
            </div>
        </div>
        <c:forEach var="part" items="${parts}">
            <div class="edit-part-info-container collapsed">
              <div class="edit-part-info-title">
                <strong>${part.name}</strong>
                <span class="subtitle">종류</span>
              </div>

              <!-- Body (처음에는 숨김) -->
              <div class="edit-part-info-body">
                <div class="edit-part-top-container">
                  <div>
                    <label>점검주기</label>
                    <div>${formatHoursToYMDH(part.maintenanceCycle)}</div>
                  </div>
                  <div>
                    <label>사용시간</label>
                    <div>${part.usageTime}</div>
                  </div>
                </div>
                <div class="edit-part-bottom-container">
                  <div>
                    <label>점검예정일</label>
                    <div>${part.expectedDate}</div>
                  </div>
                  <div>
                    <label>최근점검일</label>
                    <div>${part.lastDate}</div>
                  </div>
                </div>
              </div>

              <div class="edit-part-image-box">
                <img src="<c:url value='/assets/asset/down.svg'/>"
                     alt="toggle" class="toggle-icon"/>
              </div>
            </div>
          </c:forEach>
    </div>

    <div class="edit-modal-btn">
        <div class="edit-modal-edit-btn">수정</div>
        <div class="edit-modal-cancel-btn">닫기</div>
    </div>

  </div>
</div>










<script>


(function searchWiring(){
  const select = document.querySelector('.search-select');
  const input  = document.querySelector('.search-input');
  const btn    = document.querySelector('.search-btn');

  const fieldMap = { all:'all', name:'category', type:'company', location:'location' };

  function go() {
    const raw = select?.value || 'all';
    const field = fieldMap[raw] || 'all';
    const keyword = (input?.value || '').trim();

    const url = new URL('<c:url value="/admin/asset/list"/>', location.origin);
    url.searchParams.set('page', '1');
    if (field && field !== 'all') url.searchParams.set('field', field);
    if (keyword) url.searchParams.set('keyword', keyword);

    location.href = url.toString();
  }

  btn?.addEventListener('click', go);
  input?.addEventListener('keydown', e => { if (e.key === 'Enter') go(); });

  const p = new URLSearchParams(location.search);
  if (p.get('keyword') && input) input.value = p.get('keyword');

  const reverse = { category:'name', company:'type', location:'location' };
  const uiVal = reverse[p.get('field')] || 'all';
  if (select && [...select.options].some(o => o.value === uiVal)) select.value = uiVal;
})();



/* ==================== 모달 동작 ==================== */
(function modalWiring() {
  const modal        = document.getElementById("assetRegisterModal");
  const openBtn      = document.getElementById("openRegisterModal");
  const cancelBtn    = document.getElementById("cancelRegisterBtn");
  const partList     = document.getElementById("partList");
  const modalContent = document.querySelector("#assetRegisterModal .modal-content");
  const addPartBtn   = document.getElementById("addPartBtn");

  function makePartRow() {
    const wrap = document.createElement("div");
    wrap.className = "modal-part-info-container";
    wrap.innerHTML = `
      <div class="modal-part-category-container">
        <label>종류</label>
        <input class="modal-part-input" placeholder="전체" />
      </div>
      <div class="modal-part-lifecycle-container">
        <div>점검주기</div>
        <input class="modal-part-lifecycle-input" placeholder="ex) 1일, 1개월, 1년" />
      </div>
    `;
    return wrap;
  }

  function resetModal() {
    modal.querySelectorAll("input").forEach(i => i.value = "");
    partList.innerHTML = "";
    partList.appendChild(makePartRow());
    modalContent.scrollTop = 0;
  }

  function openModal() {
    modal.style.display = "block";
    const sw = window.innerWidth - document.documentElement.clientWidth;
    document.body.classList.add("modal-open");
    if (sw > 0) document.body.style.paddingRight = sw + "px";
  }

  function closeModal() {
    modal.style.display = "none";
    document.body.classList.remove("modal-open");
    document.body.style.paddingRight = "";
    resetModal();
  }

  openBtn.addEventListener("click", openModal);
  cancelBtn.addEventListener("click", closeModal);
  window.addEventListener("click", (e) => { if (e.target === modal) closeModal(); });

  addPartBtn.addEventListener("click", () => {
    partList.appendChild(makePartRow());
    modalContent.scrollTop = modalContent.scrollHeight;
  });
})();

/* ==================== 파일 업로드(사진첨부) ==================== */
(function fileUploadWiring() {
  const pickBtn = document.getElementById('openImagePicker');
  const fileInp = document.getElementById('assetImageInput');
  const nameInp = document.getElementById('assetImageName');
  const urlInp  = document.getElementById('assetImageUrl');
  const cancelBtn = document.getElementById('cancelRegisterBtn');

  const clearFile = () => { nameInp.value = ''; urlInp.value = ''; fileInp.value = ''; };

  pickBtn.addEventListener('click', () => fileInp.click());
  nameInp.addEventListener('click', () => fileInp.click());

  fileInp.addEventListener('change', async () => {
    const file = fileInp.files && fileInp.files[0];
    if (!file) return;
    nameInp.value = file.name;

    const fd = new FormData();
    fd.append('file', file);
    try {
      const res = await fetch('<c:url value="/admin/asset/file-upload"/>', { method: 'POST', body: fd });
      if (!res.ok) throw new Error(await res.text() || '업로드 실패');
      const data = await res.json();
      nameInp.value = data.originalName || file.name;
      urlInp.value  = data.url || '';
    } catch (err) {
      alert('업로드 오류: ' + err.message);
      clearFile();
    }
  });

  if (cancelBtn) cancelBtn.addEventListener('click', clearFile);
})();

/* ==================== 기간 문자열 → 시간(정수) ==================== */
const DURATION_BASE = { hoursPerDay: 24, daysPerWeek: 7, daysPerMonth: 30, daysPerYear: 365 };
function parseDurationToHours(s) {
  if (!s) return 0;
  const str = String(s).trim();
  if (!str) return 0;

  let hours = 0;
  const re = /(\d+(?:\.\d+)?)\s*(년|개월|달|월|주|일|시간|시|분|초)/g;
  let match;

  while ((match = re.exec(str)) !== null) {
    const val = parseFloat(match[1]);
    switch (match[2]) {
      case '년':                         hours += val * DURATION_BASE.daysPerYear  * DURATION_BASE.hoursPerDay; break;
      case '개월': case '달': case '월':  hours += val * DURATION_BASE.daysPerMonth * DURATION_BASE.hoursPerDay; break;
      case '주':                         hours += val * DURATION_BASE.daysPerWeek  * DURATION_BASE.hoursPerDay; break;
      case '일':                         hours += val * DURATION_BASE.hoursPerDay; break;
      case '시간': case '시':            hours += val; break;
      case '분':                         hours += val / 60; break;
      case '초':                         hours += val / 3600; break;
    }
  }

  if (hours === 0) {
    const num = parseFloat(str.replace(/[^\d.]/g, ''));
    if (!isNaN(num)) hours = num;
  }

  return Math.round(hours);
}

(function registerSubmitWiring() {
  const submitBtn = document.querySelector('#assetRegisterModal .confirm-btn');

  submitBtn.addEventListener('click', async (e) => {
    e.preventDefault();

    try {
      const category = document.querySelector('#assetRegisterModal .modal-category-container .modal-input')?.value?.trim();
      const company = document.querySelector('#assetRegisterModal .modal-company-container .modal-input')?.value?.trim();
      const modelName = document.querySelector('#assetRegisterModal .modal-model-container .modal-input')?.value?.trim();

      if (!category || !company || !modelName) {
        alert('종류, 제조사, 모델명은 필수 입력 항목입니다.');
        return;
      }

      const assetData = {
        category,
        company,
        modelName,
        assetStatus: 'AVAILABLE'
      };

      const imageUrl = document.getElementById('assetImageUrl')?.value?.trim();
      if (imageUrl) assetData.imagePath = imageUrl;

      const cycleStr = document.querySelector('#assetRegisterModal .asset-info-bottom-container .modal-lifecycle-container .modal-input')?.value?.trim();
      if (cycleStr) {
        const maintenance = parseDurationToHours(cycleStr);
        if (maintenance > 0) assetData.maintenanceCycle = maintenance;
      }

      const partRows = document.querySelectorAll('#partList .modal-part-info-container');
      const validParts = [];
      partRows.forEach((row) => {
        const partName = row.querySelector('.modal-part-input')?.value?.trim();
        const cycleStr = row.querySelector('.modal-part-lifecycle-input')?.value?.trim();

        if (partName && partName.length > 0) {
          const part = {
            partName,
            partStatus: 'AVAILABLE'
          };

          if (cycleStr && cycleStr.length > 0) {
            const cycleHours = parseDurationToHours(cycleStr);
            if (cycleHours > 0) part.maintenanceCycle = cycleHours;
          }
          validParts.push(part);
        }
      });

      const payload = {
        asset: assetData,
        parts: validParts
      };

      const res = await fetch('<c:url value="/admin/asset/register"/>', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...(document.querySelector('meta[name="_csrf_header"]')
            ? { [document.querySelector('meta[name="_csrf_header"]').getAttribute('content')]:
                document.querySelector('meta[name="_csrf"]').getAttribute('content') }
            : {})
        },
        body: JSON.stringify(payload)
      });

      if (!res.ok) throw new Error(await res.text() || '등록 실패');

      window.location.href = '<c:url value="/admin/asset/list"/>';

    } catch (error) {
      console.error('Registration failed:', error);
      alert('등록 중 오류가 발생했습니다: ' + error.message);
    }
  });
})();




document.addEventListener("DOMContentLoaded", () => {
  const tbody        = document.querySelector(".asset-table tbody");
  if (!tbody) return;

  const detailModal  = document.querySelector(".detail-modal");
  const detailBox    = detailModal?.querySelector(".detail-modal-content");
  const editModal    = document.querySelector(".edit-modal");          // id=assetEditModal
  const editBox      = editModal?.querySelector(".edit-modal-content");

  const FALLBACK_IMG = "<c:url value='/assets/asset/plus.svg'/>";
  const CTX          = "<c:url value='/'/>";
  const resolveUrl = (p) => {
    if (!p || !p.trim()) return FALLBACK_IMG;
    if (/^https?:\/\//i.test(p)) return p;
    const base = CTX.endsWith("/") ? CTX.slice(0, -1) : CTX;
    const rel  = p.startsWith("/") ? p : "/" + p;
    return base + rel;
  };

  const show = (el) => el && el.style.setProperty("display", "block", "important");
  const hide = (el) => el && el.style.setProperty("display", "none",  "important");

  function resetDetailModal() {
    if (!detailModal) return;
    detailModal.querySelectorAll("[data-field]").forEach(el => el.textContent = "");
    const img = detailModal.querySelector("[data-field='imagePath']");
    if (img) img.src = FALLBACK_IMG;
    const partBox = detailModal.querySelector(".detail-part-container");
    if (partBox) partBox.innerHTML =
      '<div class="detail-part-title-box"><h3>부품</h3></div>' +
      '<div class="detail-part-btn"><div class="detail-part-request-btn">점검요청</div><div class="detail-part-cancel">닫기</div></div>';
  }

  function resetEditModal() {
    if (!editModal) return;

    editModal.querySelectorAll("input, textarea").forEach(i => i.value = "");
    editModal.querySelectorAll("[data-edit-field]").forEach(el => {
      if (el.tagName === "INPUT" || el.tagName === "TEXTAREA") el.value = "";
      else el.textContent = "";
    });
    const img = editModal.querySelector("[data-edit-field='imagePath']");
    if (img) img.src = FALLBACK_IMG;
    const partBox = editModal.querySelector(".edit-part-container");
    if (partBox) {

      const title = partBox.querySelector(".edit-part-title-box")?.outerHTML || "";
      partBox.innerHTML = title;
    }
  }

  function closeModal(modalEl) {
    if (!modalEl) return;
    hide(modalEl);
    if (modalEl === detailModal) resetDetailModal();
    if (modalEl === editModal)   resetEditModal();
  }

  // ========================= 상세 열기 =========================
  async function openDetailModal(assetId) {
    try {
      closeModal(editModal);

      const res = await fetch("<c:url value='/admin/asset/detail/'/>" + assetId, { method: "GET" });
      if (!res.ok) throw new Error("서버 오류 " + res.status);
      const { asset, parts } = await res.json();

      const setField = (sel, val) => {
        const el = detailModal.querySelector(sel);
        if (el) el.textContent = (val ?? "") !== "" ? val : "정보 없음";
      };

      const assetExpDate =
        computeExpectedDate(
          asset?.lastMaintenanceDate,
          getCreatedAt(asset),
          asset?.maintenanceCycle
        ) || toDate(asset?.expectedMaintenanceDate);

      setField("[data-field='category']", asset?.category);
      setField("[data-field='model']", asset?.modelName);
      setField("[data-field='company']", asset?.company);
      setField("[data-field='usageTime']", asset?.usageTime);
      setField("[data-field='maintenanceCycle']", formatHoursToYMDH(asset?.maintenanceCycle));
      setField("[data-field='expectedDate']", formatYmdDot(asset?.expectedMaintenanceDate));
      setField("[data-field='lastDate']", formatYmdDot(asset?.lastMaintenanceDate));

      const img = detailModal.querySelector("[data-field='imagePath']");
      if (img) img.src = resolveUrl(asset?.imagePath);

      const partBox = detailModal.querySelector(".detail-part-container");
      partBox.innerHTML = '<div class="detail-part-title-box"><h3>부품</h3></div>';

      (parts || []).forEach(p => {
      const expDate =
        computeExpectedDate(
          p.lastMaintenanceDate,
          getCreatedAt(p),
          p.maintenanceCycle
        ) || toDate(p.expectedMaintenanceDate);

        const exp  = formatYmdDot(p.expectedMaintenanceDate);
        const last = formatYmdDot(p.lastMaintenanceDate);
        const mc   = formatHoursToYMDH(p.maintenanceCycle);



        const div = document.createElement("div");
        div.className = "detail-part-info-container collapsed";
        div.innerHTML =
          '<div class="detail-part-info-title">' +
            '<strong>' + (p.partName || "정보 없음") + '</strong>' +
            '<span class="subtitle">종류</span>' +
          '</div>' +
          '<div class="detail-part-info-body">' +
            '<div class="detail-part-top-container">' +
              '<div><label>점검주기</label><div>' + (mc ?? "정보 없음") + '</div></div>' +
              '<div><label>사용시간</label><div>' + (p.usageTime ?? "정보 없음") + '</div></div>' +
            '</div>' +
            '<div class="detail-part-bottom-container">' +
              '<div><label>점검예정일</label><div>' + (exp ?? "정보 없음") + '</div></div>' +
              '<div><label>최근점검일</label><div>' + (last ?? "정보 없음") + '</div></div>' +
            '</div>' +
          '</div>' +
          '<div class="detail-part-image-box">' +
            '<img src="<c:url value='/assets/asset/down.svg'/>" alt="toggle" class="toggle-icon"/>' +
          '</div>';
        partBox.appendChild(div);
      });

      const btns = document.createElement("div");
      btns.className = "detail-part-btn";
      btns.innerHTML =
        '<div class="detail-part-request-btn">점검요청</div>' +
        '<div class="detail-part-cancel">닫기</div>';
      partBox.appendChild(btns);

      partBox.querySelectorAll(".toggle-icon").forEach(tg => {
        tg.addEventListener("click", () => {
          const box = tg.closest(".detail-part-info-container");
          const open = box.classList.contains("collapsed");
          box.classList.toggle("collapsed", !open);
          box.classList.toggle("expanded", open);
          tg.src = open
            ? "<c:url value='/assets/asset/up.svg'/>"
            : "<c:url value='/assets/asset/down.svg'/>";
        });
      });

      btns.querySelector(".detail-part-cancel").addEventListener("click", () => closeModal(detailModal));

      show(detailModal);
    } catch (e) {
      console.error(e);
      alert("상세 조회 중 오류가 발생했습니다.");
    }
  }

  // ========================= 수정 열기 =========================
  async function openEditModal(assetId) {
    try {
      closeModal(detailModal);

      const res = await fetch("<c:url value='/admin/asset/detail/'/>" + assetId, { method: "GET" });
      if (!res.ok) throw new Error("서버 오류 " + res.status);
      const { asset, parts } = await res.json();

      console.log(asset);
      console.log(parts);

      const setField = (sel, val) => {
        const el = editModal.querySelector(sel);
        if (el) el.textContent = (val ?? "") !== "" ? val : "정보 없음";
      };

      setField("[data-edit-field='category']", asset?.category);
      setField("[data-edit-field='model']", asset?.modelName);
      setField("[data-edit-field='company']", asset?.company);
      setField("[data-edit-field='usageTime']", asset?.usageTime);
      setField("[data-edit-field='expectedDate']", formatYmdDot(asset?.expectedMaintenanceDate));
      setField("[data-edit-field='lastDate']", formatYmdDot(asset?.lastMaintenanceDate));

      const editImg = editModal.querySelector("[data-edit-field='imagePath']");
      if (editImg) editImg.src = resolveUrl(asset?.imagePath);

      const cycleInp = editModal.querySelector("[data-edit-input='maintenanceCycle']");
      if (cycleInp) {
        cycleInp.placeholder = formatHoursToYMDH(asset?.maintenanceCycle) ?? "ex) 1일, 1개월, 1년";
      }

      const partBox = editModal.querySelector(".edit-part-container");
      const titleHTML = partBox.querySelector(".edit-part-title-box")?.outerHTML || "";
      partBox.innerHTML = titleHTML;

      (parts || []).forEach(p => {
        const exp  = formatYmdDot(p.expectedMaintenanceDate);
        const last = formatYmdDot(p.lastMaintenanceDate);
        const mc   = formatHoursToYMDH(p.maintenanceCycle);

        const wrap = document.createElement("div");
        wrap.className = "edit-part-info-container collapsed";
        wrap.innerHTML =
          '<div class="edit-part-info-title-container">' +
            '<div class="edit-part-info-title">' +
                '<strong>' + (p.partName || "정보 없음") + '</strong>' +
                '<span class="subtitle">종류</span>' +
            '</div>' +
            '<div>' +
                '<form method="post" action="<c:url value='/admin/asset/delete/part'/>" class="delete-part-form" style="display:none;">' +
                  '<input type="hidden" name="partId" value="' + p.partId + '">' +
                  '<button type="submit" class="delete-part-btn" title="삭제">' +
                    '<img src="<c:url value='/assets/asset/delete.svg'/>" alt="delete"/>' +
                  '</button>' +
                '</form>' +
            '</div>' +
          '</div>' +
          '<div class="edit-part-info-body">' +
            '<div class="edit-part-top-container">' +
              '<div><label>점검주기</label>' +
                '<input class="edit-part-input" ' +
                'placeholder="' + (mc ?? "정보 없음") + '" />' +
              '</div>' +
              '<div><label>사용시간</label><div>' + (p.usageTime ?? "정보 없음") + '</div></div>' +
            '</div>' +
            '<div class="edit-part-bottom-container">' +
              '<div><label>점검예정일</label><div>' + (exp ?? "정보 없음") + '</div></div>' +
              '<div><label>최근점검일</label><div>' + (last ?? "정보 없음") + '</div></div>' +
            '</div>' +
          '</div>' +
          '<div class="edit-part-image-box">' +
            '<img src="<c:url value="/assets/asset/down.svg"/>" alt="toggle" class="toggle-icon"/>' +
          '</div>';
        partBox.appendChild(wrap);
      });

      partBox.querySelectorAll(".toggle-icon").forEach(tg => {
        tg.addEventListener("click", () => {
          const box = tg.closest(".edit-part-info-container");
          const open = box.classList.contains("collapsed");
          box.classList.toggle("collapsed", !open);
          box.classList.toggle("expanded", open);
          tg.src = open
            ? "<c:url value='/assets/asset/up.svg'/>"
            : "<c:url value='/assets/asset/down.svg'/>";

            const delForm = box.querySelector(".delete-part-form");
            if (delForm) {
              delForm.style.display = open ? "inline-block" : "none";
            }
        });
      });

       const addBtn    = editModal.querySelector(".edit-part-add-box");
          function makeEditPartRow() {
            const wrap = document.createElement("div");
            wrap.className = "modal-part-info-container";
            wrap.innerHTML = `
              <div class="modal-part-category-container">
                <label>종류</label>
                <input class="modal-part-input" placeholder="전체" />
              </div>
              <div class="modal-part-lifecycle-container">
                <div>점검주기</div>
                <input class="modal-part-lifecycle-input" placeholder="ex) 1일, 1개월, 1년" />
              </div>
            `;
            return wrap;
          }

        if (addBtn) {
          addBtn.addEventListener("click", () => {
            partBox.appendChild(makeEditPartRow());
            partBox.scrollTop = partBox.scrollHeight;
          });
        }

    const picker  = document.getElementById("editImagePicker");
    const fileInp = document.getElementById("editImageInput");
    const preview = document.querySelector(".edit-image"); // 기존 <img>
    const urlInp  = document.getElementById("editImageUrl"); // 서버 URL 저장용 hidden input

    picker.addEventListener("click", (e) => {
      e.stopPropagation();
      fileInp.click();
    });

    fileInp.addEventListener("change", async () => {
      const file = fileInp.files && fileInp.files[0];
      if (!file) return;

      try {
        const fd = new FormData();
        fd.append("file", file);

        const res = await fetch("<c:url value='/admin/asset/file-upload'/>", {
          method: "POST",
          body: fd
        });

        if (!res.ok) throw new Error(await res.text() || "업로드 실패");

        const data = await res.json();

        preview.src = data.url || preview.src;

        if (urlInp) urlInp.value = data.url || "";

        console.log("이미지 업로드 성공:", data);
      } catch (err) {
        alert("이미지 업로드 오류: " + err.message);
        console.error(err);
      }
    });

      editModal.querySelector(".edit-modal-cancel-btn")?.addEventListener("click", () => closeModal(editModal), { once:true });

      show(editModal);
    } catch (e) {
      console.error(e);
      alert("수정 조회 중 오류가 발생했습니다.");
    }

    editModal.dataset.assetId = assetId;

 {
   const oldBtn = editModal.querySelector(".edit-modal-edit-btn");
   const submitBtn = oldBtn.cloneNode(true);
   oldBtn.parentNode.replaceChild(submitBtn, oldBtn);

   submitBtn.addEventListener("click", async (e) => {
     e.preventDefault();

     try {
       const aid = editModal.dataset.assetId || assetId;

       const res = await fetch("<c:url value='/admin/asset/detail/'/>" + aid, { method: "GET" });
       if (!res.ok) throw new Error("서버 오류 " + res.status);
       const { asset, parts } = await res.json();

       const origAssetCycle = Number(asset?.maintenanceCycle ?? 0);
       const origCount      = Array.isArray(parts) ? parts.length : 0;

       let assetCycleChanged = false;
       let partCyclesChanged = false;

       const mergedParts = Array.isArray(parts) ? [...parts] : [];

       const assetCycleInput = editModal.querySelector("[data-edit-input='maintenanceCycle']");
       const assetCycleStr   = assetCycleInput?.value?.trim();
       if (assetCycleStr) {
         const h = parseDurationToHours(assetCycleStr);
         if (h > 0) {
           if (origAssetCycle !== h) assetCycleChanged = true;
           asset.maintenanceCycle = h;
         }
       }

       const existingRows = editModal.querySelectorAll(".edit-part-info-container");
       existingRows.forEach((row, idx) => {
         const inp = row.querySelector("input.edit-part-input");
         const val = inp?.value?.trim();
         if (!val) return;

         const h = parseDurationToHours(val);
         if (h > 0 && mergedParts[idx]) {
           const before = Number(mergedParts[idx].maintenanceCycle ?? 0);
           if (before !== h) partCyclesChanged = true;
           mergedParts[idx].maintenanceCycle = h;
         }
       });

       const newPartRows = editModal.querySelectorAll(".modal-part-info-container");
       newPartRows.forEach(row => {
         const nameEl  = row.querySelector(".modal-part-input");
         const cycleEl = row.querySelector(".modal-part-lifecycle-input");
         const partName = nameEl?.value?.trim();
         const cycleStr = cycleEl?.value?.trim();

         if (!partName) return;

         const p = { partName, partStatus: "AVAILABLE" };
         p.assetId = aid;
         if (cycleStr) {
           const h = parseDurationToHours(cycleStr);
           if (h > 0) p.maintenanceCycle = h;
         }
         mergedParts.push(p);
       });

       const partCountChanged = mergedParts.length !== origCount;

       const payload = {
         asset,
         parts: mergedParts,
         assetCycleChanged,
         partCyclesChanged,
         partCountChanged
       };

       const postRes = await fetch("<c:url value='/admin/asset/update'/>", {
         method: "POST",
         headers: {
           "Content-Type": "application/json",
           ...(document.querySelector('meta[name="_csrf_header"]')
             ? { [document.querySelector('meta[name="_csrf_header"]').getAttribute('content')]:
                 document.querySelector('meta[name="_csrf"]').getAttribute('content') }
             : {})
         },
         body: JSON.stringify(payload)
       });

       if (!postRes.ok) throw new Error(await postRes.text() || "수정 실패");
       alert("수정되었습니다.");
       location.reload();

     } catch (err) {
       console.error(err);
       //alert("수정 중 오류가 발생했습니다: " + err.message);
     }
   });
 }
}

  tbody.addEventListener("click", (e) => {
    const editBtn   = e.target.closest("button.edit-asset-btn");
    const detailBtn = e.target.closest("button.detail-asset-btn");
    if (!editBtn && !detailBtn) return;

    const row     = e.target.closest("tr");
    const assetId = row?.getAttribute("data-id");
    if (!assetId) return;

    e.preventDefault();
    if (detailBtn) openDetailModal(assetId);
    else openEditModal(assetId);
  });

  detailModal?.addEventListener("click", (e) => { if (e.target === detailModal) closeModal(detailModal); });
  editModal?.addEventListener("click",   (e) => { if (e.target === editModal)   closeModal(editModal); });

  detailBox?.addEventListener("click", (e) => e.stopPropagation());
  editBox?.addEventListener("click",   (e) => e.stopPropagation());

  window.addEventListener("keydown", (e) => {
    if (e.key !== "Escape") return;
    if (detailModal && getComputedStyle(detailModal).display !== "none") closeModal(detailModal);
    else if (editModal && getComputedStyle(editModal).display !== "none") closeModal(editModal);
  });
});


function redirectWith(mutator){
  const url = new URL(location.href);
  mutator(url.searchParams);
  url.searchParams.set('page','1');
  location.href = url.toString();
}

(function multiFilterWiring(){
  const map = {
    'filter-category': 'category',
    'filter-company' : 'company',
    'filter-location': 'location',
    'filter-status'  : 'assetStatus'
  };
  document.querySelectorAll('.selection-container select').forEach(sel=>{
    sel.addEventListener('change', ()=>{
      const key = map[sel.id];
      const val = (sel.value||'').trim();
      if (!key || !val) return;

      redirectWith(sp=>{
        const exists = sp.getAll(key).includes(val);
        if (!exists) sp.append(key, val);
      });

      sel.value = '';
    });
  });
})();

(function tagRemoveWiring(){
  document.getElementById('filter-tags')?.addEventListener('click', e=>{
    const x = e.target.closest('.xbtn'); if(!x) return;
    const tag = x.closest('.filter-tag');
    const key = tag?.dataset.key;
    const val = tag?.dataset.label;
    if(!key) return;

    redirectWith(sp=>{
      const all = sp.getAll(key);
      sp.delete(key);
      all.filter(v=>v!==val).forEach(v=>sp.append(key,v));
    });
  });
})();

document.getElementById('resetFiltersBtn')?.addEventListener('click', ()=>{
  const keys = ['category','company','location','assetStatus'];
  redirectWith(sp=> keys.forEach(k=>sp.delete(k)));
});


(function wireServerCsvExport(){
  const btn = document.querySelector('.download-btn');
  if (!btn) return;

  btn.addEventListener('click', function(){
    var sp = new URLSearchParams(window.location.search);
    sp.delete('page'); sp.delete('pageSize');

    var base = '<c:url value="/admin/asset/export"/>';
    var url  = base + (sp.toString() ? ('?' + sp.toString()) : '');
    window.location.href = url;
  });
})();

/* ==================== 날짜 → "YYYY.MM.DD" ==================== */
function formatYmdDot(value) {
  if (value == null) return "";

  if (value instanceof Date && !isNaN(value.getTime())) {
    const y  = value.getFullYear();
    const mm = String(value.getMonth() + 1).padStart(2, "0");
    const dd = String(value.getDate()).padStart(2, "0");
    return y + "." + mm + "." + dd;
  }

  const s = String(value).trim();
  if (!s) return "";

  const m = s.match(/^(\d{4})-(\d{2})-(\d{2})/);
  if (m) return m[1] + "." + m[2] + "." + m[3];

  const d = new Date(s);
  if (!isNaN(d.getTime())) {
    const y  = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, "0");
    const dd = String(d.getDate()).padStart(2, "0");
    return y + "." + mm + "." + dd;
  }
  return s;
}

function toDate(v) {
  if (!v) return null;
  if (v instanceof Date) return isNaN(v.getTime()) ? null : v;
  const s = String(v).trim();
  if (!s) return null;
  const d = new Date(s);
  return isNaN(d.getTime()) ? null : d;
}

function addHours(date, hours) {
  if (!(date instanceof Date)) return null;
  if (!isFinite(hours)) return null;
  return new Date(date.getTime() + hours * 3600 * 1000);
}

function getCreatedAt(obj) {
  if (!obj) return null;
  const candidates = [
    "createdAt", "createdDate", "createAt", "createDate",
    "regDate", "registeredAt", "insertedAt", "insertedDate", "created_time"
  ];
  for (let i = 0; i < candidates.length; i++) {
    if (obj[candidates[i]] != null) return obj[candidates[i]];
  }
  return null;
}

function computeExpectedDate(lastDate, createdAt, cycleHours) {
  const h = Number(cycleHours);
  if (!isFinite(h) || h <= 0) return null;

  const base =
    toDate(lastDate) ||
    toDate(createdAt);

  if (!base) return null;
  return addHours(base, h);
}

function formatHoursToYMDH(value) {
  const n = Math.floor(Number(value));
  if (!isFinite(n)) return "";
  if (n <= 0) return "0시간";

  let rem = n;
  const H_PER_DAY   = 24;
  const D_PER_YEAR  = 365;
  const D_PER_MONTH = 30;

  const years  = Math.floor(rem / (H_PER_DAY * D_PER_YEAR));  rem -= years  * H_PER_DAY * D_PER_YEAR;
  const months = Math.floor(rem / (H_PER_DAY * D_PER_MONTH)); rem -= months * H_PER_DAY * D_PER_MONTH;
  const days   = Math.floor(rem / H_PER_DAY);                  rem -= days   * H_PER_DAY;
  const hours  = rem;

  const parts = [];
  if (years)  parts.push(years  + "년");
  if (months) parts.push(months + "개월");
  if (days)   parts.push(days   + "일");
  if (hours)  parts.push(hours  + "시간");

  return parts.length ? parts.join(" ") : "0시간";
}

</script>