# Decision Table: in-port-dialog

state: in-port-dialog
description: NPC 대화 진행 중. 건너뛰기/자동진행으로 대화 종료 후 이전 상태로 복귀.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |

### COMBAT_ESCAPE
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |

### COMBAT_ACTIVE
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |

## 공통
skip-dialog: 건너뛰기 또는 자동진행 버튼 탭 → 대화 종료
