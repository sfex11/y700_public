# Decision Table: title

state: title
description: 게임 타이틀 화면. 접속 버튼 탭 후 항구 진입 대기.

## 팩트 체크리스트
- app_running: 게임 프로세스 실행 중
- popup_detected: 팝업/공지 표시 중

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | popup_detected=200 | handle-login-popup |
| tap_connect | always=150 | handle-title-connect |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | popup_detected=200 | handle-login-popup |
| tap_connect | always=150 | handle-title-connect |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | popup_detected=200 | handle-login-popup |
| tap_connect | always=150 | handle-title-connect |

## 검증 기준
- 성공: 로딩 화면 전이 또는 항구 화면 감지
- 실패: 타이틀 화면 유지
