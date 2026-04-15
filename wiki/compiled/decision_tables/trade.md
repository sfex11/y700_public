# Decision Table: trade

state: trade
description: 교역소 내. 화면: 물품 목록, 시세, 적재량.

## 팩트 체크리스트
- gold: 보유 골드
- cargo_ratio: 적재율 (%)
- buy_prices: 매입 시세 dict
- sell_prices: 매도 시세 dict
- trade_event: 교역 이벤트 (폭등/폭락/유행/과잉)
- tariff: 관세율
- bargain_available: 흥정 가능 여부

## 모드별 goal 점수

### MONEY_MAKING
| goal | score_if | action |
|------|----------|--------|
| buy | 시세≤85% AND cargo<90% AND gold충분=85 | buy-goods |
| sell | 시세≥115% AND cargo_has_goods=85 | sell-goods |
| invest | gold>50000 AND 쿨타임완료=60 | invest |
| exit | 목표달성 OR 시세불리=70 | 나가기 → in-port |

### RECOVERY
| goal | score_if | action |
|------|----------|--------|
| emergency_sell | gold<3000=95 | sell-goods (시세무관) |
| exit | 완료=80 | 나가기 → in-port |

### GROWTH
| goal | score_if | action |
|------|----------|--------|
| invest | gold>50000=80 | invest |
| exit | 투자완료=70 | 나가기 → in-port |

## 시세 판정 기준
- ≤ 85%: 매입 신호 (할인)
- 86~99%: 관망
- 100%: 기준가
- 101~109%: 약간 비쌈
- ≥ 110%: 매도 신호 (프리미엄)
- 교역 이벤트 시: 폭등=매도 급행, 폭락=매입 급행
