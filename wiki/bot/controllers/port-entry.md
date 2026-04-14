---
type: bot
status: draft
updated: 2026-04-14
sources: []
links: [bot/controllers/master-control.md, bot/state-machine.md]
---

# Port Entry — 항구 진입

## Purpose
항해 중 항구에 안정적으로 진입한다.

## Detect (감지)
- 미니맵에서 항구 아이콘 표시
- "항구 진입" 버튼 또는 항구 아이콘 클릭 가능
- 로딩 화면 → 항구 UI 등장 확인

## Act (실행)
1. 화면 캡처
2. 항구 아이콘/버튼 템플릿 매칭
3. 항구 아이콘 tap
4. 진입 버튼 tap (필요시)
5. 로딩 대기 (2~5s)
6. 항구 UI 화면 verify

## Verify (확인)
- 항구 화면 분류 (/completion): "항구" 응답
- NPC/메뉴 아이콘 템플릿 매칭

## Recover (복구)
- **진입 버튼 없음**: 화면 스크롤 후 재시도
- **로딩 지연**: 10s 대기 후 재확인
- **잘못된 항구**: 뒤로가기 → 재시도
- **3회 실패**: Recovery 상태로 전환, Sailing으로 복귀

## Notes
- 항구마다 UI가 다를 수 있음 → 도시별 템플릿 필요
- 팝업 발생 시 Event 상태로 우선 처리
