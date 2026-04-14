---
type: action
category: combat
status: draft
updated: 2026-04-15
---

# 도주 (Flee Battle)

## 목적
- 전투를 회피하거나 진행 중인 전투에서 이탈한다

## 전제조건
- 상태: 관찰 모드(observation) 또는 전투 중(in_combat)
- 화면: 도주 버튼이 활성화 상태
- 제한: PvP 전투 진입 후에는 도주 불가

## 단계
1. 감지: 현재 상태가 관찰 모드인지 전투 중인지 판별
2. 실행-관찰모드: "도주" 버튼 tap → 확률 기반 판정
3. 실행-전투중: 턴 시작 시 "도주" 명령 선택 → 기동력 기반 판정
4. 확인: 도주 성공 시 항해 화면 복귀 확인, 실패 시 전투 화면 유지

## 실패 시
- 재시도: 도주 실패 → 다음 턴에 재시도 (최대 3턴 연속)
- 복구: 3회 실패 시 전투 수행으로 전환 (engage-battle 전략)

## 타이밍
- 대기: 도주 판정 애니메이션 2000ms
- 랜덤 딜레이: 100~300ms

## ADB 명령 예시
- tap(300, 1700): 관찰 모드에서 "도주" 버튼
- tap(150, 1800): 전투 중 "도주" 명령 메뉴
- tap(540, 1200): 도주 확인 팝업 "예"

## 관련 state
- observation → flee_attempt → sailing (성공) / combat_ready (실패)
- in_combat → flee_attempt → sailing (성공) / in_combat (실패)
