document.addEventListener("DOMContentLoaded", () => {
    console.log("Alarm script loaded");
    
    // 알람 데이터 로딩
    loadAlarms();
    
    // 읽지 않은 알람 개수 로딩
    loadUnreadCount();
    
    // 이벤트 위임 방식으로 동적 요소 처리
    document.addEventListener("click", (e) => {
        // 알람 버튼 클릭 시 모달 열기
        if (e.target.closest(".alarm-container")) {
            const modal = document.getElementById("alarmModal");
            if (modal) {
                modal.style.display = "block";
                console.log("Alarm modal opened");
            } else {
                console.error("Alarm modal not found");
            }
        }
        
        // 닫기 버튼 클릭 시 모달 닫기
        if (e.target.classList.contains("modal-close")) {
            const modal = document.getElementById("alarmModal");
            if (modal) {
                modal.style.display = "none";
                console.log("Alarm modal closed");
            }
        }
        
        // 모달 배경 클릭 시 닫기
        if (e.target.id === "alarmModal") {
            e.target.style.display = "none";
            console.log("Alarm modal closed by background click");
        }
        
        // 읽음 버튼 클릭 시 알람 읽음 처리
        if (e.target.classList.contains("read-button")) {
            const alarmId = e.target.getAttribute("data-alarm-id");
            markAlarmAsRead(alarmId);
        }
    });
    
    // ESC 키로 모달 닫기
    document.addEventListener("keydown", (e) => {
        if (e.key === "Escape") {
            const modal = document.getElementById("alarmModal");
            if (modal && modal.style.display === "block") {
                modal.style.display = "none";
                console.log("Alarm modal closed by ESC key");
            }
        }
    });
});

// 알람 데이터 로딩
function loadAlarms() {
    fetch('/alarms')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(alarms => {
            console.log('Alarms loaded:', alarms);
            renderAlarms(alarms);
        })
        .catch(error => {
            console.error('Error loading alarms:', error);
            renderEmptyAlarms();
        });
}

// 알람 리스트 렌더링
function renderAlarms(alarms) {
    const alarmList = document.querySelector('.alarm-list');
    if (!alarmList) {
        console.error('Alarm list element not found');
        return;
    }
    
    if (alarms.length === 0) {
        alarmList.innerHTML = '<li class="alarm-item"><div class="alarm-message">알람이 없습니다.</div></li>';
        return;
    }
    
    alarmList.innerHTML = alarms.map(alarm => {
        const isRead = alarm.isDeleted === 'Y';
        const readButton = isRead ? '' : `<button class="read-button" data-alarm-id="${alarm.id}">읽음</button>`;
        
        return `<li class="alarm-item ${isRead ? 'read' : ''}">
            <div class="alarm-icon">
                <img src="/assets/layout/admin/alarm-detail.svg" alt="알람"/>
            </div>
            <div class="alarm-body">
                <div class="alarm-message">${alarm.description}</div>
                <div class="alarm-date">${formatDate(alarm.createdAt)}</div>
            </div>
            ${readButton}
        </li>`;
    }).join('');
}

// 알람 타입 설명 가져오기
function getAlarmTypeDescription(type) {
    const typeMap = {
        'ASSET_REGULAR_MAINTENANCE': '자산 정기점검 도래 알림',
        'PART_REGULAR_REPLACE': '부품 정기점검 도래 알림',
        'RESERVATION_OVERDUE': '예약 연체 알림',
        'ASSET_MAINTENANCE_LEAVED': '자산 점검 방치 알림'
    };
    return typeMap[type] || type;
}

// 날짜 포맷팅 (YYYY-MM-DD)
function formatDate(dateString) {
    if (!dateString) return '';
    
    try {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}.${month}.${day}`;
    } catch (error) {
        console.error('Error formatting date:', error);
        return dateString;
    }
}

// 빈 알람 상태 렌더링
function renderEmptyAlarms() {
    const alarmList = document.querySelector('.alarm-list');
    if (alarmList) {
        alarmList.innerHTML = '<li class="alarm-item"><div class="alarm-message">알람을 불러올 수 없습니다.</div></li>';
    }
}

// 읽지 않은 알람 개수 로딩
function loadUnreadCount() {
    fetch('/alarms/unread-count')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(count => {
            updateUnreadCount(count);
        })
        .catch(error => {
            console.error('Error loading unread count:', error);
            updateUnreadCount(0);
        });
}

// 읽지 않은 알람 개수 업데이트
function updateUnreadCount(count) {
    const alarmCountElement = document.querySelector('.alarm-count');
    if (alarmCountElement) {
        if (count > 0) {
            alarmCountElement.textContent = count;
            alarmCountElement.classList.remove('hidden');
        } else {
            alarmCountElement.classList.add('hidden');
        }
    }
}

// 알람 읽음 처리
function markAlarmAsRead(alarmId) {
    fetch(`/alarms/${alarmId}/read`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        
        // 해당 알람 아이템을 DOM에서 완전히 제거
        const alarmItem = document.querySelector(`[data-alarm-id="${alarmId}"]`).closest('.alarm-item');
        if (alarmItem) {
            alarmItem.remove();
            
            // 알람 리스트가 비었는지 확인
            const alarmList = document.querySelector('.alarm-list');
            const remainingAlarms = alarmList.querySelectorAll('.alarm-item');
            
            if (remainingAlarms.length === 0) {
                // 모든 알람이 읽혔을 때 빈 상태 메시지 표시
                alarmList.innerHTML = '<li class="alarm-item"><div class="alarm-message">모든 알람을 확인했습니다.</div></li>';
            }
        }
        
        // 읽지 않은 알람 개수 업데이트
        loadUnreadCount();
        
        console.log(`Alarm ${alarmId} marked as read and removed from UI`);
    })
    .catch(error => {
        console.error('Error marking alarm as read:', error);
    });
}