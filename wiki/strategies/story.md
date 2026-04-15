---
type: strategy
mode: MONEY_MAKING (sub-mode)
status: draft
updated: 2026-04-15
---

# Story — 스토리 진행 전략

## 목표
연대기 챕터를 진행하여 보상을 획득하고 항해사를 영입한다.

## 진입 조건
- 스토리 퀘스트 활성화 (연대기 알림)
- 목표 도시에 근접
- 전투력 충분 (전투 퀘스트인 경우)

## goal 점수표

| goal | 조건 | 점수 |
|------|------|------|
| story_quest | 활성 스토리 퀘스트 있음 | 85 |
| chapter_reward | 챕터 클리어 직전 | 90 |
| navigator_recruit | 연대기 항해사 합류 이벤트 | 80 |
| discovery | 발견물 퀘스트 | 60 |

## action 매핑

| goal | 액션 |
|------|------|
| story_quest | accept-story-quest → progress-chapter |
| chapter_reward | complete-story |
| navigator_recruit | progress-chapter (이벤트 트리거) |
| discovery | discovery → progress-chapter |

## 주의사항
- 스토리 퀘스트는 수익성이 낮을 수 있음 → 자금 여유 확인 후 진행
- 전투 퀘스트: 전투력 충분한지 사전 평가
- 분기점 선택: 기본값은 "안전한 선택" (위험도 낮은 루트)
- 제독별 전용 퀘스트 (다른 제독 전환 필요 시 보류)

## 이탈 조건
- 자금 부족 (골드 < 5000) → MONEY_MAKING
- 내구도 위험 → RECOVERY
- 전투력 부족 → GROWTH 먼저
