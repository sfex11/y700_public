---
type: action
category: recovery
status: draft
updated: 2026-04-15
---

# 재접속

## 목적
- 네트워크 끊김 후 재접속 및 재로그인을 수행한다

## 전제조건
- 상태: 네트워크 연결 끊김 또는 세션 만료
- 화면: 재접속 팝업 또는 로그인 화면

## 단계
1. 감지: "재접속", "연결 끊김", "세션 만료" 텍스트 확인
2. 실행: 재접속 버튼 tap → 로딩 대기 → 로그인 화면 시 자동 로그인
3. 확인: 게임 홈 화면 복귀 확인

## 실패 시
- 재시도: 재접속 실패 시 10초 후 재시도 (최대 5회)
- 복구: 5회 실패 시 restart-app 액션으로 전환

## 타이밍
- 대기: 5000ms (서버 재접속 대기)
- 랜덤 딜레이: 500~2000ms

## ADB 명령 예시
- tap(540, 1400): 재접속 버튼
- tap(540, 1200): 로그인 버튼

## 관련 state
- network_connected: false → true
- reconnect_attempts: 0~5
- session_valid: false → true
