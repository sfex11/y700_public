# gemma 프롬프트: 교역 이벤트 분류
용도: 교역소에서 이벤트 배지 종류 판단
엔드포인트: /completion
최대 토큰: 5

## 프롬프트 템플릿
```
Look at the trade screen event badge.
What type of trade event is this?
Options: surge, crash, fad, oversupply, none
Answer only the event type.
```

## 정답 매핑
- surge (폭등) → 매도 급행, 매입 금지
- crash (폭락) → 매입 급행, 매도 금지
- fad (유행) → 해당 품목 매입/매도 활성
- oversupply (과잉) → 해당 품목 매도, 매입 금지
- none → 일반 교역 룰 적용
