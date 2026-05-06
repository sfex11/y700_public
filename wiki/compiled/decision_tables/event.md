# Decision Table: event

state: event
description: 이벤트/팝업 화면. 오버레이 또는 전체화면 팝업.

## 팩트 체크리스트
- popup_overlay: 팝업/오버레이가 보이는가
- event_hub_overlay: 출석/공지/업데이트 탭이 함께 보이는 이벤트 허브인가
- alert_popup: 확인/재접속/오류형 경고 팝업인가
- event_stable_3plus: popup_overlay 팩트가 약해도 event 상태가 3루프 이상 안정적으로 유지됐는가

### RECOVERY

| goal | score_if | action |
|------|----------|--------|
| close_event_hub | event_hub_overlay=100 | close-event-hub |
| dismiss_alert | alert_popup=88 | dismiss-generic |
| dismiss_popup_fallback | popup_overlay=60 | dismiss-generic |
| dismiss_low_conf_stable | event_stable_3plus=42 | dismiss-generic |
| back_low_conf_stable | event_stable_3plus=36 | back |

### BOOTSTRAP

| goal | score_if | action |
|------|----------|--------|
| close_event_hub | event_hub_overlay=100 | close-event-hub |
| dismiss_alert | alert_popup=88 | dismiss-generic |
| dismiss_popup_fallback | popup_overlay=60 | dismiss-generic |
| dismiss_low_conf_stable | event_stable_3plus=42 | dismiss-generic |
| back_low_conf_stable | event_stable_3plus=36 | back |

### MONEY_MAKING

| goal | score_if | action |
|------|----------|--------|
| close_event_hub | event_hub_overlay=100 | close-event-hub |
| dismiss_alert | alert_popup=88 | dismiss-generic |
| dismiss_popup_fallback | popup_overlay=60 | dismiss-generic |
| dismiss_low_conf_stable | event_stable_3plus=42 | dismiss-generic |
| back_low_conf_stable | event_stable_3plus=36 | back |

이벤트 허브형 오버레이는 내부 탭 라벨에 `공지사항`/`업데이트`가 포함되어도 실제 점검 팝업으로 간주하지 않는다.
좌측 출석 목록과 중앙 공지 패널이 함께 있는 허브는 내부 공지 패널보다 외부 우상단 닫기 버튼으로 전체 허브를 우선 닫는다.
일반 확인형/재접속형 팝업만 `dismiss-generic`으로 처리하고, 오버레이 증거만 있을 때는 보조 fallback으로만 사용한다.
RECOVERY 모드에서는 `popup_overlay`가 비어 있어도 `event_stable_3plus`가 참이면 저신뢰 fallback으로 `dismiss-generic` 또는 `back`을 허용한다.
