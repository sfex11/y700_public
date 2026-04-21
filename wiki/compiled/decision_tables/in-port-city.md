# Decision Table: in-port-city

state: in-port-city
description: 도시 내부. 미니맵으로 항구 이동.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| open_minimap | always=100 | tap-minimap |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| open_minimap | always=100 | tap-minimap |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| open_minimap | always=100 | tap-minimap |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| open_minimap | always=100 | tap-minimap |
