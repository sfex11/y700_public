# Decision Table: in-port-warehouse

state: in-port-warehouse
description: 창고/함대 관리 화면. 보급 후 이전 상태로 복귀.

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
