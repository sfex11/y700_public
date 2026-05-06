# Decision Table: in-port-city

state: in-port-city
description: 도시 내부 이동 상태. 아직 핵심 시설 패널에 들어가지 않았거나 항구 전경으로 복귀해야 하는 중간 상태다.

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| back_to_harbor | always=100 | back |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| back_to_harbor | always=100 | back |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| back_to_harbor | always=100 | back |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| back_to_harbor | always=100 | back |

## 메모
- 새 위키 구조 기준으로는 `port-menu` 전 단계 또는 도시 내부 이동 단계에 해당한다.
- `in-port-city`에서는 시설 선택보다 항구 전경(`in-port-harbor`) 복귀를 우선한다.
- 미니맵 사용은 `in-port-harbor`처럼 항구 전경 증거가 있는 상태에서만 허용한다.
- 최근 장애 로그에서는 harbor 복구 문맥이 도시 내부 화면까지 과투영되어 `tap-departure-office`가 반복 제안됐다. 이 상태에서는 recent review의 출항소 문맥보다 현재 도시 내부 증거를 우선하며, 즉시 복구는 `back`이 기본이다.
- runtime도 같은 이유로 `in-port-city`에서는 goal scoring 전에 `back`을 직접 우선하도록 보강한다. 다른 AI가 이후 규칙을 바꾸더라도 이 안전 성격은 유지하는 편이 좋다.
