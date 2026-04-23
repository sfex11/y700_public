---
type: action
category: login
status: draft
updated: 2026-04-15
---

# 로그인 팝업 처리

## 목적
- 로그인 과정에서 나타나는 약관 동의, 공지사항, 이벤트 배너를 처리한다

## 전제조건
- 상태: 서버 접속 직후
- 화면: 약관/공지/이벤트 팝업 표시됨

## 단계
1. 감지: 약관 동의 화면, 공지 팝업, 이벤트 배너 순서대로 확인
2. 실행: 약관 → 전체 동의 tap → 확인 tap / 공지 → 닫기 tap / 배너 → X 버튼 tap
3. 확인: 모든 팝업 소멸 후 게임 홈 화면 진입 확인

## 실패 시
- 재시도: 팝업 잔존 시 닫기/X 버튼 재tap (최대 5회)
- 복구: 팝업 무한 반복 시 back-to-home 액션으로 전환

## 타이밍
- 대기: 1000ms (각 팝업 전환 간격)
- 랜덤 딜레이: 200~600ms

## ADB 명령 예시
- tap(540, 1600): 동의/확인 버튼
- tap(900, 300): X 닫기 버튼
- tap(540, 1500): 닫기 버튼

## 관련 state
- login_popup_count: 남은 팝업 수
- terms_accepted: true/false
- screen: login_popup → home
