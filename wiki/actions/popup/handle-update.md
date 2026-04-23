---
type: action
category: popup
status: draft
updated: 2026-04-15
---

# 업데이트 팝업 대응

## 목적
- 앱 버전 업데이트 필요 팝업을 감지하고 대응한다

## 전제조건
- 상태: 게임 실행 시 버전 불일치 감지
- 화면: 업데이트 안내 팝업 표시됨

## 단계
1. 감지: "업데이트", "새 버전", "update" 텍스트 확인
2. 실행: 봇 일시정지 → 알림 발송 (수동 업데이트 필요)
3. 확인: 업데이트 완료 후 재실행 시 팝업 미출현 확인

## 실패 시
- 재시도: 업데이트는 자동 처리 불가, 사용자 개입 필요
- 복구: 알림 발송 후 봇 대기 모드 전환

## 타이밍
- 대기: 없음 (즉시 알림)
- 랜덤 딜레이: 없음

## ADB 명령 예시
- tap(540, 1500): 확인 버튼 (스토어 이동)
- keyevent KEYCODE_HOME: 홈 화면 이동

## 관련 state
- update_required: true
- bot_mode: paused
- notification_sent: true
