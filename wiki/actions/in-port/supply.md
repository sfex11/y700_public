---
type: action
category: in-port
status: draft
updated: 2026-04-15
---

# 보급 (Supply)

## 목적
- 항해에 필요한 물/식량/자재/포탄 보급
- 항해 중 매일 물과 식량 소모, 자재는 수리용, 포탄은 전투용

## 전제조건
- 상태: 항구 정박 중 (docked)
- 화면: 출항소 내부 또는 보급 화면
- 두카트 보유 (보급 비용)

## 보급 항목
- 물 (water): 매일 소모, 부족 시 선원 체력 감소
- 식량 (food): 매일 소모, 부족 시 선원 사기 저하
- 자재 (materials): 선박 수리용
- 포탄 (cannonballs): 해전 전투용

## 단계
1. 감지: 출항소 진입 → 보급 탭/화면 확인
2. 실행: 자동 보급 버튼 tap 또는 개별 항목 수량 조정
3. 확인: 보급량 수치 변경 확인

## 비율 설정 (프로필)
- 모험: 물/식량 높음, 포탄 낮음
- 교역: 화물 공간 최대, 보급 최소
- 전투: 포탄/자재 높음

## 실패 시
- 재시도: 두카트 부족 시 교역 선행
- 복구: 보급 실패 시 수동 모드로 전환

## 타이밍
- 대기: 800ms (수치 반영 대기)
- 랜덤 딜레이: 200~500ms

## ADB 명령 예시
- tap(auto_supply_x, auto_supply_y): 자동 보급 버튼
- tap(item_plus_x, item_plus_y): 개별 항목 증가

## 관련 state
- supply_water, supply_food, supply_materials, supply_cannonballs
- ducat_balance
- cargo_ratio_profile
