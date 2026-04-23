---
type: state
status: draft
updated: 2026-04-17
parent: in-port
---

# In-Port Warehouse — 창고/함대 관리

## 화면 특징
- "창고" 타이틀
- 좌측 메뉴 탭 (선창, 장비함, 항해사 위킹, 도구함, 부품함)
- 보급품 수치 표시 (식량/물)
- 선창 관리 버튼
- 뒤로가기 화살표

## 감지 방법
- wh_tab_cargo.png 매칭 (선창 탭)
- wh_back_arrow.png 매칭
- "창고" 텍스트 OCR

## 전이 조건

| 조건 | 대상 상태 |
|------|----------|
| 뒤로가기 탭 | in-port-harbor 또는 in-port-departure |
| 보급 완료 | in-port-harbor 또는 in-port-departure |
| 팝업 발생 | event |

## 액션
- supply-fill: 보급품 채우기
- back: 뒤로가기
