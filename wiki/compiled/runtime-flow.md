---
type: meta
status: draft
updated: 2026-04-15
---

# Runtime Flow — 전체 실행 사이클

## 개요

gemma 4 초소형 모델을 사용한 게임 자동제어의 런타임 흐름.
각 단계에서 LLM이 하는 일과 하지 않는 일을 명확히 구분.

## 메인 루프

```
┌─────────────────────────────────────────────────┐
│                  MAIN LOOP                       │
│                                                  │
│  1. 스크린샷 캡처 (adb screencap)               │
│       ↓                                          │
│  2. 상태 분류 (gemma, ~1.4초)                    │
│       ↓                                          │
│  3. 팩트 추출 (OCR/템플릿 우선, gemma 보조)     │
│       ↓                                          │
│  4. 전략 모드 유지 또는 전환                     │
│       ↓                                          │
│  5. Decision Table → goal 점수 계산 (룰 엔진)   │
│       ↓                                          │
│  6. 최고 goal → 액션 후보 2~4개 선정            │
│       ↓                                          │
│  7. gemma 후보 선택 (~0.8초)                     │
│       ↓                                          │
│  8. 액션 실행 (ADB tap/swipe)                    │
│       ↓                                          │
│  9. 결과 확인 (성공/실패/상태변화)              │
│       ↓                                          │
│  10. 실패 누적 시 fallback / 성공 시 루프 계속  │
└─────────────────────────────────────────────────┘
```

## 단계별 상세

### 1. 스크린샷 캡처
```
adb exec-out screencap -p > /tmp/screen.png
소요: ~200ms
```

### 2. 상태 분류
- **입력**: 스크린샷
- **LLM**: gemma /completion 엔드포인트
- **프롬프트**: compiled/prompts/classify-state.md
- **출력**: state 이름 (sailing, in-port, trade, combat, event, recovery 중 하나)
- **소요**: ~1.4초
- **실패 시**: recovery 상태로 간주

### 3. 팩트 추출
우선순위:
1. **템플릿 매칭** (~50ms) — 버튼/아이콘 존재 여부
2. **OCR** (~1초) — 숫자 텍스트 (gold, HP, 시세)
3. **gemma 보조** (~1초) — OCR 실패 시 또는 모호할 때만

state별 추출 항목: compiled/checklists/{state}.md 참조

### 4. 전략 모드 판단
전략 모드 전환은 아래 시점에만 평가:
- 상태 전이 발생 시 (sailing → in-port 등)
- 액션 실패 3회 누적 시
- 같은 모드 30분 이상 유지 시

평가 방식: 하드코딩 조건문 (LLM 사용 안 함)
```python
if durability < 0.4 or supply < 0.2:
    mode = "RECOVERY"
elif gold > 100000 and durability > 0.7:
    mode = "GROWTH"
# ... strategies/modes.md 전이 조건 참조
```

### 5. Goal 점수 계산
룰 엔진이 compiled/decision_tables/{state}.md 기반으로 계산.

```python
goals = {}
for goal_name, conditions in decision_table[mode].items():
    score = 0
    for condition, value in conditions["score_if"]:
        if evaluate(condition, facts):
            score = max(score, value)
    if score > 0:
        goals[goal_name] = score

best_goal = max(goals, key=goals.get)
```

### 6. 액션 후보 선정
best_goal에 대응하는 액션 목록에서 2~4개 선택.
현재 팩트 기반으로 실행 불가능한 액션은 제외.

```python
candidates = action_map[best_goal][:4]
candidates = [a for a in candidates if prerequisites_met(a, facts)]
```

### 7. gemma 후보 선택
- **입력**: 스크린샷 + 후보 리스트
- **프롬프트**: compiled/prompts/select-action.md
- **출력**: 액션 번호 (1~4)
- **소요**: ~0.8초
- **후처리**: 숫자 아니면 candidates[0] 사용

### 8. 액션 실행
actions/{category}/{action}.md의 ADB 명령 순서대로 실행.

```python
for step in action.steps:
    if step.type == "tap":
        adb_shell(f"input tap {step.x} {step.y}")
        sleep(step.delay + random(300, 600))
    elif step.type == "swipe":
        adb_shell(f"input swipe {step.x1} {step.y1} {step.x2} {step.y2} {step.duration}")
```

랜덤 딜레이로 봇 탐지 회피.

### 9. 결과 확인
액션 완료 후 스크린샷 재촬영.
성공/실패 판정:
- **템플릿 매칭**: 예상 화면 요소 존재 확인
- **yes/no 프롬프트**: gemma가 성공 여부 판단 (~0.8초)
- **타임아웃**: 5초 내 변화 없으면 실패

### 10. 실패 처리
```
실패 1회: 동일 액션 재시도
실패 2회: 대체 액션 시도
실패 3회: 상위 goal의 다음 액션 시도
실패 5회: 전략 모드 재평가 → RECOVERY 또는 stopped
```

## 타이밍 요약

| 단계 | 방식 | 소요 |
|------|------|------|
| 스크린샷 | adb | 200ms |
| 상태 분류 | gemma | 1.4s |
| 팩트 추출 | OCR+템플릿 | 0.1~1s |
| 모드 판단 | 룰 엔진 | <1ms |
| goal 점수 | 룰 엔진 | <1ms |
| 후보 선택 | gemma | 0.8s |
| 액션 실행 | ADB | 1~5s |
| 결과 확인 | 템플릿/gemma | 0.1~0.8s |
| **총 1사이클** | | **4~10s** |

## gemma 호출 최소화

최소 모드 (안정적인 템플릿/OCR 충분 시):
- 상태 분류: 템플릿으로 대체 가능하면 생략
- 후보 선택: 후보 1개면 gemma 호출 생략
- 결과 확인: 템플릿으로 확인 가능하면 생략

이 경우 gemma 호출 0~1회/사이클, 총 소요 2~4초.

## 안전 규칙

1. **emergency-stop**: 모름 상태 3회 연속 → stopped
2. **자금 보호**: 골드 < 1000에서 소비 액션 금지
3. **내구도 보호**: 내구도 < 20%에서 항해 금지
4. **무한 루프 방지**: 같은 액션 10회 연속 → 모드 재평가
5. **Telegram 알림**: stopped, 큰 손실, 예외 상황

## 파일 참조 맵

```
런타임에서 읽는 파일:
- states/{state}.md          — 상태 정의
- strategies/modes.md        — 모드 전이 조건
- compiled/decision_tables/{state}.md — goal 점수 테이블
- compiled/checklists/{state}.md     — 팩트 추출 항목
- compiled/prompts/{type}.md         — gemma 프롬프트
- actions/{category}/{action}.md     — 액션 실행 절차

런타임에서 읽지 않는 파일 (참고용):
- strategies/{mode}.md       — 전략 설계서 (사람용)
- bot/                       — 제어 로직 설계서
```
