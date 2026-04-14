---
type: action
category: event
status: draft
updated: 2026-04-15
---

# 보물 발견

## 목적
- 탐험 또는 특수 이벤트 중 보물을 획득한다

## 전제조건
- 상태: 탐험 진행 중 또는 특수 이벤트 트리거
- 화면: 보물 발견 연출 화면 표시됨

## 단계
1. 감지: 보물 상자 아이콘 및 빛 연출 출현
2. 실행: 보물 상자 tap → 개봉 연출 대기 → 확인 tap
3. 확인: 획득 아이템 목록 표시 확인

## 실패 시
- 재시도: 상자 tap 미반응 시 2초 후 재tap (최대 3회)
- 복구: 연출 멈춤 시 화면 아무 곳 tap으로 스킵 시도

## 타이밍
- 대기: 2000ms (개봉 연출 완료 대기)
- 랜덤 딜레이: 300~700ms

## ADB 명령 예시
- tap(540, 960): 보물 상자 tap
- tap(540, 1600): 확인 버튼

## 관련 state
- exploration_active: true
- treasure_event: triggered
