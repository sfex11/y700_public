# Phase 1: 인식 맵 문서화 — Claude Code 프롬프트

## 작업
각 액션마다 precondition(시작 전), execution(실행 중), verification(성공 판정) 3시점의
인식 요구사항을 YAML 형식으로 정의하시오.

## 대상 파일 (21개, 우선순위 1~6)
```
wiki/actions/login/start-app.md
wiki/actions/login/select-server.md
wiki/actions/login/handle-login-popup.md
wiki/actions/login/daily-checkin.md
wiki/actions/in-port/dock.md
wiki/actions/in-port/supply.md
wiki/actions/in-port/depart.md
wiki/actions/sailing/auto-sail.md
wiki/actions/sailing/detect-port.md
wiki/actions/sailing/handle-durability.md
wiki/actions/trade/enter-market.md
wiki/actions/trade/check-prices.md
wiki/actions/trade/buy-goods.md
wiki/actions/trade/sell-goods.md
wiki/actions/recovery/reconnect.md
wiki/actions/recovery/restart-app.md
wiki/actions/recovery/back-to-home.md
wiki/actions/popup/dismiss-generic.md
wiki/actions/popup/handle-error.md
wiki/actions/popup/handle-update.md
wiki/actions/popup/handle-maintenance.md
```

## 출력 형식
**단일 파일: wiki/compiled/recognition-map.md**

```yaml
# ============================================================
# 액션별 인식 매핑 (Recognition Map)
# ============================================================

- action: start-app
  category: login
  recognition:
    precondition:
      - id: app_not_running
        method: system  # adb shell pidof 등 시스템 명령
        target: "pidof com.bluepotion.uwom2 == empty"
        timeout: 2000
    execution:
      - id: splash_screen
        method: template
        target: login_splash_logo.png
        region: [300, 200, 700, 600]
        timeout: 15000
    verification:
      - id: title_or_login_screen
        method: template
        target: login_title_screen.png
        region: [0, 0, 1280, 800]
        timeout: 5000
        alternatives:
          - method: ocr
            target: "터치|시작|접속"
            region: [300, 600, 900, 780]

- action: select-server
  ...
```

## 작성 규칙

### method 종류
- `template`: 템플릿 이미지 매칭 (NCC)
- `ocr`: Tesseract 텍스트 인식
- `llm`: gemma 비전 판정 (최후 수단)
- `system`: adb shell 명령으로 확인
- `color`: 특정 영역 색상/히스토그램
- `pixel`: 특정 픽셀 좌표 색상 체크

### region 형식
[x, y, w, h] — 게임 해상도 1280x800 기준

### 각 액션 파일 읽기
반드시 각 액션 파일의 "단계", "전제조건", "확인" 섹션을 읽고
그 내용에 맞게 인식 항목을 도출할 것.

### 참고 checklists (이미 정의된 팩트 추출 항목)

**in-port state:**
- OCR: gold, durability, cargo_ratio, supply_water, supply_food
- Template: market_icon, shipyard_icon, inn_icon, guild_icon, palace_icon, depart_icon, quest_notification
- LLM: popup_overlays, event_banner, levelup_indicator

**sailing state:**
- OCR: speed, durability, supply_water, supply_food, weather_icon, minimap_position
- Template: auto_sail_icon, destination_marker, enemy_marker, port_icon
- LLM: sea_zone_type, disaster_popup

**trade state:**
- OCR: price_percent, gold, cargo_ratio, tariff_badge
- Template: buy_tab, sell_tab, invest_tab, trade_event_badge
- LLM: trade_event_type, bargain_available, item_category

**recovery state:**
- System: recovery_attempt, last_screenshot, app_running, network
- LLM: screen_classifiable, known_popup

### 주의사항
- template method의 target은 `파일명.png` 형식. 명명규칙: `{category}_{요소명}.png`
- OCR target은 정규식 패턴 허용 ("매입|매도")
- 가능하면 template > ocr > llm 순으로 우선 사용
- precondition은 가능하면 system/template로 확인
- verification은 반드시 1개 이상 정의 (성공 판정 필수)
- timeout은 ms 단위
- on_fail: 인식 실패 시 동작 (scroll_and_retry, wait_and_retry, abort 등)

## 최종 산출물
wiki/compiled/recognition-map.md 하나의 파일에 21개 액션 전부를 작성할 것.
