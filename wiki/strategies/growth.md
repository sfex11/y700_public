---
type: strategy
mode: GROWTH
status: draft
updated: 2026-04-15
---

# Growth — 성장 투자 전략

## 목표
여유 자원으로 함대 전력을 영구적으로 강화한다.

## 진입 조건
- 골드 > 100000 + 내구도 > 70% + 적재 여유
- 레벨업 가능 (경험치 충분)
- 새 장비/항해사 획득 기회
- 선단 연구 완료 대기

## goal 점수표

| goal | 조건 | 점수 |
|------|------|------|
| level_up | 레벨업 가능 | 80 |
| upgrade_ship | 골드 > 80000 + 개조 가능 | 70 |
| upgrade_equip | 장비 강화 재료 충분 | 65 |
| hire_nav | 항해사 슬롯 여유 + 계약서 보유 | 60 |
| skill_up | 스킬북 보유 | 55 |
| research | 선단 연구 해금 | 50 |
| build_ship | 건조 가능 + 자재 충분 | 45 |
| research_fleet | 선단LV 충분 + 연구 미완료 | 40 |

## action 매핑

| goal | 액션 |
|------|------|
| level_up | level-up-navigator |
| upgrade_ship | visit-npc(조선소) → remodel-ship |
| upgrade_equip | visit-npc(조선소) → upgrade-equipment |
| hire_nav | visit-npc(여관) → hire-navigator → assign-navigator |
| skill_up | upgrade-skill |
| research | research-fleet |
| build_ship | visit-npc(조선소) → build-ship |
| research_fleet | research-fleet |

## 이탈 조건
- 골드 < 30000 → MONEY_MAKING
- 내구도 < 40% → RECOVERY
- 업그레이드 가능 항목 없음 → MONEY_MAKING

## 우선순위 가이드
1. 항해사 성장/고용 > 장비 > 선박 건조 (항해사가 ROI 최고)
2. 즉시 효과 있는 것 우선 (스킬 > 연구)
3. 파견함대 편성 → 수동 개입 없이도 패시브 수익

## 금지 행동
- 골드를 10000 이하로 소모 (비상금 유지)
- 내구도 낮은 상태에서 원거리 이동
- 검증 안 된 장비 세팅 (기존 세팅 백업 후 적용)
