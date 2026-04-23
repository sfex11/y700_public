# gemma 프롬프트: 상태 분류
용도: 스크린샷을 보고 현재 state 판단
엔드포인트: /completion
최대 토큰: 10

## 프롬프트 템플릿
```
Look at the screenshot. Which state is this?
Options: sailing, sailing-storm, in-port, trade, combat, event, recovery
Answer only the state name. One word.
```

## 정답 예시
- 바다 배경 + 미니맵 → sailing
- 바다 + 비/폭풐 + 어두움 → sailing-storm
- 도시 파노라마 + 건물 아이콘들 → in-port
- 물품 목록 + 시세 % → trade
- 헥스맵 + HP바 + 턴 표시 → combat
- 팝업/오버레이 → event
- 알 수 없음/이상 → recovery

## 후처리
- 정답이 options에 없으면 → recovery
- confidence 낮으면 → recovery
