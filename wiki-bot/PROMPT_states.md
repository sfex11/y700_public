# States 파일 작성 프롬프트

## 목표
아래 디렉토리에 게임 자동화 상태기계용 states/ 파일들을 작성하시오.
각 파일은 15~60줄, 간결하게.

## 출력 위치
~/phone-agent/daehang-wiki/wiki/states/

## 작성할 파일 (8개)

### 1. sailing.md — 항해 중
- 화면: 바다, 미니맵, 나침반, 풍향표시
- 감지: 파란 바다 배경, 미니맵 좌상단, 목적지 방향 화살표
- 전이: 항구 근접→in-port, NPC 조우→event, 해적→combat, 폭풍→sailing-storm
- 주의: 자동항해 중 화면과 수동항해 화면 구분

### 2. in-port.md — 항구 도착
- 화면: 항구 전경, NPC 목록, 메뉴 버튼들
- 감지: 건물/부두 배경, "교역소"/"선박소"/"여관" 등 NPC/버튼
- 전이: 교역소→trade, 출항→sailing, 팝업→popup
- 하위 상태: port-menu(메뉴선택), port-dialog(NPC대화)

### 3. trade.md — 교역 중
- 화면: 교역소 화면, 물품 목록, 가격, 적재량
- 감지: "매입"/"매도" 탭, 물품 리스트, 두카트 표시
- 전이: 매입완료→in-port, 매도완료→in-port, 실패→recovery
- 하위: trade-buy, trade-sell, trade-confirm

### 4. combat.md — 전투
- 화면: 전투 화면, 함대 HP, 사거리 표시
- 감지: 전투 UI, "전투"/"도주" 버튼, HP 바
- 전이: 승리→sailing, 패배→recovery, 도주→sailing
- 전투 유형: NPC전투, 해적, PvP

### 5. event.md — 이벤트/팝업
- 화면: 다양한 팝업/이벤트 창
- 감지: "확인"/"취소" 버튼, 이벤트 텍스트
- 하위: popup-generic(일반팝업), popup-reward(보상), popup-error(오류), popup-maintenance(점검), popup-update(업데이트)
- 전이: 확인→이전 상태, 알 수 없음→recovery

### 6. sailing-storm.md — 악천후 항해
- 화면: 폭풍/악천후 효과, 선박 피해 표시
- 감지: 비/번개 효과, 파도 높이, 선박 내구도 감소
- 전이: 폭풍 종료→sailing, 침몰 위험→recovery
- 위험: 내파/침수 피해, 선단 LV 필요 해역

### 7. recovery.md — 복구
- 오류 발생 시 진입
- 복구 전략: 백버튼 연타→홈으로→재진입, 앱 재시작
- 최대 3회 시도
- 3회 실패→stopped

### 8. stopped.md — 정지
- 사람 개입 필요
- 자동 복구 불가 상태
- 알림 발송 (Telegram)

## 포맷
```markdown
---
type: state
status: draft
updated: 2026-04-15
---

# {상태명}

## 화면 특징
- ...

## 감지 방법
- ...

## 전이 조건
| 조건 | 대상 상태 |
|------|----------|
| ... | ... |

## 하위 상태
- ...

## 주의사항
- ...
```

## 원본 자료 위치
공식 가이드: ~/phone-agent/data/official-guide/ (beginner_항해_TIP.md, beginner_교역은_어떻게_하나요_.md, beginner_전투_방법_안내.md 등)
나무위키: ~/phone-agent/data/crawled/namuwiki/ (대항해시대 온라인*.md)
기존 state-machine: ~/phone-agent/daehang-wiki/wiki/bot/state-machine.md

공식 가이드에서 관련 정보를 읽어서 정확한 내용을 반영하시오.
