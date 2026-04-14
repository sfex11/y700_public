---
type: action
category: in-port
status: draft
updated: 2026-04-15
---

# 수리 (Repair)

## 목적
- 선박 내구도 회복 (내구도 0이면 출항 불가)
- 조선소 또는 출항소에서 수리 가능

## 전제조건
- 상태: 항구 정박 중 (docked)
- 화면: 항구 메인 UI
- 두카트 보유 (수리 비용)
- 선박 내구도 < 최대치

## 단계
1. 감지: 선박 내구도 확인 → 수리 필요 판단 (임계값 이하)
2. 실행: 조선소 tap → 수리 버튼 tap → 수리 대상 선박 선택 → 확인
3. 확인: 내구도 수치 회복 확인

## 수리 장소
- 조선소 (shipyard): 전체 수리, 개조도 가능
- 출항소 (departure office): 간이 수리

## 실패 시
- 재시도: 두카트 부족 시 교역으로 자금 확보
- 복구: 조선소 없는 항구 → 자재로 항해 중 응급 수리 또는 다른 항구 이동

## 타이밍
- 대기: 1000ms (수리 애니메이션 완료 대기)
- 랜덤 딜레이: 300~700ms

## ADB 명령 예시
- tap(shipyard_x, shipyard_y): 조선소 진입
- tap(repair_btn_x, repair_btn_y): 수리 버튼
- tap(confirm_x, confirm_y): 수리 확인

## 관련 state
- ship_durability (현재/최대)
- ducat_balance
- port_has_shipyard
