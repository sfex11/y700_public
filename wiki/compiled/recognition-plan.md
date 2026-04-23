---
type: plan
status: active
updated: 2026-04-15
---

# 액션별 인식 매핑 구축 계획

## 목표
64개 액션에 대해 "어떤 템플릿/OCR로 화면을 인식하고 판정하는가"를 정의하고,
실행 가능한 템플릿 DB까지 구축한다.

---

## 현재 자산 현황 (조사 필요)

### 템플릿 이미지
- templates/web-cropped/ — Claude Code로 Steam 스크린샷에서 84개 크롭
- templates/web-resources/ — 웹에서 수집 67개
- templates/ 기타 — 추출기 v2로 58개
- **총 ~209개. 단, 모바일 화면(PC 원격렌더링)과 매칭되는지 검증 필요**

### OCR
- Tesseract 5.5.2 (kor+eng), ~1초
- 게임 한글 인식 확인됨

### Wiki
- actions/ 64개 — 각 액션에 단계는 있으나 인식 요구사항(템플릿 매핑) 미정의
- states/ 8개 — 상태별 팩트 추출 항목 정의됨 (checklists/)
- compiled/checklists/ — OCR/템플릿 타겟 명시됨

---

## 작업 단계

### Phase 1: 인식 요구사항 정의 (문서 작업)
**산출물: wiki/compiled/recognition-map.md**

각 액션마다 3가지 시점의 인식 요구사항을 정의:

| 시점 | 의미 | 예시 (enter-market) |
|------|------|---------------------|
| precondition | 액션 시작 전 확인 | 도시 메인 화면 감지 |
| execution | 액션 수행 중 확인 | 교역소 아이콘 탐색 |
| verification | 성공/실패 판정 | 매입/매도 탭 표시 확인 |

각 항목은 다음 속성을 가짐:
- method: `template` | `ocr` | `llm` | `color` | `pixel`
- target: 템플릿명/OCR 영역/프롬프트
- region: 화면 영역 (x,y,w,h) — 가능하면
- timeout: 인식 대기 시간

### Phase 2: 템플릿 현황 매핑
**산출물: wiki/compiled/template-inventory.md**

Phase 1에서 정의한 인식 요구사항 대비:
- 기존 템플릿으로 커버 가능한 것 ✅
- 새로 수집해야 하는 것 ❌
- OCR로 대체 가능한 것 📝
- LLM 판정이 필요한 것 🤖

### Phase 3: 오버레이 APK + 텔레그램 승인 루프로 템플릿 수집
**산출물: templates/ 디렉토리 정비**

기존 손가락 커서 오버레이 APK를 확장하여,
회장님이 게임을 직접 플레이하면서 자연스럽게 템플릿을 수집하는 시스템.

#### 구성 요소

**1. 오버레이 APK (기존 + 확장)**
- 말풍선(bubble): 현재 위키 액션, 상태, "다음에 인식할 것" 표시
- 손가락 커서 tap 감지 시:
  - adb로 스크린샷 캡처
  - 탭 좌표 기준 영역 크롭 (64x64~128x128)
  - 크롭 이미지 + 메타데이터(액션명, 시점, 좌표)를 Python 서버로 전송

**2. Python 템플릿 수집 서버 (phone-agent/)**
- 오버레이로부터 크롭 이미지 수신
- 텔레그램 봇으로 회장님께 전송:
  - 크롭된 템플릿 이미지
  - "액션: enter-market / 시점: execution / 타겟: 교역소 아이콘"
  - 승인/수정/거절 버튼 (Telegram Inline Keyboard)
- 회장님 승인 시:
  - templates/ 에 파일 저장
  - 파일명 규칙: `{category}_{element}_{variant}.png`
  - recognition-map.md 자동 업데이트
- 회장님 "수정" 시:
  - 영역 조정값(가감) 받아서 재크롭 → 재전송

**3. 작업 흐름**
```
위키 액션 안내 → 말풍선 "교역소 아이콘을 탭하세요"
→ 회장님 탭 → 스크린샷+좌표 캡처
→ 크롭 이미지 생성
→ 텔레그램 전송: 이미지 + 메타 + [승인/수정/거절]
→ 회장님 승인 → templates/ 저장 + recognition-map 업데이트
→ 다음 인식 타겟 안내
```

**4. 메타데이터 (각 템플릿)**
```yaml
name: trade_market_icon
source_action: enter-market
recognition_point: execution
region: [x, y, w, h]  # 원본 화면 기준
resolution: 1280x800
collected_at: 2026-04-15
approved: true
threshold: 0.85  # NCC 매칭 임계값
```

### Phase 4: Python 런타임 구현
**산출물: src/ 디렉토리**

- template_matcher.py — NCC 템플릿 매칭 (PIL+numpy, OpenCV 불필요)
- ocr_reader.py — Tesseract 래퍼
- action_executor.py — 액션별 인식→실행→검증 루프
- recognition_map_loader.py — Phase 1 문서 로더

---

## 우선순위 (액션 그룹별)

봇이 가장 먼저 해야 하는 루프부터 구현:

| 순위 | 그룹 | 액션 수 | 이유 |
|------|------|---------|------|
| 1 | login | 4 | 부팅→로그인이 모든 것의 시작 |
| 2 | in-port (기본) | 3 | dock, supply, depart — 최소 루프 |
| 3 | sailing | 3 | auto-sail, detect-port, handle-durability |
| 4 | trade (핵심) | 4 | enter-market, check-prices, buy-goods, sell-goods |
| 5 | recovery | 3 | reconnect, restart-app, back-to-home |
| 6 | popup | 4 | dismiss-generic, handle-error, handle-update, handle-maintenance |
| 7 | combat | 5 | 전투 루프 |
| 8 | 나머지 | 38 | growth/고급 기능 |

---

## Phase 1 상세: 인식 맵 문서 구조

```yaml
# 예시: enter-market
action: enter-market
category: trade
recognition:
  precondition:
    - id: city_main_screen
      method: template
      target: port_city_main.png
      region: [0, 0, 1280, 800]
      timeout: 2000
  execution:
    - id: market_icon
      method: template
      target: trade_market_icon.png
      region: [200, 400, 800, 700]
      timeout: 3000
      on_fail: scroll_and_retry
  verification:
    - id: market_loaded
      method: template
      target: trade_market_tabs.png
      region: [0, 0, 400, 200]
      timeout: 3000
    - id: market_loaded_ocr
      method: ocr
      target: "매입|매도"
      region: [50, 50, 350, 150]
      fallback: true
```

---

## 예상 소요 시간

| Phase | 작업 | 예상 시간 |
|-------|------|-----------|
| Phase 1 | 인식 맵 문서화 (64개 액션) | Claude Code 1~2세션 |
| Phase 2 | 템플릿 현황 매핑 | 30분 |
| Phase 3 | 오버레이+텔레그램 템플릿 수집 | APK 확장 + Python 서버 구현 |
| Phase 4 | Python 런타임 | Claude Code 2~3세션 |

---

## 다음 행동
1. 회장님께 Phase 1 계획 검토 요청
2. 승인 후 Phase 1 우선순위 1~6번 그룹(22개 액션)부터 문서화 시작
3. templates/ 실제 자산 현황 조사 병행
