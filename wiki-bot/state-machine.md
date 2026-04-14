---
type: bot
status: draft
updated: 2026-04-14
sources: []
links: [bot/controllers/master-control.md, facts/systems/core-loop.md]
---

# State Machine — 마스터 상태기계

## 상태 (7개)

```
        ┌─────────┐
        │ Sailing │◄──────────┐
        └────┬────┘           │
             │ 항구 도착       │ 출항 완료
             ▼                │
        ┌─────────┐     ┌─────┴──────┐
        │ In-Port │────►│   Trade    │
        └────┬────┘     │ (Buy/Sell) │
             │          └────────────┘
             │ 이벤트/팝업
             ▼
        ┌─────────┐
        │  Event  │ (전투/팝업/이벤트)
        └────┬────┘
             │ 실패/오류
             ▼
        ┌─────────┐
        │Recovery │
        └────┬────┘
             │ 복구 성공 → 이전 상태로
             │ 3회 실패
             ▼
        ┌─────────┐
        │ Stopped │
        └─────────┘

   ┌─────────┐
   │  Idle   │ (사용자 명령 대기)
   └─────────┘
```

## 상태 정의

### Sailing
- 항해 중. 목표 항구 방향 이동.
- 감지: 바다 화면, 미니맵, 목표 항구 거리
- 전이: 항구 근접 → In-Port, 이벤트 → Event

### In-Port
- 항구에 도착한 상태. 다음 행동 결정.
- 감지: 항구 UI, NPC 목록
- 전이: 교역소 선택 → Trade, 출항 → Sailing, 팝업 → Event

### Trade
- 교역 중. 매입 또는 매도.
- 감지: 교역소 화면, 가격 표시, 인벤토리
- 전이: 완료 → In-Port, 실패 → Recovery

### Event
- 전투, 팝업, 이벤트 발생.
- 감지: 전투 UI, 팝업 창, 이벤트 알림
- 전이: 처리 완료 → 이전 상태, 실패 → Recovery

### Recovery
- 오류 복구 중.
- 규칙: 최대 3회 시도. 성공 → 이전 상태. 실패 → Stopped.
- 항상 복구 로그 기록.

### Idle
- 사용자 명령 대기. 수동 모드.
- 전이: 사용자 start 명령 → Sailing 또는 In-Port

### Stopped
- 안전 정지. 사람 개입 필요.
- 전이: 사용자 resume 명령 → Idle

## 전이 규칙

| 현재 상태 | 조건 | 다음 상태 |
|----------|------|----------|
| Sailing | 항구 근접 | In-Port |
| Sailing | 전투 발생 | Event |
| Sailing | 팝업 | Event |
| In-Port | 교역소 탭 | Trade |
| In-Port | 출항 버튼 | Sailing |
| In-Port | 팝업 | Event |
| Trade | 매입/매도 완료 | In-Port |
| Trade | 에러 | Recovery |
| Event | 처리 완료 | 이전 상태 |
| Event | 처리 실패 | Recovery |
| Recovery | 성공 | 이전 상태 |
| Recovery | 3회 실패 | Stopped |

## Boot/Login

상태기계 밖에서 처리:
1. 앱 실행
2. 로그인 화면 감지
3. 로그인 (자동 또는 수동)
4. 캐릭터 선택 → In-Port 또는 Sailing으로 진입
