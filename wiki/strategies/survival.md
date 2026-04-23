---
type: strategy
mode: RECOVERY
status: draft
updated: 2026-04-15
---

# Survival — 생존/복구 전략

## 목표
내구도, 보급, 자금을 안전 수준까지 복구한다.
모든 수익/성장 활동을 중단하고 생존만 최우선.

## 진입 조건
- 내구도 < 40%
- 보급(물/식량) < 20%
- 골드 < 3000
- 전투 패배 후
- 재해 발생 후 (화재, 쥐떼 등)

## goal 점수표

| goal | 조건 | 점수 |
|------|------|------|
| repair | 내구도 < 40% | 100 |
| supply | 보급 < 30% | 90 |
| heal | 선원 피로도 > 70% | 60 |
| port_redirect | 항해 중 위험 감지 | 80 |
| emergency_sell | 골드 < 3000 | 85 |

## action 매핑

| goal | 액션 (우선순위) |
|------|----------------|
| repair | repair → depart(수리완료 후) |
| supply | supply → depart(보급완료 후) |
| heal | check-inn → rest |
| port_redirect | detect-port → dock → repair/supply |
| emergency_sell | sell-goods(시세 무관, 즉시현금화) |

## 이탈 조건
- 내구도 > 80% AND 보급 > 80% AND 골드 > 5000
- 이탈 시 이전 모드(prev_mode)로 복귀
- 이전 모드 없으면 MONEY_MAKING

## 금지 행동
- 원거리 항해 (최근 항구만)
- 투자, 장비 구매
- 위험 해역 진입
- 전투 (도주만)
- 모의전

## 예외 상황
- 항구 근처 없고 보급 고갈 → emergency-stop
- 골드 0 + 팔 물건 없 →Telegram 알림 → stopped
- 항구에 수리 불가(시설 없음) → 다음 항구로 이동
