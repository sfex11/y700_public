---
type: recognition
status: draft
updated: 2026-04-15
---

```yaml
# ============================================================
# 액션별 인식 매핑 (Recognition Map)
# 해상도: 1280x800 기준
# method 우선순위: template > ocr > color > pixel > system > llm
# ============================================================

# ────────────────────────────────────────────────────────────
# CATEGORY: login (우선순위 1)
# ────────────────────────────────────────────────────────────

- action: start-app
  category: login
  recognition:
    precondition:
      - id: app_not_running
        method: system
        target: "pidof com.bluepotion.uwom2 == empty"
        timeout: 2000
      - id: device_awake
        method: system
        target: "dumpsys power | grep 'mWakefulness=Awake'"
        timeout: 1000
    execution:
      - id: splash_screen
        method: template
        target: login_splash_logo.png
        region: [300, 200, 680, 400]
        timeout: 15000
      - id: loading_indicator
        method: template
        target: login_loading_bar.png
        region: [340, 650, 600, 50]
        timeout: 20000
    verification:
      - id: title_screen
        method: template
        target: login_title_screen.png
        region: [0, 0, 1280, 800]
        timeout: 25000
        alternatives:
          - method: ocr
            target: "터치|시작|접속|START"
            region: [340, 600, 600, 150]
      - id: app_process_alive
        method: system
        target: "pidof com.bluepotion.uwom2 != empty"
        timeout: 1000
    on_fail: wait_and_retry  # 5초 후 재시도, 최대 3회

- action: select-server
  category: login
  recognition:
    precondition:
      - id: server_list_visible
        method: template
        target: login_server_list_header.png
        region: [200, 50, 880, 100]
        timeout: 3000
        alternatives:
          - method: ocr
            target: "서버|선택|SERVER"
            region: [200, 50, 880, 100]
    execution:
      - id: target_server_found
        method: ocr
        target: "${target_server_name}"
        region: [200, 150, 880, 550]
        timeout: 3000
      - id: connect_button
        method: template
        target: login_connect_btn.png
        region: [350, 650, 580, 100]
        timeout: 2000
    verification:
      - id: login_screen_or_home
        method: template
        target: login_character_screen.png
        region: [0, 0, 1280, 800]
        timeout: 5000
        alternatives:
          - method: ocr
            target: "접속 중|로딩|게임 시작"
            region: [300, 600, 680, 150]
    on_fail: scroll_and_retry  # 서버 목록 스크롤 후 재시도

- action: handle-login-popup
  category: login
  recognition:
    precondition:
      - id: popup_overlay_present
        method: llm
        target: "화면에 약관 동의, 공지사항, 또는 이벤트 배너 팝업이 보이는가?"
        region: [0, 0, 1280, 800]
        timeout: 3000
        alternatives:
          - method: template
            target: login_popup_frame.png
            region: [100, 50, 1080, 700]
    execution:
      - id: terms_checkbox
        method: template
        target: login_terms_agree_all.png
        region: [100, 400, 600, 200]
        timeout: 2000
      - id: confirm_button
        method: template
        target: popup_confirm_btn.png
        region: [350, 600, 580, 100]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "동의|확인|닫기"
            region: [350, 600, 580, 100]
      - id: close_x_button
        method: template
        target: popup_close_x.png
        region: [850, 50, 100, 100]
        timeout: 1000
      - id: notice_close_button
        method: template
        target: login_notice_close.png
        region: [350, 650, 580, 80]
        timeout: 1500
        alternatives:
          - method: ocr
            target: "닫기|CLOSE"
            region: [350, 650, 580, 80]
    verification:
      - id: home_screen_reached
        method: template
        target: home_main_menu.png
        region: [0, 700, 1280, 100]
        timeout: 5000
        alternatives:
          - method: llm
            target: "게임 홈 화면(메인 메뉴)이 표시되어 있는가?"
            region: [0, 0, 1280, 800]
      - id: no_popup_remaining
        method: template
        target: popup_overlay_dim.png
        region: [0, 0, 1280, 800]
        timeout: 1000
        expect: absent  # 팝업 배경 딤 없어야 성공
    on_fail: wait_and_retry  # 닫기/X 버튼 재tap, 최대 5회

- action: daily-checkin
  category: login
  recognition:
    precondition:
      - id: home_screen_active
        method: template
        target: home_main_menu.png
        region: [0, 700, 1280, 100]
        timeout: 3000
      - id: checkin_popup_or_icon
        method: template
        target: login_checkin_popup.png
        region: [100, 50, 1080, 700]
        timeout: 3000
        alternatives:
          - method: template
            target: login_checkin_icon.png
            region: [1100, 50, 150, 150]
    execution:
      - id: reward_claim_button
        method: template
        target: login_checkin_claim_btn.png
        region: [400, 500, 480, 100]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "수령|받기|CLAIM"
            region: [400, 500, 480, 100]
      - id: confirm_reward
        method: template
        target: popup_confirm_btn.png
        region: [450, 600, 380, 80]
        timeout: 1500
    verification:
      - id: reward_claimed
        method: ocr
        target: "수령 완료|완료|보상 획득"
        region: [300, 300, 680, 200]
        timeout: 3000
        alternatives:
          - method: template
            target: login_checkin_claimed_stamp.png
            region: [400, 400, 480, 200]
      - id: popup_closed
        method: template
        target: home_main_menu.png
        region: [0, 700, 1280, 100]
        timeout: 3000
    on_fail: wait_and_retry  # 이미 수령한 경우 닫기 처리

# ────────────────────────────────────────────────────────────
# CATEGORY: in-port (우선순위 2)
# ────────────────────────────────────────────────────────────

- action: dock
  category: in-port
  recognition:
    precondition:
      - id: sailing_state
        method: template
        target: sailing_hud_frame.png
        region: [0, 0, 1280, 100]
        timeout: 2000
      - id: port_icon_visible
        method: template
        target: sailing_port_icon.png
        region: [0, 0, 1280, 800]
        timeout: 4000
        alternatives:
          - method: llm
            target: "화면에 항구 아이콘이나 접안 가능 표시가 보이는가?"
            region: [0, 0, 1280, 800]
    execution:
      - id: dock_confirm_popup
        method: template
        target: port_dock_confirm_btn.png
        region: [350, 450, 580, 100]
        timeout: 3000
        alternatives:
          - method: ocr
            target: "입항|정박|확인"
            region: [350, 450, 580, 100]
    verification:
      - id: port_ui_loaded
        method: template
        target: port_main_ui.png
        region: [0, 0, 1280, 800]
        timeout: 5000
        alternatives:
          - method: template
            target: port_depart_icon.png
            region: [900, 600, 200, 150]
          - method: ocr
            target: "출항소|여관|조선소|교역소"
            region: [0, 200, 1280, 500]
      - id: port_name_displayed
        method: ocr
        target: ".+"
        region: [500, 10, 280, 50]
        timeout: 2000
    on_fail: wait_and_retry  # 범위 밖이면 항구 방향 이동 후 재시도

- action: supply
  category: in-port
  recognition:
    precondition:
      - id: docked_state
        method: template
        target: port_main_ui.png
        region: [0, 0, 1280, 800]
        timeout: 2000
      - id: departure_office_or_supply
        method: template
        target: port_departure_office.png
        region: [800, 500, 300, 200]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "출항소|보급"
            region: [0, 200, 1280, 500]
      - id: gold_sufficient
        method: ocr
        target: "\\d+"
        region: [1050, 10, 200, 40]
        timeout: 1500
    execution:
      - id: supply_screen_visible
        method: template
        target: port_supply_tab.png
        region: [100, 100, 400, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "보급|물|식량|자재|포탄"
            region: [100, 100, 600, 400]
      - id: auto_supply_button
        method: template
        target: port_auto_supply_btn.png
        region: [800, 600, 300, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "자동 보급|자동"
            region: [800, 600, 300, 80]
    verification:
      - id: supply_values_changed
        method: ocr
        target: "\\d+/\\d+"
        region: [400, 200, 400, 400]
        timeout: 2000
      - id: water_filled
        method: ocr
        target: "\\d+"
        region: [500, 220, 150, 40]
        timeout: 1500
      - id: food_filled
        method: ocr
        target: "\\d+"
        region: [500, 280, 150, 40]
        timeout: 1500
    on_fail: wait_and_retry  # 두카트 부족 시 교역 선행

- action: depart
  category: in-port
  recognition:
    precondition:
      - id: docked_state
        method: template
        target: port_main_ui.png
        region: [0, 0, 1280, 800]
        timeout: 2000
      - id: depart_icon_visible
        method: template
        target: port_depart_icon.png
        region: [900, 600, 200, 150]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "출항소|출항"
            region: [800, 600, 300, 150]
      - id: durability_above_zero
        method: ocr
        target: "\\d+"
        region: [1000, 50, 150, 30]
        timeout: 1500
    execution:
      - id: departure_office_entered
        method: template
        target: port_departure_screen.png
        region: [0, 0, 1280, 800]
        timeout: 2000
      - id: depart_button
        method: template
        target: port_depart_btn.png
        region: [500, 600, 280, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "출항|DEPART"
            region: [500, 600, 280, 80]
      - id: supply_warning_check
        method: template
        target: port_supply_warning_popup.png
        region: [200, 200, 880, 400]
        timeout: 1000
        expect: absent  # 보급 부족 경고 없어야 함
    verification:
      - id: worldmap_visible
        method: template
        target: sailing_hud_frame.png
        region: [0, 0, 1280, 100]
        timeout: 5000
        alternatives:
          - method: template
            target: sailing_auto_sail_icon.png
            region: [1100, 700, 150, 80]
      - id: sailing_state_active
        method: ocr
        target: "\\d+\\.\\d+"
        region: [50, 10, 200, 40]
        timeout: 3000
    on_fail: wait_and_retry  # 보급 부족 경고 시 supply 액션 선행

# ────────────────────────────────────────────────────────────
# CATEGORY: sailing (우선순위 3)
# ────────────────────────────────────────────────────────────

- action: auto-sail
  category: sailing
  recognition:
    precondition:
      - id: worldmap_or_port_screen
        method: template
        target: sailing_hud_frame.png
        region: [0, 0, 1280, 100]
        timeout: 2000
        alternatives:
          - method: template
            target: port_main_ui.png
            region: [0, 0, 1280, 800]
      - id: supplies_sufficient
        method: ocr
        target: "\\d+"
        region: [200, 10, 300, 40]
        timeout: 2000
      - id: current_position
        method: ocr
        target: "\\d+\\.\\d+"
        region: [50, 10, 200, 40]
        timeout: 2000
    execution:
      - id: worldmap_opened
        method: template
        target: sailing_worldmap_frame.png
        region: [0, 0, 1280, 800]
        timeout: 3000
      - id: destination_selected
        method: template
        target: sailing_destination_marker.png
        region: [0, 0, 1280, 800]
        timeout: 3000
      - id: auto_sail_button
        method: template
        target: sailing_auto_sail_btn.png
        region: [400, 650, 480, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "자동 항해|자동항해|AUTO"
            region: [400, 650, 480, 80]
      - id: delegate_button
        method: template
        target: sailing_delegate_btn.png
        region: [400, 700, 480, 60]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "부관 위임|위임"
            region: [400, 700, 480, 60]
      - id: delegate_confirm
        method: template
        target: popup_confirm_btn.png
        region: [450, 450, 380, 80]
        timeout: 2000
    verification:
      - id: sailing_in_progress
        method: template
        target: sailing_auto_sail_icon.png
        region: [1100, 700, 150, 80]
        timeout: 3000
        alternatives:
          - method: ocr
            target: "항해 중|자동 항해"
            region: [900, 700, 350, 80]
      - id: delegate_toast
        method: ocr
        target: "위임 완료|위임"
        region: [300, 350, 680, 100]
        timeout: 3000
    on_fail: wait_and_retry  # 보급 부족 팝업 시 supply 호출

- action: detect-port
  category: sailing
  recognition:
    precondition:
      - id: sailing_active
        method: template
        target: sailing_hud_frame.png
        region: [0, 0, 1280, 100]
        timeout: 2000
      - id: at_sea
        method: template
        target: sailing_auto_sail_icon.png
        region: [1100, 700, 150, 80]
        timeout: 2000
    execution:
      - id: port_icon_detected
        method: template
        target: sailing_port_icon.png
        region: [0, 100, 1280, 600]
        timeout: 4000
        alternatives:
          - method: llm
            target: "화면에 접안 가능한 항구 아이콘이 보이는가?"
            region: [0, 0, 1280, 800]
      - id: dock_button_appeared
        method: template
        target: sailing_dock_btn.png
        region: [400, 600, 480, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "접안|입항|DOCK"
            region: [400, 600, 480, 80]
      - id: supply_check_water
        method: ocr
        target: "\\d+"
        region: [200, 10, 100, 30]
        timeout: 1500
      - id: supply_check_food
        method: ocr
        target: "\\d+"
        region: [320, 10, 100, 30]
        timeout: 1500
      - id: durability_check
        method: ocr
        target: "\\d+"
        region: [1000, 50, 150, 30]
        timeout: 1500
      - id: durability_color
        method: color
        target: "red_dominant"  # < 20% 빨간색
        region: [1000, 50, 150, 15]
        timeout: 500
    verification:
      - id: port_ui_loaded
        method: template
        target: port_main_ui.png
        region: [0, 0, 1280, 800]
        timeout: 5000
        alternatives:
          - method: ocr
            target: "출항소|여관|조선소|교역소"
            region: [0, 200, 1280, 500]
    on_fail: wait_and_retry  # 2000ms 대기 후 재감지, 최대 3회

- action: handle-durability
  category: sailing
  recognition:
    precondition:
      - id: hud_visible
        method: template
        target: sailing_hud_frame.png
        region: [0, 0, 1280, 100]
        timeout: 2000
        alternatives:
          - method: template
            target: port_main_ui.png
            region: [0, 0, 1280, 800]
      - id: durability_value
        method: ocr
        target: "\\d+/\\d+"
        region: [950, 40, 200, 40]
        timeout: 2000
      - id: durability_critical
        method: color
        target: "red_dominant"  # 내구도 < 20%: 빨간색 게이지
        region: [950, 60, 200, 15]
        timeout: 500
        alternatives:
          - method: color
            target: "yellow_dominant"  # 내구도 < 40%: 노란색 게이지
            region: [950, 60, 200, 15]
    execution:
      - id: shipyard_menu_port
        method: template
        target: port_shipyard_icon.png
        region: [300, 400, 200, 150]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "조선소"
            region: [200, 300, 400, 300]
      - id: repair_button
        method: template
        target: port_repair_btn.png
        region: [400, 500, 480, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "수리|REPAIR"
            region: [400, 500, 480, 80]
      - id: repair_confirm
        method: template
        target: popup_confirm_btn.png
        region: [450, 500, 380, 80]
        timeout: 2000
      - id: sea_repair_menu
        method: template
        target: sailing_ship_menu_btn.png
        region: [50, 250, 100, 100]
        timeout: 1500
      - id: sea_repair_button
        method: template
        target: sailing_emergency_repair_btn.png
        region: [300, 400, 300, 60]
        timeout: 1500
        alternatives:
          - method: ocr
            target: "긴급 수리|수리"
            region: [300, 400, 300, 60]
    verification:
      - id: durability_increased
        method: ocr
        target: "\\d+/\\d+"
        region: [950, 40, 200, 40]
        timeout: 3000
      - id: durability_gauge_not_red
        method: color
        target: "green_dominant"
        region: [950, 60, 200, 15]
        timeout: 1000
    on_fail: wait_and_retry  # 골드 부족 시 최소 수리, 자재 없으면 detect-port 호출

# ────────────────────────────────────────────────────────────
# CATEGORY: trade (우선순위 4)
# ────────────────────────────────────────────────────────────

- action: enter-market
  category: trade
  recognition:
    precondition:
      - id: city_entered
        method: template
        target: port_main_ui.png
        region: [0, 0, 1280, 800]
        timeout: 2000
      - id: market_icon_visible
        method: template
        target: trade_market_icon.png
        region: [0, 200, 1280, 400]
        timeout: 3000
        alternatives:
          - method: ocr
            target: "교역소|시장|MARKET"
            region: [0, 200, 1280, 400]
    execution:
      - id: market_loading
        method: template
        target: trade_market_loading.png
        region: [400, 300, 480, 200]
        timeout: 1500
      - id: language_warning_popup
        method: ocr
        target: "언어|부족"
        region: [200, 300, 880, 200]
        timeout: 1000
        expect: absent  # 경고 없으면 통과, 있으면 확인 tap
    verification:
      - id: market_ui_loaded
        method: template
        target: trade_buy_tab.png
        region: [100, 50, 300, 80]
        timeout: 3000
        alternatives:
          - method: ocr
            target: "매입|매도|투자"
            region: [100, 50, 600, 80]
      - id: item_list_visible
        method: template
        target: trade_item_list_frame.png
        region: [50, 130, 1180, 500]
        timeout: 2000
    on_fail: scroll_and_retry  # 아이콘 미감지 시 화면 스크롤 후 재탐색

- action: check-prices
  category: trade
  recognition:
    precondition:
      - id: market_screen_active
        method: template
        target: trade_buy_tab.png
        region: [100, 50, 300, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "매입|매도"
            region: [100, 50, 600, 80]
    execution:
      - id: price_percent_values
        method: ocr
        target: "\\d+%"
        region: [900, 130, 200, 500]
        timeout: 3000
      - id: event_badge
        method: template
        target: trade_event_badge.png
        region: [50, 130, 100, 500]
        timeout: 2000
        alternatives:
          - method: llm
            target: "물품 목록에 폭등/폭락/유행/과잉/폭증 마크가 있는가?"
            region: [50, 130, 1180, 500]
      - id: season_mark
        method: template
        target: trade_season_mark.png
        region: [800, 130, 100, 500]
        timeout: 1500
      - id: scroll_complete
        method: pixel
        target: "scroll_indicator_bottom"
        region: [1250, 600, 20, 30]
        timeout: 500
    verification:
      - id: all_prices_collected
        method: ocr
        target: "\\d+%"
        region: [900, 130, 200, 500]
        timeout: 2000
      - id: buy_candidate_found
        method: ocr
        target: "[1-8]\\d%|90%"
        region: [900, 130, 200, 500]
        timeout: 1000
    on_fail: wait_and_retry  # OCR 실패 시 화면 캡처 재시도, 최대 2회

- action: buy-goods
  category: trade
  recognition:
    precondition:
      - id: buy_tab_selected
        method: template
        target: trade_buy_tab_active.png
        region: [100, 50, 300, 80]
        timeout: 2000
        alternatives:
          - method: template
            target: trade_buy_tab.png
            region: [100, 50, 300, 80]
      - id: gold_display
        method: ocr
        target: "\\d+"
        region: [1050, 10, 200, 40]
        timeout: 1500
      - id: cargo_ratio
        method: ocr
        target: "\\d+/\\d+"
        region: [900, 10, 200, 40]
        timeout: 1500
    execution:
      - id: target_item_selected
        method: template
        target: trade_item_selected_highlight.png
        region: [50, 130, 1180, 500]
        timeout: 2000
      - id: quantity_slider
        method: template
        target: trade_quantity_slider.png
        region: [300, 550, 680, 60]
        timeout: 1500
      - id: buy_confirm_button
        method: template
        target: trade_buy_confirm_btn.png
        region: [450, 650, 380, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "매입|구매|BUY"
            region: [450, 650, 380, 80]
      - id: bargain_option
        method: template
        target: trade_bargain_btn.png
        region: [700, 550, 200, 60]
        timeout: 1000
        alternatives:
          - method: ocr
            target: "구매 전략|흥정"
            region: [700, 550, 200, 60]
    verification:
      - id: purchase_complete_popup
        method: ocr
        target: "매입 완료|구매 완료"
        region: [350, 300, 580, 100]
        timeout: 3000
        alternatives:
          - method: template
            target: trade_purchase_complete.png
            region: [350, 300, 580, 200]
      - id: cargo_ratio_changed
        method: ocr
        target: "\\d+/\\d+"
        region: [900, 10, 200, 40]
        timeout: 2000
      - id: tariff_check
        method: ocr
        target: "관세|세금"
        region: [300, 350, 680, 100]
        timeout: 1000
    on_fail: wait_and_retry  # 적재량 부족 시 수량 줄여 재시도

- action: sell-goods
  category: trade
  recognition:
    precondition:
      - id: sell_tab_visible
        method: template
        target: trade_sell_tab.png
        region: [400, 50, 300, 80]
        timeout: 2000
      - id: inventory_has_goods
        method: template
        target: trade_item_list_frame.png
        region: [50, 130, 1180, 500]
        timeout: 2000
      - id: sell_tab_selected
        method: template
        target: trade_sell_tab_active.png
        region: [400, 50, 300, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "매도"
            region: [400, 50, 300, 80]
    execution:
      - id: target_item_for_sell
        method: template
        target: trade_item_selected_highlight.png
        region: [50, 130, 1180, 500]
        timeout: 2000
      - id: sell_all_button
        method: template
        target: trade_sell_all_btn.png
        region: [400, 550, 200, 60]
        timeout: 1500
        alternatives:
          - method: ocr
            target: "전량|전부|ALL"
            region: [400, 550, 200, 60]
      - id: sell_confirm_button
        method: template
        target: trade_sell_confirm_btn.png
        region: [450, 650, 380, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "매도|판매|SELL"
            region: [450, 650, 380, 80]
      - id: bargain_sell_option
        method: template
        target: trade_bargain_sell_btn.png
        region: [700, 550, 200, 60]
        timeout: 1000
        alternatives:
          - method: ocr
            target: "판매 전략|흥정"
            region: [700, 550, 200, 60]
    verification:
      - id: sell_complete_popup
        method: ocr
        target: "매도 완료|판매 완료"
        region: [350, 300, 580, 100]
        timeout: 3000
        alternatives:
          - method: template
            target: trade_sell_complete.png
            region: [350, 300, 580, 200]
      - id: gold_increased
        method: ocr
        target: "\\d+"
        region: [1050, 10, 200, 40]
        timeout: 2000
      - id: fame_gained
        method: ocr
        target: "명성|공헌"
        region: [300, 350, 680, 100]
        timeout: 2000
    on_fail: wait_and_retry  # 흥정 실패 시 기본 가격으로 매도

# ────────────────────────────────────────────────────────────
# CATEGORY: recovery (우선순위 5)
# ────────────────────────────────────────────────────────────

- action: reconnect
  category: recovery
  recognition:
    precondition:
      - id: disconnect_popup
        method: ocr
        target: "재접속|연결 끊김|세션 만료|접속이 끊|네트워크"
        region: [200, 200, 880, 400]
        timeout: 3000
        alternatives:
          - method: template
            target: recovery_disconnect_popup.png
            region: [200, 200, 880, 400]
      - id: app_still_running
        method: system
        target: "pidof com.bluepotion.uwom2 != empty"
        timeout: 1000
    execution:
      - id: reconnect_button
        method: template
        target: recovery_reconnect_btn.png
        region: [350, 500, 580, 80]
        timeout: 3000
        alternatives:
          - method: ocr
            target: "재접속|다시 연결|확인|RETRY"
            region: [350, 500, 580, 80]
      - id: loading_screen
        method: template
        target: login_loading_bar.png
        region: [340, 650, 600, 50]
        timeout: 10000
      - id: login_button_if_needed
        method: template
        target: login_connect_btn.png
        region: [350, 650, 580, 100]
        timeout: 5000
        alternatives:
          - method: ocr
            target: "로그인|접속|시작"
            region: [350, 650, 580, 100]
    verification:
      - id: home_screen_restored
        method: template
        target: home_main_menu.png
        region: [0, 700, 1280, 100]
        timeout: 10000
        alternatives:
          - method: llm
            target: "게임이 정상적으로 복구되어 홈 화면이나 이전 화면이 표시되는가?"
            region: [0, 0, 1280, 800]
      - id: no_error_popup
        method: template
        target: recovery_disconnect_popup.png
        region: [200, 200, 880, 400]
        timeout: 2000
        expect: absent
    on_fail: wait_and_retry  # 10초 후 재시도, 5회 실패 시 restart-app

- action: restart-app
  category: recovery
  recognition:
    precondition:
      - id: error_accumulated
        method: system
        target: "recovery_attempt_count >= 3 OR screen_frozen"
        timeout: 1000
      - id: app_process_check
        method: system
        target: "pidof com.bluepotion.uwom2"
        timeout: 1000
      - id: last_screenshot_stale
        method: system
        target: "screenshot_diff < threshold"
        timeout: 2000
    execution:
      - id: force_stop_complete
        method: system
        target: "am force-stop com.bluepotion.uwom2 && pidof com.bluepotion.uwom2 == empty"
        timeout: 3000
      - id: restart_delay
        method: system
        target: "sleep 3"
        timeout: 5000
      - id: app_launched
        method: system
        target: "am start -n com.bluepotion.uwom2/.MainActivity"
        timeout: 3000
      - id: splash_after_restart
        method: template
        target: login_splash_logo.png
        region: [300, 200, 680, 400]
        timeout: 15000
    verification:
      - id: app_running_again
        method: system
        target: "pidof com.bluepotion.uwom2 != empty"
        timeout: 5000
      - id: loading_or_title
        method: template
        target: login_title_screen.png
        region: [0, 0, 1280, 800]
        timeout: 25000
        alternatives:
          - method: template
            target: login_loading_bar.png
            region: [340, 650, 600, 50]
          - method: ocr
            target: "터치|시작|접속"
            region: [340, 600, 600, 150]
    on_fail: abort  # 3회 실패 시 봇 일시정지 및 알림 발송

- action: back-to-home
  category: recovery
  recognition:
    precondition:
      - id: not_home_screen
        method: template
        target: home_main_menu.png
        region: [0, 700, 1280, 100]
        timeout: 2000
        expect: absent  # 홈 화면이 아님을 확인
      - id: app_responsive
        method: system
        target: "pidof com.bluepotion.uwom2 != empty"
        timeout: 1000
      - id: screen_unknown
        method: llm
        target: "현재 화면이 게임 홈 화면이 아닌 다른 화면인가?"
        region: [0, 0, 1280, 800]
        timeout: 3000
    execution:
      - id: back_press_effect
        method: system
        target: "input keyevent KEYCODE_BACK"
        timeout: 500
      - id: screen_changed
        method: system
        target: "screenshot_diff > threshold"
        timeout: 1500
      - id: exit_confirm_popup
        method: template
        target: popup_exit_confirm.png
        region: [200, 250, 880, 300]
        timeout: 1000
        expect: absent  # 게임 종료 확인 팝업이 나오면 '취소' 처리
    verification:
      - id: home_screen_reached
        method: template
        target: home_main_menu.png
        region: [0, 700, 1280, 100]
        timeout: 2000
        alternatives:
          - method: llm
            target: "게임 홈 화면(메인 메뉴 바 포함)이 표시되는가?"
            region: [0, 0, 1280, 800]
    on_fail: wait_and_retry  # back 최대 5회 반복, 이후 restart-app

# ────────────────────────────────────────────────────────────
# CATEGORY: popup (우선순위 6)
# ────────────────────────────────────────────────────────────

- action: dismiss-generic
  category: popup
  recognition:
    precondition:
      - id: popup_overlay_visible
        method: template
        target: popup_overlay_dim.png
        region: [0, 0, 1280, 800]
        timeout: 1500
        alternatives:
          - method: llm
            target: "화면에 팝업 오버레이(반투명 배경 + 패널)가 표시되는가?"
            region: [0, 0, 1280, 800]
      - id: has_close_or_confirm
        method: template
        target: popup_confirm_btn.png
        region: [300, 550, 680, 120]
        timeout: 1500
        alternatives:
          - method: template
            target: popup_close_x.png
            region: [800, 50, 200, 150]
          - method: ocr
            target: "확인|닫기|OK|CLOSE"
            region: [300, 550, 680, 120]
    execution:
      - id: confirm_tap
        method: template
        target: popup_confirm_btn.png
        region: [300, 550, 680, 120]
        timeout: 1000
        alternatives:
          - method: ocr
            target: "확인|닫기"
            region: [300, 550, 680, 120]
      - id: close_x_tap
        method: template
        target: popup_close_x.png
        region: [800, 50, 200, 150]
        timeout: 1000
    verification:
      - id: popup_dismissed
        method: template
        target: popup_overlay_dim.png
        region: [0, 0, 1280, 800]
        timeout: 2000
        expect: absent  # 팝업 배경 사라져야 성공
      - id: previous_screen_restored
        method: llm
        target: "팝업이 닫히고 이전 화면이 정상적으로 보이는가?"
        region: [0, 0, 1280, 800]
        timeout: 2000
    on_fail: wait_and_retry  # 1초 후 재tap, X 버튼 또는 back 시도, 최대 3회

- action: handle-error
  category: popup
  recognition:
    precondition:
      - id: error_popup_detected
        method: ocr
        target: "접속 오류|서버 오류|네트워크|오류|ERROR|에러|통신"
        region: [200, 200, 880, 300]
        timeout: 3000
        alternatives:
          - method: template
            target: popup_error_icon.png
            region: [550, 150, 180, 150]
      - id: error_popup_frame
        method: template
        target: popup_error_frame.png
        region: [150, 150, 980, 500]
        timeout: 2000
    execution:
      - id: retry_or_confirm_button
        method: template
        target: popup_retry_btn.png
        region: [350, 500, 580, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "재시도|확인|다시|RETRY|OK"
            region: [350, 500, 580, 80]
          - method: template
            target: popup_confirm_btn.png
            region: [350, 500, 580, 80]
    verification:
      - id: error_popup_gone
        method: template
        target: popup_error_frame.png
        region: [150, 150, 980, 500]
        timeout: 3000
        expect: absent
      - id: screen_recovered
        method: llm
        target: "오류 팝업이 사라지고 정상 게임 화면이 복귀했는가?"
        region: [0, 0, 1280, 800]
        timeout: 3000
    on_fail: wait_and_retry  # 동일 오류 반복 시 reconnect, 3회 이상 시 restart-app

- action: handle-update
  category: popup
  recognition:
    precondition:
      - id: update_popup_detected
        method: ocr
        target: "업데이트|새 버전|update|UPDATE|버전"
        region: [200, 200, 880, 300]
        timeout: 3000
        alternatives:
          - method: template
            target: popup_update_frame.png
            region: [150, 150, 980, 500]
    execution:
      - id: update_confirmed
        method: template
        target: popup_confirm_btn.png
        region: [350, 500, 580, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "확인|업데이트|스토어"
            region: [350, 500, 580, 80]
      - id: bot_paused
        method: system
        target: "bot_mode == paused"
        timeout: 1000
      - id: notification_sent
        method: system
        target: "send_notification('update_required')"
        timeout: 3000
    verification:
      - id: update_state_set
        method: system
        target: "update_required == true AND bot_mode == paused"
        timeout: 1000
    on_fail: abort  # 자동 처리 불가, 사용자 개입 필요

- action: handle-maintenance
  category: popup
  recognition:
    precondition:
      - id: maintenance_popup_detected
        method: ocr
        target: "점검|maintenance|MAINTENANCE|서버 점검|정기 점검"
        region: [200, 200, 880, 300]
        timeout: 3000
        alternatives:
          - method: template
            target: popup_maintenance_frame.png
            region: [150, 150, 980, 500]
      - id: maintenance_end_time
        method: ocr
        target: "\\d{1,2}[:/]\\d{2}|\\d{4}-\\d{2}-\\d{2}"
        region: [300, 300, 680, 150]
        timeout: 3000
    execution:
      - id: confirm_and_close
        method: template
        target: popup_confirm_btn.png
        region: [350, 500, 580, 80]
        timeout: 2000
        alternatives:
          - method: ocr
            target: "확인|닫기|OK"
            region: [350, 500, 580, 80]
      - id: app_closed
        method: system
        target: "am force-stop com.bluepotion.uwom2"
        timeout: 3000
      - id: bot_waiting_mode
        method: system
        target: "bot_mode == waiting"
        timeout: 1000
    verification:
      - id: maintenance_state_set
        method: system
        target: "maintenance_detected == true AND bot_mode == waiting"
        timeout: 1000
      - id: retry_scheduled
        method: system
        target: "next_retry_time == maintenance_end_time + 300000"
        timeout: 1000
    on_fail: wait_and_retry  # 5분 간격 재시도, 2시간 초과 시 봇 정지 및 알림

# ============================================================
# 공통 템플릿 파일 명명 규칙
# ============================================================
# login_*       : 로그인/스플래시/서버선택/출석 관련
# port_*        : 항구 UI/출항소/조선소/보급 관련
# sailing_*     : 항해 HUD/월드맵/자동항해 관련
# trade_*       : 교역소/매입/매도/시세 관련
# recovery_*    : 재접속/오류복구 관련
# popup_*       : 공통 팝업 프레임/버튼 관련
# home_*        : 게임 홈 화면 관련

# ============================================================
# 필요 템플릿 이미지 총 목록 (수집 대상)
# ============================================================
# login_splash_logo.png
# login_loading_bar.png
# login_title_screen.png
# login_server_list_header.png
# login_connect_btn.png
# login_character_screen.png
# login_popup_frame.png
# login_terms_agree_all.png
# login_notice_close.png
# login_checkin_popup.png
# login_checkin_icon.png
# login_checkin_claim_btn.png
# login_checkin_claimed_stamp.png
# home_main_menu.png
# port_main_ui.png
# port_depart_icon.png
# port_dock_confirm_btn.png
# port_departure_office.png
# port_supply_tab.png
# port_auto_supply_btn.png
# port_departure_screen.png
# port_depart_btn.png
# port_supply_warning_popup.png
# port_shipyard_icon.png
# port_repair_btn.png
# sailing_hud_frame.png
# sailing_port_icon.png
# sailing_auto_sail_icon.png
# sailing_worldmap_frame.png
# sailing_destination_marker.png
# sailing_auto_sail_btn.png
# sailing_delegate_btn.png
# sailing_dock_btn.png
# sailing_ship_menu_btn.png
# sailing_emergency_repair_btn.png
# trade_market_icon.png
# trade_market_loading.png
# trade_buy_tab.png
# trade_buy_tab_active.png
# trade_sell_tab.png
# trade_sell_tab_active.png
# trade_item_list_frame.png
# trade_item_selected_highlight.png
# trade_quantity_slider.png
# trade_buy_confirm_btn.png
# trade_sell_confirm_btn.png
# trade_sell_all_btn.png
# trade_bargain_btn.png
# trade_bargain_sell_btn.png
# trade_purchase_complete.png
# trade_sell_complete.png
# trade_event_badge.png
# trade_season_mark.png
# popup_confirm_btn.png
# popup_close_x.png
# popup_overlay_dim.png
# popup_exit_confirm.png
# popup_error_icon.png
# popup_error_frame.png
# popup_retry_btn.png
# popup_update_frame.png
# popup_maintenance_frame.png
# recovery_disconnect_popup.png
# recovery_reconnect_btn.png
```
