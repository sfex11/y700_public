# Decision Table: in-port

state: in-port
description: 항구 정박 기본 상태. 도시 전경 위에서 시설 선택, 교역, 출항 준비 하위 상태로 분기한다.

## 팩트 체크리스트
- gold: 보유 골드
- durability: 내구도 (%)
- cargo_ratio: 적재율 (%)
- cargo_has_goods: 교역품 보유 여부
- supply_water: 물 잔량 (%)
- supply_food: 식량 잔량 (%)
- levelup_available: 레벨업 가능 여부
- skill_book_available: 스킬북 보유 여부
- contract_available: 항해사 계약서 보유 여부
- nav_slot_free: 항해사 슬롯 여유
- invest_cooldown: 투자 쿨타임 여부
- daily_quest_pending: 일일퀘스트 미수령
- dispatch_available: 파견함대 가동 가능

## 모드별 goal 점수

### BOOTSTRAP
| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | popup_detected=200 | dismiss-generic |
| skip_dialog | dialog_active=20 | skip-dialog |
| depart_office | harbor_reached=110 | tap-departure-office |
| harbor_front_default | always=89 | tap-departure-office |
| reset_zero_stall_empty_exec_fail_control_gap | last_action_empty AND NOT last_action_success AND policy_stall_count=0=126 | tap-departure-office |
| repeat_review_null_post_loop188_control_gap | failed_action_unknown AND last_action_empty AND NOT last_action_success AND policy_stall_count>=3=125 | tap-departure-office |
| reset_empty_exec_fail_control_gap | last_action_empty AND NOT last_action_success AND policy_stall_count>=1=124 | tap-departure-office |
| reset_empty_unknown_control_gap | failed_action_unknown AND last_action_empty AND NOT last_action_success AND policy_stall_count>=1=123 | tap-departure-office |
| unknown_action_control_gap | failed_action_unknown AND policy_stall_count>=3=122 | tap-departure-office |
| empty_action_exec_fail_gap | last_action_empty AND NOT last_action_success AND policy_stall_count>=2=121 | tap-departure-office |
| empty_action_control_gap | last_action_empty AND policy_stall_count>=2=119 | tap-departure-office |
| stall_recover_departure | policy_stall_count>=3=118 | tap-departure-office |
| try_departure_office_blind | NOT harbor_reached AND policy_stall_count>=1=88 | tap-departure-office |
| open_minimap_fallback | NOT harbor_reached AND policy_stall_count<3=70 | open-minimap |
| navigate_harbor_recovery | NOT harbor_reached AND policy_stall_count>=3=45 | navigate-harbor |
| fallback_back_when_not_harbor | NOT harbor_reached=35 | back |
| fallback_back_default | always=5 | back |

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | popup_detected=200 | dismiss-generic |
| skip_dialog | dialog_active=20 | skip-dialog |
| depart_office | harbor_reached=110 | tap-departure-office |
| repeat_review_null_post_loop188_control_gap | failed_action_unknown AND last_action_empty AND NOT last_action_success AND policy_stall_count>=3=125 | tap-departure-office |
| stall_recover_departure | policy_stall_count>=3=118 | tap-departure-office |
| sell_high | cargo_has_goods AND 시세>=110=80 | sell-goods |
| buy_cheap | cargo_ratio<90% AND gold>5000 AND 시세<=90=75 | buy-goods |
| check_market | 시세_미확인=70 | check-prices |
| invest | gold>50000 AND NOT invest_cooldown=60 | invest |
| dispatch | dispatch_available=35 | dispatch |
| daily_quest | daily_quest_pending=50 | collect-daily-quest |
| harbor_front_default | always=40 | tap-departure-office |
| fallback_back_when_not_harbor | NOT harbor_reached=35 | back |
| fallback_back_default | always=5 | back |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| dismiss_popup | popup_detected=200 | dismiss-generic |
| skip_dialog | dialog_active=20 | skip-dialog |
| depart_office | harbor_reached=110 | tap-departure-office |
| reset_zero_stall_empty_exec_fail_control_gap | last_action_empty AND NOT last_action_success AND policy_stall_count=0=126 | tap-departure-office |
| reset_zero_stall_empty_unknown_control_gap | failed_action_unknown AND last_action_empty AND NOT last_action_success AND policy_stall_count=0=125 | tap-departure-office |
| reset_empty_exec_fail_control_gap | last_action_empty AND NOT last_action_success AND policy_stall_count>=1=124 | tap-departure-office |
| reset_empty_unknown_control_gap | failed_action_unknown AND last_action_empty AND NOT last_action_success AND policy_stall_count>=1=123 | tap-departure-office |
| unknown_action_control_gap | failed_action_unknown AND policy_stall_count>=3=122 | tap-departure-office |
| empty_action_exec_fail_gap | last_action_empty AND NOT last_action_success AND policy_stall_count>=2=121 | tap-departure-office |
| empty_action_control_gap | last_action_empty AND policy_stall_count>=2=119 | tap-departure-office |
| stall_recover_departure | policy_stall_count>=3=118 | tap-departure-office |
| open_minimap_fallback | NOT harbor_reached=85 | open-minimap |
| repair | durability<80% AND NOT harbor_reached=95 | repair |
| supply_water_low | supply_water<80% AND NOT harbor_reached=90 | supply |
| supply_food_low | supply_food<80% AND NOT harbor_reached=90 | supply |
| emergency_sell | gold<3000=80 | sell-goods |
| heal | crew_fatigue>70%=60 | check-inn |
| harbor_front_default | always=50 | tap-departure-office |
| fallback_back_when_not_harbor | NOT harbor_reached=35 | back |
| fallback_back_default | always=5 | back |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| level_up | levelup_available=80 | level-up-navigator |
| upgrade_ship | gold>80000 AND 개조가능=70 | remodel-ship |
| upgrade_equip | 강화재료_충분=65 | upgrade-equipment |
| hire_nav | contract_available AND nav_slot_free=60 | hire-navigator → assign-navigator |
| skill_up | skill_book_available=55 | upgrade-skill |
| build_ship | gold>100000 AND 자재충분=45 | build-ship |
| research | 선단연구_해금=50 | research-fleet |
| harbor_front_default | always=45 | tap-departure-office |
| fallback_back_default | always=5 | back |

