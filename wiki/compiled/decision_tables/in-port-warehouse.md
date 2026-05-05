# Decision Table: in-port-warehouse

state: in-port-warehouse
description: 출항소 보급/수리/선원 보충 패널. 새 위키 구조의 `port-supply`에 해당한다.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| supply | always=100 | supply-fill |
| back | always=50 | back |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| supply | supply_water<90% OR supply_food<90%=100 | supply-fill |
| back | supply_water>=90% AND supply_food>=90%=90 | back |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| supply | always=100 | supply-fill |
| back | supply_done=90 | back |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| supply | always=100 | supply-fill |
| back | supply_done=90 | back |

### COMBAT_ESCAPE
| goal | score_if | action |
|------|----------|--------|
| back | always=100 | back |

## 메모
- 이름은 `warehouse`지만 실제 운영 의미는 출항 전 보급/수리 상태다.
- 물, 식량, 자재, 포탄, 선원, 내구도 점검 후 `in-port-departure` 또는 `in-port`로 복귀한다.
