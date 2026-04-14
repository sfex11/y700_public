---
type: bot
status: draft
updated: 2026-04-14
sources: []
links: [bot/controllers/master-control.md]
---

# Market Sell — 매도

## Purpose
교역소에서 보유 품목을 매도한다.

## Detect (감지)
- 교역소 화면 확인
- 매도 탭 (또는 보유품 목록)
- 현재 매도가 확인

## Act (실행)
1. 매도 탭 tap
2. 매도 품목 선택
3. 수량 확인/입력
4. 매도가 확인 (OCR)
5. 가격 조건 충족 시 매도 버튼 tap
6. 확인 팝업 tap (있으면)
7. 매도 완료 verify

## Verify (확인)
- 인벤토리에서 품목 감소 확인
- 소지금 증가 확인

## Recover (복구)
- **가격 비정상 너무 낮음**: sell skip, 다음 도시에서 시도
- **매도 실패 팝업**: 팝업 닫기 후 재시도
- **3회 실패**: Recovery → In-Port

## Notes
- 매도가가 매입가 이하면 손실 → 반드시 가격 확인 필수
- 전략 문서에서 최소 이익률 정의
