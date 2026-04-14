---
type: action
category: in-port
status: draft
updated: 2026-04-15
---

# 출항 (Depart)

## 목적
- 항구에서 출항하여 항해 상태로 전환
- 출항 전 보급/수리 상태 확인

## 전제조건
- 상태: 항구 정박 중 (docked)
- 화면: 항구 메인 UI
- 선박 내구도 > 0 (내구도 0이면 출항 불가)

## 단계
1. 감지: 출항소 아이콘 탐지
2. 실행: 출항소 tap → 보급 확인 → 출항 버튼 tap
3. 확인: 월드맵 전환 확인 (항해 UI 요소 감지)

## 자동 보급 설정
- 자동 보급 ON: 비율 설정에 따라 자동으로 물/식량/자재/포탄 보급
- 비율 설정: 모험/교역/전투 프로필별 화물 비율 조정 가능
- 수동 보급: 개별 항목 수량 직접 지정

## 실패 시
- 재시도: 보급 부족 경고 시 보급 후 재시도
- 복구: 내구도 0 → repair 액션 선행 실행

## 타이밍
- 대기: 1500ms (화면 전환 대기)
- 랜덤 딜레이: 200~600ms

## ADB 명령 예시
- tap(departure_x, departure_y): 출항소 진입
- tap(depart_btn_x, depart_btn_y): 출항 버튼

## 관련 state
- docked → sailing
- supply_status (water, food, materials, cannonballs)
- ship_durability
