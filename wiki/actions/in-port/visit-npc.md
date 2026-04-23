---
type: action
category: in-port
status: draft
updated: 2026-04-15
---

# NPC 방문/대화 (Visit NPC)

## 목적
- 항구 내 시설 NPC 방문하여 퀘스트/교역/투자 등 수행

## 전제조건
- 상태: 항구 정박 중 (docked)
- 화면: 항구 메인 UI

## 시설 목록
- 여관 (inn): 선원 휴식/모집, 종업원 친밀도
- 조합 (guild): 퀘스트 수주/완료
- 왕궁 (palace): 칙명 수령
- 공관 (embassy): 항구 투자
- 도구점 (tool shop): 아이템 구매/판매
- 교역소 (market): 교역품 매매

## 단계
1. 감지: 항구 UI에서 대상 시설 아이콘 탐지
2. 실행: 시설 아이콘 tap → NPC 대화 또는 메뉴 진입
3. 확인: 시설 내부 UI 전환 확인

## 실패 시
- 재시도: 시설 아이콘 미감지 시 스크롤 후 재탐색
- 복구: 해당 시설이 없는 항구 → 스킵 후 다음 액션

## 타이밍
- 대기: 1200ms (화면 전환 대기)
- 랜덤 딜레이: 300~800ms (대화 진행 간격)

## ADB 명령 예시
- tap(facility_x, facility_y): 시설 아이콘 위치
- tap(dialog_next_x, dialog_next_y): 대화 다음 버튼
- swipe(540, 1200, 540, 600): 시설 목록 스크롤

## 관련 state
- current_port_facilities[]
- quest_status
- npc_dialog_active
