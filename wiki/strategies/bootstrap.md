---
type: strategy
mode: BOOTSTRAP
status: draft
updated: 2026-04-15
---

# Bootstrap — 초기 부팅 전략

## 목표
앱 실행부터 자동화 메인 루프 진입까지의 절차를 수행한다.

## 진입 조건
- 봇 시작 시 항상
- 앱 재시작 후
- 서버 점검 종료 후

## goal 점수표 (순차 실행)

| 단계 | goal | 점수 | 액션 |
|------|------|------|------|
| 1 | start | 100 | start-app |
| 2 | login | 100 | handle-login-popup |
| 3 | server | 100 | select-server |
| 4 | checkin | 90 | daily-checkin |
| 5 | dismiss_popups | 80 | dismiss-generic (반복) |
| 6 | assess | 70 | 현재 상태 판단 |

## 부팅 후 평가

로그인 완료 후 현재 상태 확인:
1. 항구 안 → in-port → MONEY_MAKING 또는 RECOVERY
2. 항해 중 → sailing → 현재 목적지 확인
3. 전투 중 → combat → COMBAT_ESCAPE 우선
4. 알 수 없음 → screenshot 분류 → recovery

## 이탈 조건
- 모든 팝업 처리 완료 + 현재 상태 확인 완료
- → 이전 모드(prev_mode) 복원
- → 이전 모드 없으면 MONEY_MAKING

## 실패 처리
- 앱 실행 실패 3회 → restart-app
- 로그인 실패 3회 → Telegram 알림 → stopped
- 서버 점검 중 → handle-maintenance → 대기
- 업데이트 필요 → handle-update → Telegram 알림 → stopped
