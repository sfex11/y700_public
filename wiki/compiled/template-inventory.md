---
type: inventory
status: active
updated: 2026-04-15
---

# 템플릿 현황 인벤토리

## 요약
- recognition-map에서 필요한 템플릿: **64개**
- 기존 자산: 416개 이미지 (대부분 PC 스크린샷 기반)
- **모바일(1280x800) 직접 수집 필요: 64개 전부**

---

## 기존 자산 현황

| 디렉토리 | 수량 | 소스 | 모바일 호환 |
|----------|------|------|-------------|
| elements/ | 4 | 게임 스크린샷 수동 크롭 | ⚠️ 미확인 |
| 전투/ | 4 | 게임 스크린샷 수동 크롭 | ⚠️ 미확인 |
| web-cropped/ | 84 | Steam PC 스크린샷 Claude 크롭 | ❌ PC 해상도 |
| web-resources/ | 67 | 웹 이미지 수집 | ❌ 웹 소스 |
| auto-extracted/ | 238 | 유튜브 v2 추출기 | ⚠️ 영상 기반 |
| _variations/ | 15 | 전투 변형 | ⚠️ 미확인 |
| **총계** | **416** | | |

### 기존 자산의 한계
- **web-cropped**: Steam PC 클라이언트(1920x1080+)에서 크롭. 모바일 원격렌더링(1280x800)과 UI 배치/해상도 다름
- **web-resources**: 홍보용 이미지, 인게임 스크린샷 아님
- **auto-extracted**: 유튜브 영상 프레임 기반. 해상도/비율 불일치 가능
- **elements/전투**: 실제 태블릿에서 수동 크롭한 것. 가장 유망하나 8개뿐

---

## 인식 맵 vs 기존 자산 매핑

### login (4개 액션, 14개 템플릿 필요)

| 필요 템플릿 | 기존 자산 | 상태 |
|-------------|-----------|------|
| login_splash_logo.png | ❌ 없음 | 수집 필요 |
| login_loading_bar.png | ❌ 없음 | 수집 필요 |
| login_title_screen.png | ❌ 없음 | 수집 필요 |
| login_server_list_header.png | ❌ 없음 | 수집 필요 |
| login_connect_btn.png | ❌ 없음 | 수집 필요 |
| login_character_screen.png | ❌ 없음 | 수집 필요 |
| login_popup_frame.png | ❌ 없음 | 수집 필요 |
| login_terms_agree_all.png | ❌ 없음 | 수집 필요 |
| login_notice_close.png | ❌ 없음 | 수집 필요 |
| login_checkin_icon.png | ❌ 없음 | 수집 필요 |
| login_checkin_popup.png | ❌ 없음 | 수집 필요 |
| login_checkin_claim_btn.png | ❌ 없음 | 수집 필요 |
| login_checkin_claimed_stamp.png | ❌ 없음 | 수집 필요 |
| home_main_menu.png | ❌ 없음 | 수집 필요 |

### in-port (3개 액션, 9개 템플릿 필요)

| 필요 템플릿 | 기존 자산 | 상태 |
|-------------|-----------|------|
| port_main_ui.png | ❌ 없음 | 수집 필요 |
| port_departure_office.png | ❌ 없음 | 수집 필요 |
| port_departure_screen.png | ❌ 없음 | 수집 필요 |
| port_depart_btn.png | ❌ 없음 | 수집 필요 |
| port_depart_icon.png | ❌ 없음 | 수집 필요 |
| port_supply_tab.png | ❌ 없음 | 수집 필요 |
| port_auto_supply_btn.png | ❌ 없음 | 수집 필요 |
| port_supply_warning_popup.png | ❌ 없음 | 수집 필요 |
| port_dock_confirm_btn.png | ❌ 없음 | 수집 필요 |
| port_shipyard_icon.png | ❌ 없음 | 수집 필요 |
| port_repair_btn.png | ❌ 없음 | 수집 필요 |

### sailing (3개 액션, 9개 템플릿 필요)

| 필요 템플릿 | 기존 자산 | 상태 |
|-------------|-----------|------|
| sailing_hud_frame.png | web-cropped/항해(메인)/유사 요소 | ⚠️ PC, 검증 필요 |
| sailing_auto_sail_btn.png | ❌ 없음 | 수집 필요 |
| sailing_auto_sail_icon.png | ❌ 없음 | 수집 필요 |
| sailing_destination_marker.png | ❌ 없음 | 수집 필요 |
| sailing_port_icon.png | ❌ 없음 | 수집 필요 |
| sailing_dock_btn.png | ❌ 없음 | 수집 필요 |
| sailing_ship_menu_btn.png | ❌ 없음 | 수집 필요 |
| sailing_emergency_repair_btn.png | ❌ 없음 | 수집 필요 |
| sailing_delegate_btn.png | ❌ 없음 | 수집 필요 |
| sailing_worldmap_frame.png | ❌ 없음 | 수집 필요 |

