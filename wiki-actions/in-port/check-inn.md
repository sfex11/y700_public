---
type: action
category: in-port
status: draft
updated: 2026-04-15
---

# 여관 (Check Inn)

## 목적
- 항해사 휴식으로 피로도 회복
- 새 항해사 모집 (선원 충원)
- 조합원 만남/이벤트
- 종업원 친밀도 관리

## 전제조건
- 상태: 항구 정박 중 (docked)
- 화면: 항구 메인 UI
- 해당 항구에 여관 존재

## 단계
1. 감지: 여관 아이콘 탐지
2. 실행: 여관 tap → 메뉴 선택 (휴식/모집/대화)
3. 확인: 선택한 행동 완료 UI 확인

## 여관 기능
- 휴식: 선원 피로도 회복 (두카트 소모)
- 모집: 항구별 가용 항해사 목록 → 선택 → 고용
- 조합원 만남: 특정 조건 충족 시 이벤트 발생
- 종업원 친밀도: 대화/선물로 친밀도 상승 → 보상 해금

## 실패 시
- 재시도: 모집 슬롯 부족 시 기존 선원 해고 후 재시도
- 복구: 두카트 부족 시 교역 선행

## 타이밍
- 대기: 1000ms (메뉴 전환 대기)
- 랜덤 딜레이: 400~900ms (대화/애니메이션 대기)

## ADB 명령 예시
- tap(inn_x, inn_y): 여관 진입
- tap(rest_btn_x, rest_btn_y): 휴식 버튼
- tap(recruit_btn_x, recruit_btn_y): 모집 버튼
- tap(staff_x, staff_y): 종업원 대화

## 관련 state
- crew_fatigue
- crew_count / crew_max
- inn_staff_affinity
- ducat_balance
