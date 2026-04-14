---
type: action
category: sailing
status: draft
updated: 2026-04-15
---

# 항구 감지 및 입항 (Detect Port)

## 목적
- 항해 중 접안 가능 항구 감지
- 보급/수리/교역 필요 시 자동 입항

## 전제조건
- 상태: 해상 항해 중
- 화면: HUD에 항구 아이콘 또는 접안 버튼 출현 가능

## 단계
1. 감지: 화면 내 항구 아이콘 출현 여부 (접안범위 진입 시 표시)
   - 기본 접안범위 = 척후법 레벨에 비례
   - 악천후 시 접안범위 축소 (흐림-300, 폭우/폭설-300, 폭풍우-500)
2. 판단: 입항 필요 여부
   - 물/식량 < 30% > 즉시 입항
   - 내구도 < 40% > 즉시 입항
   - 목적지 항구 도달 > 입항
3. 실행: "접안" 버튼 탭 > 입항 확인 팝업 "확인"
4. 확인: 항구 화면 로딩 완료 (항구 UI 요소 감지)

## 실패 시
- 재시도: 접안 버튼 미출현 시 2000ms 대기 후 재감지 (최대 3회)
- 복구: 접안범위 벗어남 시 수동 조작으로 항구 방향 스와이프

## 타이밍
- 대기: 2000ms (항구 화면 로딩)
- 랜덤 딜레이: 200~400ms
- 항구 스캔 주기: 4000ms

## ADB 명령 예시
- tap(540, 1700): "접안" 버튼
- tap(640, 1100): 입항 확인 "확인"
- swipe(540, 960, 300, 700): 항구 방향 수동 접근

## 관련 state
- state.sailing.nearby_port = null | port_name
- state.sailing.docking_range
- state.supplies.water_pct, state.supplies.food_pct
- state.ship.durability_pct
- state.knowledge.척후법_level
