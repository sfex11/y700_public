---
type: action
category: recovery
status: draft
updated: 2026-04-15
---

# 백버튼으로 홈 복귀

## 목적
- Android 백버튼을 반복 눌러 게임 홈 화면으로 복귀한다

## 전제조건
- 상태: 알 수 없는 화면 또는 하위 메뉴에 진입한 상태
- 화면: 게임 내 임의 화면

## 단계
1. 감지: 현재 화면이 홈 화면이 아님을 확인
2. 실행: back 버튼 전송 → 화면 변화 확인 → 반복 (최대 5회)
3. 확인: 홈 화면 특징 요소(메뉴 아이콘 등) 출현 확인

## 실패 시
- 재시도: 5회 back 후에도 홈 미도달 시 restart-app 액션 전환
- 복구: 앱이 종료된 경우 start-app 액션 실행

## 타이밍
- 대기: 1000ms (각 back 사이 간격)
- 랜덤 딜레이: 200~500ms

## ADB 명령 예시
- keyevent KEYCODE_BACK: 백버튼 1회
- 반복: 최대 5회, 매회 홈 화면 여부 체크

## 관련 state
- back_press_count: 0~5
- current_screen: unknown → home
