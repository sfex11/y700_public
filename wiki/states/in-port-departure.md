---
type: state
status: draft
updated: 2026-04-17
parent: in-port
---

# In-Port Departure — 출항소

## 화면 특징
- 출항소 NPC 대화창 (dialog_nametag)
- 자동 진행/건너뛰기 버튼
- 출항소 관리 패널
- 출항 버튼

## 감지 방법
- dialog_nametag_* 매칭
- 건너뛰기/자동진행 버튼 표시
- 출항소 배너

## 전이 조건

| 조건 | 대상 상태 |
|------|----------|
| NPC 대화 진행 중 (건너뛰기 필요) | in-port-departure |
| 대화 완료 → 출항 패널 | in-port-departure |
| 출항 버튼 탭 → 항해 | sailing |
| 뒤로가기 → 항구 전경 | in-port-harbor |
| 창고/보급 진입 | in-port-warehouse |
| 팝업 발생 | event |

## 액션
- skip-dialog: 건너뛰기/자동진행 버튼 탭
- tap-depart: 출항 버튼 탭
- open-supply: 보급/창고 진입
- back: 뒤로가기
