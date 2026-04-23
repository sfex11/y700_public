# Decision Table: sailing

state: sailing
description: 항해 중. 화면: 바다 배경, 미니맵, 나침반.

## 팩트 체크리스트
- speed: 현재 속도
- weather: 날씨 (맑음/비/폭풍/눈)
- durability: 내구도 (%)
- supply_remaining: 보급 잔량 예상 시간
- distance_to_dest: 목적지까지 거리
- enemy_spotted: 적 함대 감지 여부
- port_nearby: 근처 항구 여부
- sea_zone_type: 해역 종류 (일반/쇄빙/내파/돌파/조류)
- disaster_active: 재해 발생 여부

## 모드별 goal 점수

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| auto_sail | always=60 | auto-sail (목적지까지) |
| detect_port | supply<30% OR durability<40%=90 | detect-port → dock |
| avoid_storm | weather=폭풍=95 | avoid-storm |
| handle_disaster | disaster_active=100 | handle-disaster |
| manual_sail | 특수해역=50 | manual-sail |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| nearest_port | always=90 | detect-port → dock |
| avoid_storm | weather=폭풍=100 | avoid-storm |
| handle_disaster | disaster_active=100 | handle-disaster |
| manage_durability | durability<50%=85 | handle-durability |

### COMBAT_ESCAPE
| goal | score_if | action |
|------|----------|--------|
| flee | enemy_spotted=95 | flee-battle |
| avoid | enemy_spotted AND flee_success=90 | auto-sail (반대방향) |
| delegate | enemy_spotted AND NOT flee_success=70 | delegate-combat |

### COMBAT_ACTIVE
| goal | score_if | action |
|------|----------|--------|
| engage | enemy_spotted AND 전투력우위=90 | engage-battle |
| auto_continue | NOT enemy_spotted=60 | auto-sail |

## 공통 골 게이트
- durability<20% → nearest_port (score=100)
- supply_critical → nearest_port (score=100)
- disaster_active → handle_disaster (score=100)
