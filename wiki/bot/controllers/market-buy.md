---
type: bot
status: draft
updated: 2026-04-14
sources: []
links: [bot/controllers/master-control.md]
---

# Market Buy — 매입

## Purpose
교역소에서 지정된 품목을 매입한다.

## Detect (감지)
- 교역소 화면 확인 (/completion: "항구" + 교역소 탭)
- 가격 표시 OCR 또는 템플릿
- 인벤토리 여유 공간 확인

## Act (실행)
1. 교역소 탭 tap
2. 매입 품목 탭 선택 (또는 스크롤)
3. 가격 확인 (OCR)
4. 가격이 조건 충족 시:
   a. 수량 입력 (필요시)
   b. 매입 버튼 tap
   c. 확인 팝업 tap (있으면)
5. 매입 완료 verify

## Verify (확인)
- 인벤토리에 품목 추가 확인
- 소지금 감소 확인

## Recover (복구)
- **가격 비정상**: 가격 30% 이상 높으면 skip, 다음 품목
- **인벤토리 꽉참**: Trade 종료, In-Port로 복귀
- **매입 실패 팝업**: 팝업 닫기 후 재시도
- **3회 실패**: Recovery → In-Port

## Notes
- 가격은 매번 OCR로 확인 (실시간 변동 가능)
- 수량은 전략(play/route) 문서에서 결정
