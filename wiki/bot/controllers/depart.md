---
type: bot
status: draft
updated: 2026-04-14
sources: []
links: [bot/controllers/master-control.md]
---

# Depart — 출항

## Purpose
항구에서 출항하여 다음 목표 항구로 항해를 시작한다.

## Detect (감지)
- 항구 화면 확인 (/completion: "항구")
- 출항 버튼 템플릟 매칭
- 식량/보급 상태 확인

## Act (실행)
1. 보급 확인 (식량 50% 이하 → 조선소/도구점 경유)
2. 출항 버튼 tap
3. 목적지 선택 (필요시)
4. 출항 확인 tap (있으면)
5. 항해 화면 verify

## Verify (확인)
- 화면 분류: "바다" 또는 "항해"
- 미니맵에 선박 위치 확인
- 식량 게이지 확인

## Recover (복구)
- **출항 버튼 없음**: 다른 화면일 가능성 → In-Port로 복귀
- **목적지 선택 실패**: 기본 루트의 다음 항구 자동 선택
- **보급 부족으로 출항 불가**: 보급 선행 → 재출항
- **3회 실패**: Recovery → In-Port

## Notes
- 출항 전 반드시 보급 상태 확인
- 다음 항구는 play/route 문서에서 결정
- 랜덤 딜레이 (2~5s)로 자연스러운 동작
