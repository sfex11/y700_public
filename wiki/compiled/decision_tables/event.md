# Decision Table: event

state: event
description: 이벤트/팝업 화면. 오버레이 또는 전체화면 팝업.

## 팩트 체크리스트
- popup_type: 팝업 종류 (보상/공지/오류/점검/업데이트/발견물/퀘스트)
- has_confirm: 확인 버튼 여부
- has_cancel: 취소 버튼 여부
- is_reward: 보상 수령 가능 여부

## 모드 무관 (모든 모드 동일)

| goal | score_if | action |
|------|----------|--------|
| handle_maintenance | 점검 팝업=100 | handle-maintenance → stopped |
| handle_update | 업데이트 팝업=100 | handle-update → stopped |
| handle_error | 오류 팝업=90 | handle-error |
| collect_reward | 보상 팝업=85 | collect-reward / quest-complete |
| confirm_discovery | 발견물=80 | discovery |
| dismiss | default=50 | dismiss-generic |
| confirm_treasure | 보물=80 | treasure |

이벤트 상태는 모드와 무관하게 항상 팝업 처리가 최우선.
