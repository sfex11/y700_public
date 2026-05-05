# Decision Table: in-port-dialog

state: in-port-dialog
description: NPC 대화 진행 중. 건너뛰기/자동진행으로 대화 종료 후 이전 상태로 복귀.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |
| resume_harbor_flow | harbor_reached=92 | tap-departure-office |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |
| resume_harbor_flow | harbor_reached=92 | tap-departure-office |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |
| resume_harbor_flow | harbor_reached=92 | tap-departure-office |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |
| resume_harbor_flow | harbor_reached=92 | tap-departure-office |

### COMBAT_ESCAPE
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |
| resume_harbor_flow | harbor_reached=92 | tap-departure-office |

### COMBAT_ACTIVE
| goal | score_if | action |
|------|----------|--------|
| skip_dialog | always=100 | skip-dialog |
| resume_harbor_flow | harbor_reached=92 | tap-departure-office |

## 공통
skip-dialog: 건너뛰기 또는 자동진행 버튼 탭 → 대화 종료
- recent loop처럼 `skip-dialog` 직후에도 `in-port-dialog`로 남지만 실제 대화 증거가 없고 `harbor_reached=true`면 false dialog로 보고 `tap-departure-office`로 즉시 복귀한다.
