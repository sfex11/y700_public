# Decision Table: map-view

state: map-view
description: 도시 내 미니맵/지도 화면. 건물 아이콘으로 이동 가능.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| go_harbor | always=100 | navigate-harbor |
| go_warehouse | always=80 | navigate-warehouse |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| go_trading_post | always=90 | navigate-trading |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| go_harbor | always=100 | navigate-harbor |