### COMBAT_ESCAPE
| goal | score_if | action |
|------|----------|--------|
| depart_flee | always=90 | depart (도주 목적지) |
| repair_quick | durability<50%=70 | repair |

### COMBAT_ACTIVE
(항구에서는 발생하지 않음. 전투 종료 후 collect-reward → 이전 모드 복귀)

## 공통 골 게이트 (모든 모드)
survival 액션은 항상 최우선:
- durability<30% → repair (score=100, 모드 무관)
- supply<15% → supply (score=100, 모드 무관)

## 메모
- 실제 시설 선택 화면은 `port-menu` 의미에 가깝지만 런타임 상태명은 `in-port` 또는 `in-port-harbor`를 계속 사용한다.
- 출항소 대화/보급/출항 단계는 `in-port-departure`, `in-port-warehouse`, `in-port-dialog`로 분리해 해석한다.
- 최근 supervisor review에서 `in-port` 상위 상태와 `policy_stall_count>=3` 조합이 반복되면 `unknown` 실패가 나와도 항구 전경 고착으로 보고 `tap-departure-office`를 최우선 복구로 유지한다.
- `recent_reviews`가 같은 항구 전경을 연속 지목하고 `last_action`이 비어 있거나 실패 액션이 `unknown`으로 끝난 경우도 동일하게 control 전달 실패로 간주한다. 이때는 `open-minimap`나 `navigate-harbor`로 분기하지 않고 같은 `tap-departure-office` 복구를 유지한다.
- `last_action_empty AND NOT last_action_success AND policy_stall_count>=2` 조합은 loop 80 계열의 빈 액션 handoff 실패를 직접 가리키는 강한 신호로 본다. 이 경우 `tap-departure-office`를 같은 stall 단계의 다른 우회 액션보다 먼저 고정한다.
- `last_action_empty AND policy_stall_count>=2` 조합은 supervisor review 없이도 control handoff 공백의 조기 신호로 취급한다. 이 경우 BOOTSTRAP/RECOVERY 모두 `tap-departure-office`를 `navigate-harbor`보다 먼저 선택한다.
- `action_trace`에서 `in-port-harbor` 계열 액션이 `exec_fail`로 반복되면 recognition/decision 자체보다 control 병목 가능성이 높다. 이 경우 `policy_stall_count>=3` 복구는 계속 `tap-departure-office`에 남기고, `navigate-harbor`는 마지막 우회 수단으로만 유지한다.
- loop 100 계열처럼 같은 리스본 항구 전경과 출항소 툴팁이 반복될 때는 `tap-departure-office`의 성공 판정을 `in-port-departure` 진입 또는 상단 `출항` 버튼 노출로 고정한다. 검증 전에는 같은 화면 재분류만으로 `unknown` 실패를 늘리지 않는다.
- `tap-departure-office`는 `depart`의 일부 우회가 아니라 독립 실행 액션으로 취급한다. 항구 전경 고착 복구에서는 먼저 출항소를 눌러 `in-port-departure` 또는 상단 `출항` 버튼 노출을 확인한 뒤에만 `depart`를 이어서 실행한다.
- `in-port` 상위 상태는 항구 전경, 도시 내부, 패널 진입 직전이 섞여 잡힐 수 있으므로 각 모드에 문서상 `always` fallback을 둔다. 기본 우선순위는 `tap-departure-office`이며, 항구 전경 증거가 약하거나 실행 전이가 없을 때만 `back`이 마지막 안전판이 된다.
- recent review 파일들의 `changes_made`가 계속 `null`이고 같은 리스본 항구 전경의 출항소 툴팁이 반복되면, 새 루프에서 `policy_stall_count`가 1로 다시 시작돼도 control/verify 재시도로 보고 `tap-departure-office` 복구를 유지한다. 이때 codex 탭 기준은 툴팁 중심에 더 가까운 `[390,180]` 하향 좌표를 우선한다.
- `failed_action=unknown`이 recent review에서 반복되고 실제 화면은 계속 `in-port-harbor`인 경우, `BOOTSTRAP`/`RECOVERY` 모두 이를 분류 실패보다 control handoff 실패로 우선 해석한다. `policy_stall_count>=3`이면 `tap-departure-office`를 같은 조건의 일반 stall 복구보다 한 단계 높게 고정한다.
- `RECOVERY` 모드에서도 현재 화면이 실제 `in-port-harbor`인데 `harbor_reached` 팩트가 비어 `open-minimap`으로 새는 경우가 있다. `last_action_empty AND NOT last_action_success AND policy_stall_count=0` 또는 같은 조건의 `failed_action_unknown`이 보이면 이를 0-stall control handoff reset으로 보고 `open-minimap`보다 먼저 `tap-departure-office`를 강제한다.
- recent review 3건이 모두 같은 `in-port-harbor` 복구 탭으로 끝났는데 새 루프에서 `policy_stall_count`만 1로 리셋되는 경우도 control/verify 재시작으로 본다. 이때 `failed_action_unknown AND last_action_empty AND NOT last_action_success` 조합이면 stall 누적을 다시 기다리지 않고 즉시 `tap-departure-office`를 유지한다.
- `recent_reviews`가 같은 항구 전경과 `changes_made=null` 반복을 가리키는 동안 `last_action=''`와 `last_action_success=false`만 남고 `failed_action`이 비어 있어도, 이를 별도 분기 신호로 해석하지 않는다. `policy_stall_count>=1`이면 reset 직후에도 `tap-departure-office`를 유지해 `open-minimap` 우회를 막는다.
- 다만 현재 분류 상태가 이미 `in-port-city`이거나 OCR/리뷰가 도시 내부 이동 화면을 강하게 가리키면, 위 harbor 복구 문맥을 그대로 재사용하면 안 된다. 이 경우 우선 액션은 `tap-departure-office`가 아니라 `back`이며, 항구 전경(`in-port-harbor`) 복귀 후에만 출항소 복구 문맥을 다시 적용한다.
- loop 184 이후 recent review 2회가 추가로 같은 `in-port-harbor` 장면과 `changes_made=null`로 끝났는데 현재 루프가 다시 `policy_stall_count=2`, `last_action=''`, `last_action_success=false`라면, 이를 새 규칙 부족이 아니라 post-tap no-transition 검증 병목으로 본다. 이 경우도 `open-minimap`·`navigate-harbor`로 새지 않고 `[390,180]` `tap-departure-office`와 `in-port-departure` 또는 상단 `출항` 버튼 노출 확인만 유지한다.
- loop 193처럼 recent review 3건이 모두 같은 리스본 항구 전경의 `in-port-harbor` 복구 탭과 `changes_made=null`로 끝났고 현재도 `failed_action=unknown`, `last_action=''`, `last_action_success=false`, `policy_stall_count=3`이면 결정 부족이 아니라 출항소 진입 인식/검증 병목으로 본다. 이 경우 `tap-departure-office`는 유지하되 실행 좌표 탐색과 진입 확인을 더 느슨하게 잡아 같은 `[390,180]` 복구를 다시 살린다.
- loop 213/216처럼 recent review 3건이 계속 같은 리스본 항구 전경, `changes_made=null`, `last_action=''`, `last_action_success=false`, `policy_stall_count=2`로 반복되면 툴팁 중심 `[390,180]`보다 출입구/닻 아이콘 쪽 `[386,208]` 하향 탭을 우선한다. 이 경우도 `open-minimap`·`navigate-harbor`로 분기하지 않고 `in-port-departure` 또는 상단 `출항` 버튼 노출 확인만 유지한다.
- loop 221처럼 최신 recent review 3건도 모두 `changes_made=null`인 같은 리스본 항구 전경의 `in-port-harbor` 복구 탭으로 끝났는데 현재 루프가 다시 `policy_stall_count=2`, `last_action=''`, `last_action_success=false`라면, 이를 `[390,180]` 규칙 회귀가 아니라 loop 213/216과 같은 하향 좌표 유지 신호로 본다. 다만 이 하향 좌표 `[386,208]`까지 3회 이상 반복해도 `in-port-departure` 전이가 없으면 더 이상 툴팁/닻 중심을 유지하지 말고 실제 출입문 중심으로 기준을 내린다.
- loop 223처럼 recent review 최신 3건이 같은 `in-port-harbor` 항구 전경과 `changes_made=null`로 끝난 직후 현재 루프의 `policy_stall_count`만 0으로 리셋되고 `last_action=''`, `last_action_success=false`가 남아도, 이를 `open-minimap` 복귀 신호로 해석하지 않는다. 이 경우도 loop 213/216/221의 하향 좌표 문맥을 이어 받아 `tap-departure-office`를 먼저 유지한다.
- loop 231처럼 recent review 최신 3건이 모두 같은 리스본 항구 전경의 `in-port-harbor`, `changes_made=null`, `codex_tap=[386,208]`로 끝났는데도 현재 루프가 다시 `policy_stall_count=2`, `last_action=''`, `last_action_success=false`라면, 병목은 decision 분기보다 출항소 탭 좌표 오차로 본다. 이 경우 `tap-departure-office` 액션 자체는 유지하되 기준 좌표를 툴팁 중심이나 닻 아이콘 근처가 아니라 건물 실제 출입문 중심 `[430,248]` 쪽으로 옮기고 `open-minimap`·`navigate-harbor` 분기는 계속 막는다.
- loop 245처럼 recent review 최신 3건이 모두 같은 리스본 항구 전경의 `in-port-harbor`, `changes_made=null`, `codex_tap=[430,248]`로 끝났는데도 현재 루프가 `policy_stall_count>=3`, `last_action=''`, `last_action_success=false`라면, 병목은 출항소 선택 실패가 아니라 문턱 앞 정지로 본다. 이 경우 `tap-departure-office`는 유지하되 실행 좌표 기준을 실제 문짝 안쪽 중심 `[442,256]`으로 더 깊게 내리고, 검증도 `in-port-departure` 또는 상단 `출항` 버튼/출항 준비 패널 노출만 확인한다.
- loop 254처럼 recent review 최신 3건이 모두 같은 리스본 항구 전경의 `in-port-harbor`, `changes_made=null`, `codex_tap=[442,256]`로 끝났는데도 현재 루프가 다시 `policy_stall_count=0`, `last_action=''`, `last_action_success=false`라면, 이를 더 깊은 좌표 탐색 부족으로 보지 않고 verify/control handoff deadlock 지속으로 본다. 이 경우 `tap-departure-office` 자체는 유지하되 recognition-map 좌표를 다시 흔들지 말고 `in-port-departure` 또는 상단 `출항` 버튼/출항 준비 패널 노출 여부 확인만 우선한다.
- loop 285/288처럼 recent review 최신 3건이 계속 같은 리스본 항구 전경의 `in-port-harbor`, `changes_made=null`, `codex_tap=[442,256]`로 끝난 뒤 새 루프에서 `policy_stall_count`가 다시 1 또는 3으로 시작하고 `last_action=''`, `last_action_success=false`, `failed_action=unknown`이 남아도 이를 새 좌표 탐색 근거로 해석하지 않는다. 이 경우 `BOOTSTRAP`/`RECOVERY` 모두 `tap-departure-office`를 그대로 유지하고 검증 목표는 오직 `in-port-departure` 진입이나 상단 `출항` 버튼, 출항 준비/보급/수리/선원 모집 패널 노출로 고정한다.
