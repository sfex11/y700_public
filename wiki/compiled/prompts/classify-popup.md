# gemma 프롬프트: 팝업 종류 분류
용도: event 상태에서 팝업 유형 판단
엔드포인트: /completion
최대 토큰: 5

## 프롬프트 템플릿
```
What type of popup is this?
Options: reward, notice, error, maintenance, update, discovery, quest, generic
Answer only the popup type.
```

## 정답별 액션 매핑
- reward → collect-reward
- notice → dismiss-generic
- error → handle-error
- maintenance → handle-maintenance
- update → handle-update
- discovery → discovery
- quest → quest-complete
- generic → dismiss-generic
