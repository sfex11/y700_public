# Decision Table: combat

state: combat
description: 전투 중. 화면: 헥스맵, HP바, 턴 순서.

## 팩트 체크리스트
- my_hp: 내 함대 HP (%)
- enemy_hp: 적 함대 HP (%)
- my_crew: 선원 수
- enemy_crew: 적 선원 수
- morale: 사기 게이지
- skill_cooldown: 스킬 쿨타임 상태
- turn_number: 현재 턴
- weather: 날씨 (전투 효과)
- ship_advantage: 상성 (범선>갤리>프리깃>범선)

## 모드별 goal 점수

### COMBAT_ACTIVE
| goal | score_if | action |
|------|----------|--------|
| use_skill | 쿨타임완료 AND 적HP>30%=80 | use-skill |
| boarding | enemy_crew<30% AND 근접=75 | boarding |
| attack_normal | always=60 | engage (기본공격) |
| collect | victory=100 | collect-reward |
| flee | my_hp<15%=90 | flee-battle |

### COMBAT_ESCAPE
| goal | score_if | action |
|------|----------|--------|
| flee | always=100 | flee-battle |
| delegate | flee_실패=80 | delegate-combat |
- 최대 도주 시도 3회, 이후 위임 전투

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| collect | victory=100 | collect-reward |
| flee_low_hp | my_hp<30%=95 | flee-battle |
| delegate_combat | consecutive_fails>=3=90 | delegate-combat |
| attack_normal | always=60 | engage |
| dismiss_popup | popup_detected=100 | dismiss-generic |
- RECOVERY 모드에서 전투 발생 시: 일단 전투를 종결시켜야 in-port/sailing 흐름으로 복귀 가능.
- 연속 실패 누적 시 위임 전투(자동전투)로 빠르게 종결.

## 상성 판단
- 내 함선이 상성 우위 → 공격 점수 +15
- 내 함선이 상성 열위 → 회피 점수 +15
- 날씨 효과 반영 (폭풍=백병+30%, 포격-20%)
