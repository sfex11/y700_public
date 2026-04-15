# Decision Table: recovery / stopped

state: recovery / stopped
description: 복구 모드 또는 완전 정지.

## recovery

### 팩트 체크리스트
- last_known_state: 마지막으로 알던 상태
- recovery_attempt: 복구 시도 횟수
- screenshot_classifiable: 화면 분류 가능 여부

| goal | score_if | action |
|------|----------|--------|
| reclassify | screenshot_classifiable=90 | 스크린샷 → state 분류 |
| back_button | NOT classifiable AND attempt<3=80 | back-to-home |
| restart | attempt≥3=95 | restart-app |
| stop | restart 실패 3회=100 | → stopped |

## stopped

봇 완전 정지. 사람 개입 필요.

| 원인 | 복구 방법 |
|------|-----------|
| recovery 3회 실패 | 사람이 직접 게임 확인 후 재시작 |
| 서버 점검 | 점검 종료 후 수동 재시작 |
| 업데이트 필요 | 수동 업데이트 후 재시작 |
| 네트워크 장애 | 네트워크 복구 후 재시작 |
| 골드 0 + 팔 물건 없 | 사람이 자금 지원 또는 전략 변경 |

모든 stopped 원인은 Telegram 알림 발송.
