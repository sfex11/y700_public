# Decision Table: in-port-departure

state: in-port-departure
description: 출항소 준비 화면. 대화 정리, 보급/수리 진입, 최종 출항 판단을 담당한다.

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

## 메모
- 새 위키 구조 기준으로 `in-port-departure`는 출항소 패널, `in-port-warehouse`는 그 안의 보급/수리 패널이다.
- 출항 버튼이 보여도 대화 오버레이가 남아 있으면 `skip-dialog`를 우선한다.
