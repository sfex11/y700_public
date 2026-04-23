# Decision Table: in-port

state: in-port
description: 항구에 체류 중. 모든 시설 접근 가능.

## 팩트 체크리스트
- gold: 보유 골드
- durability: 내구도 (%)
- cargo_ratio: 적재율 (%)
- cargo_has_goods: 교역품 보유 여부
- supply_water: 물 잔량 (%)
- supply_food: 식량 잔량 (%)
- levelup_available: 레벨업 가능 여부
- skill_book_available: 스킬북 보유 여부
- contract_available: 항해사 계약서 보유 여부
- nav_slot_free: 항해사 슬롯 여유
- invest_cooldown: 투자 쿨타임 여부
- daily_quest_pending: 일일퀘스트 미수령
- dispatch_available: 파견함대 가동 가능

## 모드별 goal 점수

### BOOTSTRAP
|| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | always=200 | dismiss-generic |
| skip_dialog | always=190 | skip-dialog |
| depart_office | harbor_reached=100 | tap-departure-office |
| open_map | NOT harbor_reached=100 | open-minimap |

### MONEY_MAKING
|| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | always=200 | dismiss-generic |
| skip_dialog | always=190 | skip-dialog |
| depart_office | harbor_reached=100 | tap-departure-office |
| sell_high | cargo_has_goods AND 시세≥110%=80 | sell-goods |
| buy_cheap | cargo_ratio<90% AND gold>5000 AND 시세≤90%=75 | buy-goods |
| check_market | 시세_미확인=70 | check-prices |
| invest | gold>50000 AND NOT invest_cooldown=60 | invest |
| dispatch | dispatch_available=35 | dispatch |
| daily_quest | daily_quest_pending=50 | collect-daily-quest |

### RECOVERY
|| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | always=200 | dismiss-generic |
| skip_dialog | always=190 | skip-dialog |
| depart_office | harbor_reached=100 | tap-departure-office |
| open_map | NOT harbor_reached=100 | open-minimap |
| repair | durability<80% AND NOT harbor_reached=95 | repair |
| supply | (supply_water<80% OR supply_food<80%) AND NOT harbor_reached=90 | supply |
| emergency_sell | gold<3000=80 | sell-goods |
| heal | crew_fatigue>70%=60 | check-inn |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| level_up | levelup_available=80 | level-up-navigator |
| upgrade_ship | gold>80000 AND 개조가능=70 | remodel-ship |
| upgrade_equip | 강화재료_충분=65 | upgrade-equipment |
| hire_nav | contract_available AND nav_slot_free=60 | hire-navigator → assign-navigator |
| skill_up | skill_book_available=55 | upgrade-skill |
| build_ship | gold>100000 AND 자재충분=45 | build-ship |
| research | 선단연구_해금=50 | research-fleet |

### COMBAT_ESCAPE
| goal | score_if | action |
|------|----------|--------|
| depart_flee | always=90 | depart (도주 목적지) |
| repair_quick | durability<50%=70 | repair |

### COMBAT_ACTIVE
(항구에서는 발생하지 않음. 전투 종료 후 collect-reward → 이전 모드 복귀)

## 공통 골 게이트 (모든 모드)
survival 액션은 항상 최우선:
- durability<30% → repair (score=100, 모드 무관)
- supply<15% → supply (score=100, 모드 무관)
