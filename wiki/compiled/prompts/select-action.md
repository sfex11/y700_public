# gemma 프롬프트: 후보 액션 선택
용도: 2~4개 후보 중 현재 화면에 가장 적합한 액션 선택
엔드포인트: /completion
최대 토큰: 5

## 프롬프트 템플릿
```
STATE={state}
FACTS:
{facts_formatted}
CANDIDATES:
{candidates_formatted}

Choose the best action for this screen.
Answer only the action id (number).
```

## 실제 사용 예시
```
STATE=in-port
FACTS:
- gold=45000
- durability=85%
- cargo_ratio=30%
- supply=75%
CANDIDATES:
1. check-prices
2. buy-goods
3. repair
4. visit-npc

Choose the best action.
Answer only the number.
```

## 기대 응답
- "1" 또는 "2" 등 숫자만
- 숫자가 아니거나 범위 밖이면 → candidates[0] (첫번째 후보)

## 후보 생성 규칙
- candidates는 decision_table에서 현재 mode 기준으로 상위 2~4개만 전달
- 항상 survival goal의 액션을 최우선으로 배치
