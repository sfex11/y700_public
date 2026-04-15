# Checklist: recovery
state: recovery

## 시스템 상태
- recovery_attempt: 복구 시도 횟수 (내부 카운터)
- last_screenshot: 마지막 스크린샷 분류 가능 여부
- app_running: 앱 프로세스 실행 여부
- network: 네트워크 연결 상태

## LLM 판정
- screen_classifiable: 현재 화면이 알려진 state인지
- known_popup: 알려진 팝업인지
