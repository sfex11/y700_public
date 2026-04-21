---
type: state
status: draft
updated: 2026-04-17
parent: in-port
---

# In-Port City — 도시 내부

## 화면 특징
- 도시 전경 배경 (건물, 부두, 거리)
- NPC 캐릭터 표시
- 좌측 하단 캐릭터 아이콘
- 우측 상단 미니맵

## 감지 방법
- 도시 배경 + NPC + 미니맵
- 시설 목록 없음 (자유 이동)
- 등대 아이콘 없음 (항구 전경)

## 전이 조건

| 조건 | 대상 상태 |
|------|----------|
| 미니맵 클릭 → 지도 열기 | in-port-harbor |
| NPC 클릭 → 대화창 열림 | in-port-dialog |
| 팝업/이벤트 발생 | event |
| 복귀 버튼 → 항구 전경 | in-port-harbor |

## 액션
- minimap-tap: 미니맵 클릭하여 항구 지도 열기
- walk-to-facility: 시설 방향으로 이동
