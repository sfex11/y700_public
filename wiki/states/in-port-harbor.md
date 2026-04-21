---
type: state
status: draft
updated: 2026-04-17
parent: in-port
---

# In-Port Harbor — 항구 전경

## 화면 특징
- 등대 아이콘 (port_lighthouse_icon)
- 메인 UI 바 (port_main_ui)
- 출항소 배너 (port_departure_zone_banner)
- 도시 이름 라벨
- 출항소 버튼 표시

## 감지 방법
- port_lighthouse_icon.png 매칭 (신뢰도 > 0.9)
- port_main_ui.png 매칭
- OR 출항소 배너 표시

## 전이 조건

| 조건 | 대상 상태 |
|------|----------|
| 출항소 버튼 탭 → NPC 대화 | in-port-dialog |
| 미니맵 클릭 → 지도 | in-port-city |
| 조선소 진입 | in-port-dialog |
| 창고/함대 관리 진입 | in-port-warehouse |
| 팝업 발생 | event |

## 액션
- tap-departure-office: 출항소 버튼 탭
- open-minimap: 미니맵 탭
- open-fleet: 함대 관리 진입