### trade (4개 액션, 17개 템플릿 필요)

| 필요 템플릿 | 기존 자산 | 상태 |
|-------------|-----------|------|
| trade_market_icon.png | ❌ 없음 | 수집 필요 |
| trade_market_loading.png | ❌ 없음 | 수집 필요 |
| trade_buy_tab.png | web-cropped/교역(구매)/구매_탭.png | ⚠️ PC, 검증 필요 |
| trade_buy_tab_active.png | web-cropped/교역(구매)/구매_탭.png | ⚠️ PC, 검증 필요 |
| trade_sell_tab.png | web-cropped/교역(구매)/판매_탭.png | ⚠️ PC, 검증 필요 |
| trade_sell_tab_active.png | web-cropped/교역(구매)/판매_탭.png | ⚠️ PC, 검증 필요 |
| trade_item_list_frame.png | ❌ 없음 | 수집 필요 |
| trade_item_selected_highlight.png | ❌ 없음 | 수집 필요 |
| trade_quantity_slider.png | ❌ 없음 | 수집 필요 |
| trade_buy_confirm_btn.png | web-cropped/교역(구매)/구매_버튼.png | ⚠️ PC, 검증 필요 |
| trade_purchase_complete.png | ❌ 없음 | 수집 필요 |
| trade_sell_confirm_btn.png | ❌ 없음 | 수집 필요 |
| trade_sell_all_btn.png | ❌ 없음 | 수집 필요 |
| trade_sell_complete.png | ❌ 없음 | 수집 필요 |
| trade_bargain_btn.png | ❌ 없음 | 수집 필요 |
| trade_bargain_sell_btn.png | ❌ 없음 | 수집 필요 |
| trade_event_badge.png | ❌ 없음 | 수집 필요 |
| trade_season_mark.png | ❌ 없음 | 수집 필요 |

### recovery (3개 액션, 2개 템플릿 필요)

| 필요 템플릿 | 기존 자산 | 상태 |
|-------------|-----------|------|
| recovery_disconnect_popup.png | ❌ 없음 | 수집 필요 |
| recovery_reconnect_btn.png | ❌ 없음 | 수집 필요 |

### popup (4개 액션, 7개 템플릿 필요)

| 필요 템플릿 | 기존 자산 | 상태 |
|-------------|-----------|------|
| popup_overlay_dim.png | ❌ 없음 | 수집 필요 (color/pixel로 대체 가능) |
| popup_close_x.png | ❌ 없음 | 수집 필요 |
| popup_confirm_btn.png | ❌ 없음 | 수집 필요 |
| popup_error_frame.png | ❌ 없음 | 수집 필요 |
| popup_error_icon.png | ❌ 없음 | 수집 필요 |
| popup_update_frame.png | ❌ 없음 | 수집 필요 |
| popup_maintenance_frame.png | ❌ 없음 | 수집 필요 |
| popup_retry_btn.png | ❌ 없음 | 수집 필요 |
| popup_exit_confirm.png | ❌ 없음 | 수집 필요 |

---

## 통계

| 상태 | 수량 | 비율 |
|------|------|------|
| ❌ 수집 필요 | 57개 | 89% |
| ⚠️ PC 있음, 검증 필요 | 7개 | 11% |
| ✅ 바로 사용 가능 | 0개 | 0% |

---

## OCR로 대체 가능한 항목

다음 템플릿은 OCR로 대체 가능하여 수집 우선순위에서 제외 가능:

| 템플릿 | OCR 대체 | 비고 |
|--------|----------|------|
| popup_confirm_btn.py | "확인" 텍스트 | 버튼 내 텍스트 인식 |
| popup_close_x.png | color/pixel | 우상단 X 버튼 위치 거의 고정 |
| popup_retry_btn.png | "재시도" 텍스트 | |
| trade_buy_tab_active.png | "매입" 텍스트 | |
| trade_sell_tab_active.png | "매도" 텍스트 | |

**OCR 대체 시 수집 필요: 57 - 5 = 52개**

---

## 결론

1. 기존 416개 자산은 대부분 PC/웹 소스로 **모바일 런타임에 직접 사용 불가**
2. web-cropped 7개만 검증 가치 있음 → 실제 모바일 화면과 비교 필요
3. **Phase 3 (오버레이+텔레그램 수집) 로 52~57개 신규 수집 필수**
4. popup 관련은 OCR/color 대체로 수집 부담 감소 가능
5. 우선순위: login(14) → in-port(11) → sailing(10) → trade(17) → recovery(2) → popup(7)
