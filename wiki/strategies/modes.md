---
type: meta
status: draft
updated: 2026-04-15
---

# 전략 모드 FSM — Strategic Mode State Machine

## 개요

게임 상태(states/)와 별개로, 봇의 "현재 전략 목표"를 관리하는 모드 FSM.
같은 게임 상태(in-port)에서도 전략 모드에 따라 완전히 다른 행동을 하게 만드는 핵심 레이어.

## 모드 정의 (6개)

### BOOTSTRAP — 초기 부팅
- 앱 실행 → 로그인 → 서버 선택 → 출석 → 기본 루프 진입
- 자동화 시작 시 항상 여기서 시작
- 곧바로 이전 모드 복원 또는 MONEY_MAKING으로 전이

### MONEY_MAKING — 수익 극대화 (기본 모드)
- 교역 루프: 시세 확인 → 매입 → 이동 → 매도 반복
- 투자 기회 있으면 투자
- 밀수 이익이 크면 밀수 경로
- 가장 오래 머무는 모드

### RECOVERY — 생존/복구
- 내구도, 보급, 자금이 위험 수준일 때
- 모든 수익 활동 중단, 가까운 항구로 이동
- 수리/보급 완료 후 이전 모드 복귀

### GROWTH — 성장 투자
- 여유 자금/자재가 있을 때 선박/항해사/장비 업그레이드
- 레벨업 기회, 스킬 승급, 선단 연구
- 성장 완료 또는 자원 부족 시 MONEY_MAKING 복귀

### COMBAT_ESCAPE — 전투 회피
- 약한 상태에서 적 조우 시 도주 우선
- 전투 불가피하면 위임 전투
- 위험 벗어나면 이전 모드 복귀

### COMBAT_ACTIVE — 전투 적극
- 전투로 이득을 얻을 수 있을 때 (해적 토벌, 퀘스트, 모의전)
- 스킬 적극 사용, 접현전 판단
- 전투 종료 후 이전 모드 복귀

## 전이도

```
              ┌─────────────┐
              │  BOOTSTRAP   │ ← 시작
              └──────┬──────┘
                     │ 부팅 완료
                     ▼
              ┌──────────────┐◄─────────────────┐
              │ MONEY_MAKING  │                  │
              └──┬─┬─┬─┬─────┘                  │
                 │ │ │ │                        │
    위험 탐지    │ │ │ │ 여유 자원              │
         ┌───────┘ │ │ └──────────┐             │
         ▼         │ │            ▼             │
  ┌──────────┐     │ │    ┌──────────┐          │
  │ RECOVERY  │────┘ │    │  GROWTH   │──────────┘
  └──────────┘ 복구완료    └──────────┘ 성장완료
                     │
         전투 상황   │
         ┌───────────┤
         ▼           ▼
  ┌────────────┐ ┌──────────────┐
  │COMBAT_ESCAPE│ │COMBAT_ACTIVE │
  └────────────┘ └──────────────┘
         │              │
         └──────┬───────┘
                │ 전투 종료
                ▼
           이전 모드 복귀
```

## 전이 조건

| From | To | 조건 |
|------|-----|------|
| BOOTSTRAP | MONEY_MAKING | 로그인+출석 완료, 자금 > 0 |
| BOOTSTRAP | RECOVERY | 로그인 후 내구도 < 30% |
| MONEY_MAKING | RECOVERY | 내구도 < 40% 또는 보급 < 20% 또는 골드 < 3000 |
| MONEY_MAKING | GROWTH | 골드 > 100000 + 내구도 > 70% + 적재 여유 |
| MONEY_MAKING | COMBAT_ESCAPE | 적 조회 + 내 함대 전투력 < 적 * 0.7 |
| MONEY_MAKING | COMBAT_ACTIVE | 적 조회 + 내 함대 전투력 > 적 * 1.3 |
| RECOVERY | MONEY_MAKING | 내구도 > 80% + 보급 > 80% + 골드 > 5000 |
| RECOVERY | GROWTH | 복구 완료 + 골드 > 80000 |
| GROWTH | MONEY_MAKING | 골드 < 30000 또는 업그레이드 가능 항목 없음 |
| GROWTH | RECOVERY | 내구도 < 40% 감지 |
| COMBAT_ESCAPE | 이전 모드 | 도주 성공 또는 전투 종료 |
| COMBAT_ACTIVE | 이전 모드 | 전투 승리 또는 패배 |
| COMBAT_ESCAPE | COMBAT_ACTIVE | 도주 실패, 전투 불가피 |
| COMBAT_ACTIVE | RECOVERY | 전투 패배 + 내구도 < 30% |

## 각 모드별 goal 집합

| 모드 | goal 우선순위 (높→낮) |
|------|----------------------|
| BOOTSTRAP | login > checkin > restore |
| MONEY_MAKING | survival > economy > growth |
| RECOVERY | survival >> safety > economy |
| GROWTH | survival > growth > economy |
| COMBAT_ESCAPE | survival > escape > safety |
| COMBAT_ACTIVE | victory > survival > reward |

## 모드별 금지 행동

| 모드 | 금지 |
|------|------|
| RECOVERY | 장기 투자, 원거리 항해, 위험 해역 진입 |
| COMBAT_ESCAPE | 적극 전투, 접현전, 모의전 |
| BOOTSTRAP | 교역, 전투, 항해 (로그인 절차만) |
| MONEY_MAKING | 불필요한 장비 구매, 위험 투자 |

## 런타임 모드 관리

```python
class StrategyMode:
    mode: str           # 현재 모드
    prev_mode: str      # 이전 모드 (전투/복구 후 복귀용)
    mode_since: float   # 모드 진입 시간
    facts: dict         # 현재 팩트 (gold, durability 등)
    
    def evaluate_transition(self, state, facts):
        """상태 전이 시점에만 호출"""
        ...
```

모드 전환은:
- 상태 전이 시점 (in-port 진입, 전투 진입 등)
- 액션 실패 N회 누적 시
- 타이머 (같은 모드 30분 이상 시 재평가)

매 프레임마다 호출하지 않는다.
