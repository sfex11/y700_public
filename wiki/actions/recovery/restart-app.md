---
type: action
category: recovery
status: draft
updated: 2026-04-15
---

# 앱 재시작

## 목적
- 게임 앱 프로세스를 종료하고 재실행한다

## 전제조건
- 상태: 오류 반복, 화면 멈춤, 복구 불가 상태
- 화면: 임의 (반응 없음 포함)

## 단계
1. 감지: 복구 시도 실패 또는 오류 3회 이상 누적
2. 실행: am force-stop으로 앱 종료 → 3초 대기 → am start로 재실행
3. 확인: 스플래시/로고 화면 출현 확인 → 로딩 완료 대기

## 실패 시
- 재시도: 앱 미실행 시 5초 후 재시도 (최대 3회)
- 복구: 3회 실패 시 봇 일시정지 및 알림 발송

## 타이밍
- 대기: 3000ms (종료 후 재실행 간격)
- 랜덤 딜레이: 없음

## ADB 명령 예시
- am force-stop com.bluepotion.uwom2: 앱 강제 종료
- am start -n com.bluepotion.uwom2/.MainActivity: 앱 실행

## 관련 state
- app_running: false → true
- restart_count: 누적 재시작 횟수
