---
type: strategy
mode: COMBAT_ACTIVE / COMBAT_ESCAPE
status: draft
updated: 2026-04-15
---

# Combat — 전투 전략

## 목표
전투 상황에서 최소 피해로 최대 이익을 얻는다.
적극 모드와 회피 모드를 전투력 비교로 결정.

## 모드 선택 기준

| 조건 | 모드 |
|------|------|
| 내 전투력 > 적 * 1.3 | COMBAT_ACTIVE |
| 내 전투력 < 적 * 0.7 | COMBAT_ESCAPE |
| 그 사이 | 전투 예측 결과에 따라 결정 |

## COMBAT_ACTIVE goal 점수표

| goal | 조건 | 점수 |
|------|------|------|
| win | 전투 중 | 90 |
| use_skill | 스킬 쿨타임 완료 | 70 |
| boarding | 적 HP < 30% + 근접 | 65 |
| capture | 적 선박 가치 > 수리비 | 55 |
| collect | 전투 승리 | 95 |

## COMBAT_ESCAPE goal 점수표

| goal | 조건 | 점수 |
|------|------|------|
| flee | 도주 가능 | 100 |
| delegate | 도주 불가 + 위험 | 80 |
| survive | HP < 20% | 95 |

## action 매핑

### COMBAT_ACTIVE
| goal | 액션 |
|------|------|
| win | engage-battle → use-skill (반복) |
| use_skill | use-skill |
| boarding | boarding |
| capture | collect-reward (선택: 포획) |
| collect | collect-reward |

### COMBAT_ESCAPE
| goal | 액션 |
|------|------|
| flee | flee-battle |
| delegate | delegate-combat |
| survive | flee-battle (최후) → recovery |

## 전투 판단 체크리스트

1. 적 함대 전투력 vs 내 함대 전투력 비교
2. 날씨 효과 확인 (폭풍: 포격 감소, 백병 증가)
3. 해역 특수 효과 (빙하: 충파 증가 등)
4. 모의전 티켓 잔여 확인 (모의전인 경우)
5. 내구도 확인 (전투 후 수리 비용 예측)

## 이탈 조건
- 전투 승리 → 이전 모드 복귀 + collect-reward
- 전투 패배 + 내구도 > 30% → 이전 모드 복귀
- 전투 패배 + 내구도 < 30% → RECOVERY
- 도주 성공 → 이전 모드 복귀

## 금지 행동
- COMBAT_ESCAPE에서 접현전 시도
- 내구도 < 30%에서 전투 계속
- PvP(모의전 아닌)에서 무리한 전투
