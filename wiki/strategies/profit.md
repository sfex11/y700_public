---
type: strategy
mode: MONEY_MAKING
status: draft
updated: 2026-04-15
---

# Profit — 수익 극대화 전략

## 목표
교역으로 골드를 지속적으로 번다.
가장 많이 머무는 기본 모드.

## 진입 조건
- BOOTSTRAP 완료
- RECOVERY/GROWTH/COMBAT 종료 후 복귀
- 기본 상태 (특별한 위험/기회 없음)

## goal 점수표

| goal | 조건 | 점수 |
|------|------|------|
| buy_cheap | 시세 ≤ 85% + 적재 여유 | 80 |
| sell_high | 시세 ≥ 115% + 보유 물품 | 80 |
| check_market | 시세 모름 | 70 |
| invest | 골드 > 50000 + 투자 쿨타임 완료 | 60 |
| smuggling | 밀수 이익 > 일반 교역 1.5배 | 55 |
| barter | 마을 근처 + 물물교환 가능 | 40 |
| dispatch | 파견함대 미운영 중 | 30 |

## action 매핑

| goal | 액션 (우선순위) |
|------|----------------|
| buy_cheap | check-prices → buy-goods → check-cargo |
| sell_high | check-prices → sell-goods |
| check_market | enter-market → check-prices |
| invest | enter-market → invest |
| smuggling | visit-npc(밀수상) → smuggling |
| barter | visit-npc(마을) → barter |
| dispatch | dispatch(파견함대 설정) |

## 교역 루트 선택 로직

### 기본 루트 (초보)
- 리스보아 ↔ 세비야 (근거리, 안전)
- 보르도 ↔ 암스테르담 (북유럽)

### 고급 루트 (자금 > 50000)
- 이스탄불 ↔ 아테네 (지중해)
- 인도 항로 (고위험 고수익)

### 루트 선택 기준
1. 현재 위치에서 가장 가까운 수익 루트
2. 보유 교역품과 시세 매칭
3. 위험 해역 회피 (전투력 부족 시)
4. 언어 능력 (관세 면제 여부)

## 이탈 조건
- 내구도 < 40% → RECOVERY
- 골드 > 100000 + 내구도 > 70% → GROWTH
- 적 조회 + 약함 → COMBAT_ESCAPE
- 적 조회 + 강함 → COMBAT_ACTIVE

## 금지 행동
- 위험 해역에서의 불필요한 체류
- 시세 > 100% 매입 (손실)
- 시세 < 100% 매도 (손실)
- 적재량 초과 매입

## 예외 상황
- 교역소 폐업/이벤트 → 다음 항구로 이동
- 면세증 없이 외국 항구 → 관세 고려한 수익 재계산
- 이슬람 항구 주류 교역 → 금지, 다른 품목 선택
