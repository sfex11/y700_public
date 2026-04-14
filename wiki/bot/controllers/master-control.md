---
type: bot
status: draft
updated: 2026-04-14
sources: []
links: [bot/state-machine.md, bot/controllers/port-entry.md, bot/controllers/market-buy.md, bot/controllers/market-sell.md, bot/controllers/depart.md]
---

# Master Control — 전체 제어 흐름

## 순서

자동화의 기본 루프는 이 순서를 따른다:

```
1. Port Entry    (항구 진입)
2. Market Read   (시세 확인)
3. Buy           (매입)
4. Supply        (보급)
5. Depart        (출항)
6. Sailing       (항해)
7. → 1번으로 반복
```

## 각 컨트롤러

| # | 컨트롤러 | 문서 | 상태 전이 |
|---|---------|------|----------|
| 1 | 항구 진입 | [port-entry](port-entry.md) | Sailing → In-Port |
| 2 | 시세 확인 | (미작성) | In-Port 유지 |
| 3 | 매입 | [market-buy](market-buy.md) | In-Port → Trade |
| 4 | 매도 | [market-sell](market-sell.md) | Trade → In-Port |
| 5 | 출항 | [depart](depart.md) | In-Port → Sailing |

## 공통 패턴

모든 컨트롤러는 동일한 구조를 따른다:

```
Detect (감지) → Act (실행) → Verify (확인)
    ↓ 실패
Recover (복구) → 재시도 (최대 3회)
    ↓ 3회 실패
Stopped (안전 정지)
```

## 감지 방법

1. **화면 분류**: /completion으로 현재 화면 상태 파악 (~1.4s)
2. **템플릿 매칭**: 등록된 템플릿으로 UI 요소 위치 확인
3. **OCR**: 텍스트(가격, 수량 등) 읽기

## 실행 방법

- ADB tap: 좌표 클릭
- ADB swipe: 드래그/스크롤
- 타이밍: 화면 전환 대기 (0.5~2s 랜덤 딜레이)

## 현재 상태

- [x] 상태기계 설계 완료
- [x] 화면 분류 테스트 완료 (/completion)
- [x] 템플릿 빌더 v2 코드 완료
- [ ] 템플릿 실제 등록 (게임 화면 필요)
- [ ] 컨트롤러 구현
- [ ] 루프 통합 테스트
