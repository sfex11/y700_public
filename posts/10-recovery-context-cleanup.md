---
layout: post
title: "복구 문맥 오염 정리: in-port-city vs in-port-harbor 혼란 해결"
date: 2026-04-29
tags: [debugging, gemmaton, maintenance, decision-table, precondition]
---

# 복구 문맥 오염 정리: in-port-city vs in-port-harbor 혼란 해결

## TL;DR

게임 자동화 런타임이 "정상 액션 선택 없이 Codex 진단만 반복"하는 현상을 분석하고 수정했다.
근본 원인은 단순한 OCR 오판이 아니라 **복구 정책 문맥 오염 + 실행 경로 결손 + 너무 좁은 precondition**이었다.
8개 파일을 수정하고 유지보수 스킬 2개를 신설했다.

## 증상

최근 `logs/runtime.log`에서 다음 패턴이 반복되었다:

1. 상태 분류 후 **정상 액션 선택 경로로 들어가지 않고** 매 루프 Codex 진단 발동
2. `in-port-city` 화면인데 이전 `in-port-harbor` 복구 문맥이 계속 남음
3. `back`이 정책상 선택되어도 `control.back()` 호출 오류로 크래시
4. `tap-departure-office`가 자주 선택되지만 `departure_office_visible` precondition에서 자주 막힘
5. `open-minimap` fallback도 검증 실패로 자주 막힘
6. Codex review가 문서만 남기고 현재 화면을 직접 풀지 못하는 구조적 한계

## 원인 분석

### 1. classification_conflict가 매 루프 Codex 진단을 발동

3개 분류기(OCR, 규칙, 템플릿)가 항상 의견이 달랐다:
- OCR: `in-port-harbor` (도시명만 보면 HARBOR → in-port-harbor 매핑)
- 규칙: `in-port` (형상 기반)
- 템플릿: `in-port` (패턴 기반)

앙상블 `top_gap ≤ 0.08`이면 `classification_conflict=True`가 되어 매 루프 Codex 진단이 발동했다.
하지만 불일치는 버그가 아니라 **정상적인 한계**였다 — 3개 분류기가 완벽히 일치할 수 없다.

### 2. OCR이 substate 판정 권한 초과

`in-port`(항구 전경) 화면에도 도시명("리스본" 등)이 표시되므로,
OCR이 도시명만 보면 무조건 `HARBOR → in-port-harbor`로 매핑했다.
하지만 항구 전경과 항구 시설 선택 화면 모두 도시명이 있으므로 OCR로는 구분이 불가능하다.

### 3. tap-departure-office 격리로 무한 open-minimap 루프

`tap-departure-office`가 5회 precondition 실패로 격리(QUARANTINED)되면,
`in-port-harbor` 상태의 폴백이 `open-minimap`만 남아 무한 루프에 빠졌다.

### 4. control.back() 경로 불일치 (structural bug)

`control.py`에 `back()` 메서드가 없는데 런타임에서 `control.back()`을 호출하고 있었다.

## 수정 내역

### 1. classification_conflict 진단 정책 재설계

**이전:** 불일치 시 매 루프 Codex 진단 발동 → 루프 블로킹 (40~60초/회)
**이후:** 불일치 시 최대 2회 진단, 성공 시 분류 갱신, 실패 시 종료

```python
# 분류 불일치 → 진단 → 액션 → 성공 시 분류 갱신 / 실패 시 재진단
if classification_conflict:
    if self._classification_conflict_retries < 2:
        diagnosis = await self._codex_diagnose_with_fallback(...)
        if diagnosis:
            continue  # 성공
    # 2회 소진 → 종료
    self.stop_event.set()
    break
```

### 2. HARBOR → in-port 매핑 변경

OCR은 coarse state만 반환하도록 매핑을 변경:
- `HARBOR: 'in-port'` (이전: `'in-port-harbor'`)

OCR은 substate 판정 권한이 없다. `in-port-harbor` 승격은 template/rule에서 담당.

### 3. 핵심 액션 격리 해제

`tap-departure-office`, `navigate-harbor`, `open-minimap`은 핵심 액션이므로
격리 대상에서 제외:

```python
core_actions = ("tap-departure-office", "navigate-harbor", "open-minimap")
if action in core_actions:
    logger.info(f"핵심 액션 격리 해제: {action}")
    quarantined = False
```

### 4. tap-departure-office precondition 완화

기존 `departure_office_visible` 단일 템플릿 의존도를 낮추고 fallback 체인 추가:
- `actions/departure_btn.png`
- `in-port/port_lighthouse_icon.png`
- 최종적으로 LLM YES/NO 판정

### 5. in-port-city 상태 안전 가드 추가

`select_action()` 초기에 `current_state == "in-port-city"`이면 `back` 우선.
도시 내부 화면에서 harbor 복구 문맥보다 back이 먼저다.

### 6. back() compatibility alias 추가

`control.py`에 `back()` → `go_back()` alias를 추가하여 structural bug 수정.

### 7. Codex 진단 프롬프트 응답 스키마 확장

기존 응답에 `changes_made`, `report`, `goal_progress`, `bottleneck` 필드를 추가.
이전에는 이 필드들이 요청에 없어서 텔레그램 보고에 항상 "보고 없음"이 나왔다.

### 8. 유지보수 스킬 신설

- **gemmaton-maintenance**: 유지보수 프로젝트 문맥, 파일 지도, 반복 장애 패턴
- **gemmaton-codex-diagnosis**: 즉시 진단 액션 JSON 계약에 슬림화

## 구조 변경 요약

```
이전: 분류 불일치 → Codex 진단 → 탭 → 성공해도 다음 루프에서 또 불일치 → 무한 진단
이후: 분류 불일치 → 진단 → 탭 → 성공 시 actual_state로 분류 갱신 → 불일치 해소
                               ↓ 실패
                         재진단 → 실패 → 종료
```

## 수정한 파일

| 파일 | 변경 |
|------|------|
| `src/runtime.py` | 진단 정책, 매핑, 격리 해제, precondition 완화, 안전 가드, 프롬프트 |
| `src/control.py` | `back()` compatibility alias |
| `src/runtime_supervisor.py` | 자가개선 직접 액션 실행 |
| `src/ocr_classifier.py` | HARBOR 매핑 참고 (runtime.py에서 매핑 변경) |
| `decision_tables/in-port.md` | 복구 문맥 메모 |
| `decision_tables/in-port-city.md` | back 우선 정책 명시 |
| `recognition-map-v2.md` | precondition fallback 체인 |
| `prompts/supervisor-review.md` | 직접 액션 JSON 스키마 |
| `~/.codex/skills/gemmaton-maintenance/` | 신설 (3파일) |
| `~/.codex/skills/gemmaton-codex-diagnosis/` | 슬림화 |

## 남은 리스크

1. **약한 모델에게 직접 액션 + 문서 수정 동시 요청은 부담이 큼** — 장기적으로 역할 분리 필요
2. **supervisor-review 직접 액션은 진단만큼 강한 검증을 하지 않음** — 로그로 성공률 점검 필요
3. **recognition-map-v2.md와 runtime patch 로직이 동시에 존재** — 나중에 의도가 어긋나면 혼란 가능

## 다음 작업

1. 실제 런타임 로그 1세션 재수집
2. supervisor-review 직접 액션 성공률 점검
3. 필요 시 약한 모델/강한 모델 역할 재분리
4. `review_context.json`에 "현재 화면 우선, 과거 review는 보조" 규칙 강화
