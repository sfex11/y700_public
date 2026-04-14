---
type: action
category: popup
status: draft
updated: 2026-04-15
---

# 점검 팝업 대응

## 목적
- 서버 점검 공지 팝업을 감지하고 대기 모드로 전환한다

## 전제조건
- 상태: 게임 실행 중 또는 로그인 시도 중
- 화면: 점검 안내 팝업 (카운트다운 포함)

## 단계
1. 감지: "점검", "maintenance" 텍스트 및 종료 예정 시각 확인
2. 실행: 확인 버튼 tap → 앱 종료 또는 대기 모드 진입
3. 확인: 점검 종료 시각까지 주기적으로 앱 재실행 시도

## 실패 시
- 재시도: 점검 종료 예상 시각 이후 5분 간격으로 재시도
- 복구: 점검 2시간 초과 시 봇 일시정지 및 알림 발송

## 타이밍
- 대기: 300000ms (5분 간격 재시도)
- 랜덤 딜레이: 없음

## ADB 명령 예시
- tap(540, 1500): 확인 버튼
- am force-stop: 앱 종료

## 관련 state
- maintenance_detected: true
- maintenance_end_time: 점검 종료 예상 시각
- bot_mode: waiting
