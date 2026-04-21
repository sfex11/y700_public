# Decision Table: in-port-departure

state: in-port-departure
description: 출항소 화면. 대화 종료 후 보급 → 출항.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | dialog_active=100 | skip-dialog |
| supply | dialog_done AND supply_low=90 | open-supply |
| depart | dialog_done AND supply_ok=100 | depart |
| back | always=10 | back |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | dialog_active=100 | skip-dialog |
| supply | supply_water<80% OR supply_food<80%=95 | open-supply |
| depart | supply_ok=100 | depart |
| back | always=10 | back |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | dialog_active=100 | skip-dialog |
| supply | always=80 | open-supply |
| depart | always=90 | depart |
| back | supply_done=10 | back |

### COMBAT_ESCAPE
| goal | score_if | action |
|------|----------|--------|
| depart | always=100 | depart |
