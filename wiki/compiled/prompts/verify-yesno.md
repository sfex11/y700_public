# gemma 프롬프트: yes/no 검증
용도: 현재 화면이 특정 상태/메뉴인지 확인
엔드포인트: /completion
최대 토큰: 3

## 프롬프트 템플릿
```
Is this screen showing {target}?
Answer only YES or NO.
```

## 사용 예시
- "Is this screen showing repair menu? Answer only YES or NO."
- "Is this the trade house? Answer only YES or NO."
- "Is a popup blocking the screen? Answer only YES or NO."
- "Is there an enemy fleet marker? Answer only YES or NO."
- "Is the battle result screen showing? Answer only YES or NO."

## 후처리
- YES → 진행
- NO → fallback 액션
- 응답 불명 → NO 취급 (안전 우선)
