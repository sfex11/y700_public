# Decision Table: in-port-harbor

state: in-port-harbor
description: 항구 전경과 시설 선택이 동시에 가능한 상태. 새 위키 구조의 `port-menu`에 가장 가깝다.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| open_departure | always=90 | tap-departure-office |
| dismiss_popup | popup_detected=100 | dismiss-generic |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | popup_detected=100 | dismiss-generic |
| sell | cargo_has_goods AND 시세>=110%=90 | enter-market → sell-goods |
| buy | cargo_ratio<80% AND gold>5000=80 | enter-market → buy-goods |
| open_departure | always=75 | tap-departure-office |
| depart | always=70 | depart |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| repair | durability<80%=100 | tap-departure-office → repair |
| supply | supply_water<80% OR supply_food<80%=100 | tap-departure-office → supply |
| depart_flee | durability<30%=95 | depart |
| open_departure_fallback | always=75 | tap-departure-office |
| depart_fallback | always=60 | depart |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| open_departure | always=85 | tap-departure-office |
| depart | always=80 | depart |

## 메모
- 교역소, 출항소, 조선소, 여관 등 시설 선택 전이가 핵심이다.
- 실제 세부 대화나 보급 패널이 열리면 `in-port-dialog`, `in-port-departure`, `in-port-warehouse`로 넘긴다.
