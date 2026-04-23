---
type: recognition
status: active
updated: 2026-04-17
version: 2.2
---

```yaml
# ============================================================
# 액션별 인식 매핑 v2.1 (Recognition Map)
# 해상도: 720x450 기준 (원본 3040x1904 → 리사이즈)
# method 우선순위: template > llm > ocr > system
# template 매칭 우선, LLM 비전은 fallback 및 실행 단계 좌표 탐색용
# ============================================================

# ────────────────────────────────────────────────────────────
# TIER 1: 핵심 게임 루프 (10개)
# ────────────────────────────────────────────────────────────

# ── CATEGORY: login ──

- action: start-app
  category: login
  recognition:
    precondition:
      - id: app_not_running
        method: system
        target: "pidof com.linegames.uwogl == empty"
        timeout: 2000
      - id: device_awake
        method: system
        target: "dumpsys power | grep 'mWakefulness=Awake'"
        timeout: 1000
    execution:
      - id: launch_app
        method: system
        target: "am start -n com.linegames.uwogl/.MainActivity"
        timeout: 5000
      - id: wait_loading
        method: system
        target: "sleep 10"
        timeout: 15000
    verification:
      - id: app_process_alive
        method: system
        target: "pidof com.linegames.uwogl != empty"
        timeout: 5000
      - id: title_logo_detected
        method: template
        target: "login/login__title_logo.png"
        region: [100, 20, 520, 180]
        threshold: 0.7
        timeout: 30000
        fallback:
          method: llm
          target: "게임 타이틀 화면이나 홈 화면이 보이는가?"
          region: [0, 0, 720, 450]
      - id: publisher_logos_detected
        method: template
        target: "login/login__publisher_logos.png"
        region: [200, 350, 520, 440]
        threshold: 0.65
        timeout: 5000
        fallback:
          method: llm
          target: "게임 타이틀 화면이나 홈 화면이 보이는가?"
          region: [0, 0, 720, 450]
    on_fail: wait_and_retry

- action: handle-login-popup
  category: login
  recognition:
    precondition:
      - id: popup_announcement
        method: template
        target: "popup/popup__announcement_banner_preseason.png"
        region: [60, 40, 660, 400]
        threshold: 0.6
        timeout: 5000
        fallback:
          method: template
          target: "popup/popup__benefits_panel_header.png"
          region: [60, 40, 660, 400]
          threshold: 0.6
          fallback:
            method: llm
            target: "화면에 팝업(약관동의, 공지사항, 이벤트배너)이 떠 있는가?"
            region: [0, 0, 720, 450]
      - id: popup_benefits
        method: template
        target: "popup/popup__benefits_tab_혜택.png"
        region: [60, 40, 660, 120]
        threshold: 0.6
        timeout: 3000
        fallback:
          method: llm
          target: "혜택/보상 팝업이 보이는가?"
          region: [0, 0, 720, 450]
    execution:
      - id: find_close_button
        method: llm
        target: "팝업의 닫기/확인/X 버튼 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
    verification:
      - id: popup_gone
        method: template
        target: "popup/popup__announcement_banner_preseason.png"
        region: [60, 40, 660, 400]
        threshold: 0.6
        expect_match: false
        timeout: 5000
        fallback:
          method: llm
          target: "팝업이 사라졌는가?"
          region: [0, 0, 720, 450]
    on_fail: wait_and_retry

# ── CATEGORY: in-port ──

- action: dock
  category: in-port
  recognition:
    precondition:
      - id: sailing_state
        method: llm
        target: "항해 중이고 근처에 항구가 보이는가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    execution:
      - id: tap_dock
        method: llm
        target: "항구 접안/입항 버튼의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
    verification:
      - id: port_main_ui_detected
        method: template
        target: "in-port/port_main_ui.png"
        region: [0, 0, 720, 450]
        threshold: 0.65
        timeout: 10000
        fallback:
          method: llm
          target: "항구 내부 화면(출항소, 여관 등 메뉴)이 표시되는가?"
          region: [0, 0, 720, 450]
      - id: city_label_lisbon
        method: template
        target: "in-port/in-port__city_label_lisbon.png"
        region: [0, 0, 300, 60]
        threshold: 0.6
        timeout: 5000
    on_fail: wait_and_retry

- action: supply
  category: in-port
  recognition:
    precondition:
      - id: in_port_detected
        method: template
        target: "in-port/port_main_ui.png"
        region: [0, 0, 720, 450]
        threshold: 0.65
        timeout: 10000
        fallback:
          method: template
          target: "in-port/in-port__city_label_lisbon.png"
          region: [0, 0, 300, 60]
          threshold: 0.6
          fallback:
            method: llm
            target: "항구 내부 화면인가?"
            region: [0, 0, 720, 450]
      - id: departure_office_visible
        method: template
        target: "in-port/in-port__departure_office_button.png"
        region: [0, 80, 720, 300]
        threshold: 0.6
        timeout: 5000
    execution:
      - id: find_supply
        method: llm
        target: "보급/출항소 메뉴의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
      - id: auto_supply
        method: llm
        target: "자동 보급 또는 보급 확인 버튼의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
    verification:
      - id: supply_done
        method: llm
        target: "보급이 완료되었는가? 물/식량이 가득 찼는가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    on_fail: wait_and_retry

- action: depart
  category: in-port
  recognition:
    precondition:
      - id: in_port_detected
        method: template
        target: "in-port/port_main_ui.png"
        region: [0, 0, 720, 450]
        threshold: 0.65
        timeout: 10000
        fallback:
          method: template
          target: "in-port/in-port__departure_office_button.png"
          region: [0, 80, 720, 300]
          threshold: 0.6
          fallback:
            method: llm
            target: "항구 내부 화면인가?"
            region: [0, 0, 720, 450]
    execution:
      - id: find_depart
        method: template
        target: "actions/departure_btn.png"
        region: [280, 60, 480, 120]
        threshold: 0.6
        timeout: 5000
        action: tap_center
        fallback:
          method: llm
          target: "출항 버튼이나 출항소 아이콘의 좌표를 찾아 탭하세요. JSON: [x,y]"
          region: [0, 0, 720, 450]
          timeout: 20000
      - id: confirm_depart
        method: llm
        target: "출항 확인 버튼이 나타나면 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
    verification:
      - id: sailing_started
        method: llm
        target: "항해 화면(바다, 나침반, HUD)이 표시되는가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    on_fail: wait_and_retry

# ── CATEGORY: sailing ──

- action: auto-sail
  category: sailing
  recognition:
    precondition:
      - id: sailing_or_port
        method: llm
        target: "항해 중이거나 항구에 있는가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    execution:
      - id: open_map
        method: llm
        target: "세계지도/항해 메뉴 버튼의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
      - id: set_destination
        method: llm
        target: "지도에서 다음 목적지 항구를 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
      - id: start_auto
        method: llm
        target: "자동 항해/위임 시작 버튼의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
    verification:
      - id: world_map_opened
        method: template
        target: "sailing/sailing__world_map_title.png"
        region: [200, 0, 520, 80]
        threshold: 0.65
        timeout: 5000
        fallback:
          method: llm
          target: "자동 항해가 진행 중인가? HUD에 항해 표시가 있는가?"
          region: [0, 0, 720, 450]
      - id: auto_sailing
        method: llm
        target: "자동 항해가 진행 중인가? HUD에 항해 표시가 있는가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    on_fail: wait_and_retry

- action: detect-port
  category: sailing
  recognition:
    precondition:
      - id: sailing_active
        method: template
        target: "sailing/sailing__world_map_title.png"
        region: [200, 0, 520, 80]
        threshold: 0.6
        timeout: 3000
        fallback:
          method: llm
          target: "항해 중인가?"
          region: [0, 0, 720, 450]
          timeout: 10000
    execution:
      - id: port_nearby
        method: llm
        target: "화면에 접안 가능한 항구 아이콘이 보이는가? YES 또는 NO만 답하세요."
        region: [0, 0, 720, 450]
        timeout: 15000
    verification: []
    on_fail: wait_and_retry

# ── CATEGORY: trade ──

- action: enter-market
  category: trade
  recognition:
    precondition:
      - id: in_port_city
        method: template
        target: "in-port/port_main_ui.png"
        region: [0, 0, 720, 450]
        threshold: 0.65
        timeout: 10000
        fallback:
          method: template
          target: "in-port/in-port__city_label_lisbon.png"
          region: [0, 0, 300, 60]
          threshold: 0.6
          fallback:
            method: llm
            target: "항구 도시 내부에 있는가?"
            region: [0, 0, 720, 450]
    execution:
      - id: find_market
        method: llm
        target: "교역소/시장 아이콘의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
    verification:
      - id: market_loaded
        method: llm
        target: "교역소 화면(매입/매도 탭, 물품 목록)이 표시되는가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    on_fail: scroll_and_retry

- action: sell-goods
  category: trade
  recognition:
    precondition:
      - id: in_market
        method: llm
        target: "교역소 화면인가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    execution:
      - id: tap_sell_tab
        method: llm
        target: "매도 탭의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
      - id: select_and_sell
        method: llm
        target: "물품을 선택하고 전량 매도 버튼을 탭하세요. 매도할 물품과 매도 버튼의 좌표를 순서대로 알려주세요. JSON: [[x,y],[x,y]]"
        region: [0, 0, 720, 450]
        timeout: 30000
    verification:
      - id: sell_complete
        method: llm
        target: "매도가 완료되었는가? 골드가 증가했는가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    on_fail: wait_and_retry

- action: buy-goods
  category: trade
  recognition:
    precondition:
      - id: in_market
        method: llm
        target: "교역소 화면인가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    execution:
      - id: tap_buy_tab
        method: llm
        target: "매입 탭의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
      - id: select_and_buy
        method: llm
        target: "시세가 낮은(90%이하) 물품을 찾아 선택하고 매입 버튼을 탭하세요. 물품과 매입 버튼의 좌표를 순서대로 알려주세요. JSON: [[x,y],[x,y]]"
        region: [0, 0, 720, 450]
        timeout: 30000
    verification:
      - id: buy_complete
        method: llm
        target: "매입이 완료되었는가? 적재량이 증가했는가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    on_fail: wait_and_retry

# ────────────────────────────────────────────────────────────
# TIER 2: 안전/복구 (5개)
# ────────────────────────────────────────────────────────────

# ── CATEGORY: recovery ──

- action: reconnect
  category: recovery
  recognition:
    precondition:
      - id: disconnect_detected
        method: template
        target: "popup/popup__security_violation_full.png"
        region: [60, 40, 660, 410]
        threshold: 0.6
        timeout: 5000
        fallback:
          method: template
          target: "recovery/recovery__security_violation_popup.png"
          region: [60, 40, 660, 410]
          threshold: 0.6
          fallback:
            method: llm
            target: "연결 끊김/재접속 팝업이 보이는가?"
            region: [0, 0, 720, 450]
      - id: app_running
        method: system
        target: "pidof com.linegames.uwogl != empty"
        timeout: 1000
    execution:
      - id: tap_reconnect
        method: template
        target: "recovery/recovery__security_violation_ok_btn.png"
        region: [200, 300, 520, 420]
        threshold: 0.65
        action: tap_center
        timeout: 10000
        fallback:
          method: llm
          target: "재접속/확인 버튼의 좌표를 찾아 탭하세요. JSON: [x,y]"
          region: [0, 0, 720, 450]
          timeout: 20000
    verification:
      - id: game_restored
        method: template
        target: "login/login__title_logo.png"
        region: [100, 20, 520, 180]
        threshold: 0.65
        timeout: 15000
        fallback:
          method: template
          target: "in-port/port_main_ui.png"
          region: [0, 0, 720, 450]
          threshold: 0.6
          fallback:
            method: llm
            target: "게임이 정상 복구되었는가?"
            region: [0, 0, 720, 450]
    on_fail: wait_and_retry

- action: restart-app
  category: recovery
  recognition:
    precondition:
      - id: app_state
        method: system
        target: "pidof com.linegames.uwogl"
        timeout: 1000
    execution:
      - id: force_stop
        method: system
        target: "am force-stop com.linegames.uwogl"
        timeout: 3000
      - id: wait
        method: system
        target: "sleep 3"
        timeout: 5000
      - id: relaunch
        method: system
        target: "am start -n com.linegames.uwogl/.MainActivity"
        timeout: 5000
    verification:
      - id: app_alive
        method: system
        target: "pidof com.linegames.uwogl != empty"
        timeout: 10000
      - id: title_screen_restored
        method: template
        target: "login/login__title_logo.png"
        region: [100, 20, 520, 180]
        threshold: 0.65
        timeout: 30000
        fallback:
          method: llm
          target: "게임 타이틀 화면이 표시되는가?"
          region: [0, 0, 720, 450]
    on_fail: abort

- action: back-to-home
  category: recovery
  recognition:
    precondition:
      - id: not_home
        method: template
        target: "in-port/port_main_ui.png"
        region: [0, 0, 720, 450]
        threshold: 0.65
        expect_match: false
        timeout: 5000
        fallback:
          method: llm
          target: "현재 게임 홈 화면이 아닌가?"
          region: [0, 0, 720, 450]
      - id: app_ok
        method: system
        target: "pidof com.linegames.uwogl != empty"
        timeout: 1000
    execution:
      - id: press_back
        method: system
        target: "input keyevent KEYCODE_BACK"
        timeout: 1000
    verification:
      - id: home_reached
        method: template
        target: "in-port/port_main_ui.png"
        region: [0, 0, 720, 450]
        threshold: 0.65
        timeout: 10000
        fallback:
          method: template
          target: "sailing/sailing__world_map_title.png"
          region: [200, 0, 520, 80]
          threshold: 0.6
          fallback:
            method: llm
            target: "게임 홈 화면에 도달했는가?"
            region: [0, 0, 720, 450]
    on_fail: wait_and_retry

- action: dismiss-generic
  category: popup
  recognition:
    precondition:
      - id: popup_visible
        method: rule
        rule: center_brightness > 120
        region: [100, 100, 620, 350]
        timeout: 1000
        fallback:
          method: template
          target: "popup/popup__announcement_banner_preseason.png"
          region: [60, 40, 660, 400]
          threshold: 0.55
          timeout: 5000
        fallback:
          method: template
          target: "popup/popup__exit_confirm_dialog.png"
          region: [100, 80, 620, 370]
          threshold: 0.6
          fallback:
            method: llm
            target: "화면에 팝업이나 다이얼로그가 떠 있는가?"
            region: [0, 0, 720, 450]
    execution:
      - id: find_close
        method: template
        target: "popup/popup_close_x.png"
        region: [100, 30, 710, 150]
        threshold: 0.5
        action: tap_center
        timeout: 10000
        fallback:
          method: template
          target: "popup/popup__announcement_close_X.png"
          region: [580, 30, 710, 80]
          threshold: 0.6
          action: tap_center
          fallback:
            method: template
            target: "popup/popup_confirm_btn.png"
            region: [200, 300, 520, 420]
            threshold: 0.6
            action: tap_center
            fallback:
              method: llm
              target: "닫기/확인/X 버튼의 좌표를 찾아 탭하세요. JSON: [x,y]"
              region: [0, 0, 720, 450]
              timeout: 20000
    verification:
      - id: popup_closed
        method: template
        target: "popup/popup__announcement_banner_preseason.png"
        region: [60, 40, 660, 400]
        threshold: 0.55
        expect_match: false
        timeout: 5000
        fallback:
          method: template
          target: "popup/popup__exit_confirm_dialog.png"
          region: [100, 80, 620, 370]
          threshold: 0.6
          expect_match: false
          fallback:
            method: llm
            target: "팝업이 닫혔는가?"
            region: [0, 0, 720, 450]
    on_fail: wait_and_retry

- action: handle-durability
  category: sailing
  recognition:
    precondition:
      - id: durability_low
        method: llm
        target: "선박 내구도가 낮은가? (게이지가 빨간색이거나 노란색인가?)"
        region: [0, 0, 720, 450]
        timeout: 10000
    execution:
      - id: find_repair
        method: llm
        target: "조선소/수리 메뉴의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
      - id: confirm_repair
        method: llm
        target: "수리 확인 버튼의 좌표를 찾아 탭하세요. JSON: [x,y]"
        region: [0, 0, 720, 450]
        timeout: 20000
    verification:
      - id: durability_ok
        method: llm
        target: "내구도가 회복되었는가? 게이지가 녹색인가?"
        region: [0, 0, 720, 450]
        timeout: 10000
    on_fail: wait_and_retry

- action: skip-dialog
  category: in-port-dialog
  recognition:
    precondition:
      - id: dialog_active
        method: template
        target: "dialog/dialog_auto_btn.png"
        region: [0, 0, 500, 150]
        threshold: 0.8
        timeout: 5000
        fallback:
          method: llm
          target: "NPC 대화창이 열려있는가?"
          region: [0, 0, 720, 450]
    execution:
      - id: tap_skip
        method: template
        target: "dialog/dialog_skip_btn.png"
        region: [350, 50, 500, 120]
        threshold: 0.8
        action: tap_center
        timeout: 10000
        fallback:
          method: template
          target: "dialog/dialog_auto_btn.png"
          region: [0, 0, 500, 150]
          threshold: 0.8
          action: tap_center
          fallback:
            method: llm
            target: "자동진행 또는 건너뛰기 버튼 좌표를 찾아 탭하세요. JSON: [x,y]"
            region: [0, 0, 720, 450]
            timeout: 20000
    on_fail: wait_and_retry

- action: tap-departure-office
  category: in-port-harbor
  recognition:
    precondition:
      - id: in_harbor
        method: template
        target: "in-port/port_lighthouse_icon.png"
        region: [0, 0, 500, 80]
        threshold: 0.8
        timeout: 5000
    execution:
      - id: find_departure
        method: template
        target: "in-port/in-port__departure_office_button.png"
        region: [0, 80, 720, 300]
        threshold: 0.6
        action: tap_center
        timeout: 10000
        fallback:
          method: llm
          target: "출항소 버튼 좌표를 찾아 탭하세요. JSON: [x,y]"
          region: [0, 0, 720, 450]
          timeout: 20000
    on_fail: wait_and_retry

- action: supply-fill
  category: in-port-warehouse
  recognition:
    precondition:
      - id: in_warehouse
        method: template
        target: "warehouse/wh_back_arrow.png"
        region: [0, 0, 500, 80]
        threshold: 0.7
        timeout: 5000
        fallback:
          method: template
          target: "warehouse/wh_supplies_section.png"
          region: [410, 30, 720, 115]
          threshold: 0.7
    execution:
      - id: tap_water
        method: template
        target: "warehouse/wh_water_slot.png"
        region: [420, 50, 560, 85]
        threshold: 0.5
        action: tap_center
        timeout: 5000
        fallback:
          method: system
          target: "input tap 2068 287"
          timeout: 3000
      - id: tap_food
        method: template
        target: "warehouse/wh_food_slot.png"
        region: [550, 50, 630, 85]
        threshold: 0.5
        action: tap_center
        timeout: 5000
        fallback:
          method: system
          target: "input tap 2491 287"
          timeout: 3000
    on_fail: wait_and_retry

- action: back
  category: in-port-warehouse
  recognition:
    precondition:
      - id: in_warehouse
        method: template
        target: "warehouse/wh_back_arrow.png"
        region: [0, 0, 500, 80]
        threshold: 0.7
        timeout: 3000
    execution:
      - id: tap_back
        method: template
        target: "warehouse/wh_back_arrow.png"
        region: [0, 0, 500, 80]
        threshold: 0.7
        action: tap_center
        timeout: 5000
        fallback:
          method: system
          target: "input keyevent KEYCODE_BACK"
          timeout: 3000
    on_fail: wait_and_retry

- action: open-minimap
  category: in-port
  recognition:
    execution:
      - id: tap_minimap
        method: template
        target: "actions/minimap_panel.png"
        region: [600, 50, 720, 130]
        threshold: 0.6
        action: tap_center
        timeout: 5000
        fallback:
          method: system
          target: "input tap 2828 384"
          timeout: 3000
    verification:
      - id: map_opened
        method: rule
        rule: state_is map-view
        timeout: 3000
    on_fail: skip  # 다음 루프에서 상태 분류로 확인

- action: detect-port
  category: sailing
  recognition:
    precondition: []  # 항해 중이면 항상 실행 가능
    execution:
      - id: open_map
        method: system
        target: "input tap 2828 384"
        timeout: 3000
      - id: wait_map
        method: wait
        timeout: 2000
    verification:
      - id: map_opened
        method: rule
        rule: state_is map-view
        timeout: 3000
    on_fail: skip

- action: dock
  category: map-view
  recognition:
    precondition: []
    execution:
      - id: tap_harbor
        method: system
        target: "input tap 1353 1521"
        timeout: 3000
    verification:
      - id: arrived_harbor
        method: rule
        rule: state_is in-port
        timeout: 8000
    on_fail: skip

- action: navigate-harbor
  category: map-view
  recognition:
    precondition:
      - id: map_visible
        method: template
        target: "map-view/world_map_btn.png"
        region: [0, 0, 720, 450]
        threshold: 0.7
        timeout: 5000
    execution:
      - id: tap_harbor
        method: template
        target: "actions/map_harbor_icon.png"
        region: [250, 300, 400, 400]
        threshold: 0.6
        action: tap_center
        timeout: 5000
        fallback:
          method: system
          target: "input tap 1353 1521"
          timeout: 3000
    verification:
      - id: arrived_harbor
        method: template
        target: "in-port/port_main_ui.png"
        region: [500, 30, 720, 120]
        threshold: 0.6
        timeout: 5000
    on_fail: wait_and_retry
```
