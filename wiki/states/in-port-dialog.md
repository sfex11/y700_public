---
type: state
status: draft
updated: 2026-04-17
parent: in-port
---

# In-Port Dialog — NPC 대화

## 화면 특징
- NPC 이름표 (dialog_nametag_*.png)
- 대화 말풍선
- 자동 진행/건너뛰기 버튼 (상단)
- 대화 기록 버튼

## 감지 방법
- dialog_nametag_*.png 매칭
- dialog_speech_bubble.png 매칭
- dialog_skip_btn.png 매칭

## 전이 조건

| 조건 | 대상 상태 |
|------|----------|
| 대화 완료 (끝까지 진행) | 직전 상태로 복귀 |
| 건너뛰기 탭 | in-port-dialog 또는 직전 상태 |
| 팝업 발생 | event |

## 액션
- skip-dialog: 건너뛰기/자동진행 버튼 탭
- tap-continue: 대화 계속
