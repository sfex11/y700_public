# Decision Table: in-port-harbor

state: in-port-harbor
description: 항구 전경. 출항소/교역소 등 시설 선택.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| open_departure | always=90 | tap-departure-office |
| dismiss_popup | popup_detected=100 | dismiss-generic |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| sell | cargo_has_goods AND 시세>=110%=90 | enter-market → sell-goods |
| buy | cargo_ratio<80% AND gold>5000=80 | enter-market → buy-goods |
| depart | always=70 | depart |
| dismiss_popup | popup_detected=100 | dismiss-generic |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| repair | durability<80%=100 | tap-departure-office → repair |
| supply | supply_water<80% OR supply_food<80%=100 | tap-departure-office → supply |
| depart_flee | durability<30%=95 | depart |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| depart | always=80 | depart |
