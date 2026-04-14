# Actions 파일 작성 프롬프트

## 목표
아래 디렉토리에 게임 자동화 컨트롤러용 actions/ 파일들을 작성하시오.
각 파일은 15~60줄, 간결하게. 컨트롤러 연결 가능한 수준의 구체적 정보.

## 출력 위치
~/phone-agent/daehang-wiki/wiki/actions/

## 작성할 파일 (총 ~36개, 카테고리별 디렉토리)

### sailing/ (항해 관련)
1. auto-sail.md — 자동항해 시작/설정
2. manual-sail.md — 수동항해 (조향)
3. avoid-storm.md — 악천후 회피
4. detect-port.md — 항구 근접 감지
5. handle-durability.md — 내구도 관리 (수리 필요시 항구로)

### in-port/ (항구 관련)
6. dock.md — 항구 입항
7. depart.md — 출항
8. supply.md — 보급 (식량/자재)
9. repair.md — 수리 (선박)
10. visit-npc.md — NPC 방문/대화
11. check-inn.md — 여관 (항해사 휴식)

### trade/ (교역 관련)
12. enter-market.md — 교역소 진입
13. check-prices.md — 시세 확인
14. buy-goods.md — 물품 매입
15. sell-goods.md — 물품 매도
16. check-cargo.md — 적재량 확인
17. invest.md — 투자

### combat/ (전투 관련)
18. engage-battle.md — 전투 개시
19. flee-battle.md — 도주
20. use-skill.md — 스킬 사용
21. boarding.md — 접현전
22. collect-reward.md — 전리품 수집

### event/ (이벤트/발견)
23. discovery.md — 발견물 이벤트
24. quest-complete.md — 퀘스트 완료
25. treasure.md — 보물 발견

### popup/ (팝업 처리)
26. dismiss-generic.md — 일반 팝업 닫기
27. handle-error.md — 오류 팝업 대응
28. handle-maintenance.md — 점검 팝업 대응
29. handle-update.md — 업데이트 팝업 대응

### recovery/ (복구)
30. back-to-home.md — 백버튼으로 홈 복귀
31. restart-app.md — 앱 재시작
32. reconnect.md — 재접속

### login/ (로그인)
33. start-app.md — 앱 실행
34. select-server.md — 서버 선택
35. handle-login-popup.md — 로그인 팝업 처리
36. daily-checkin.md — 출석부/일일보상

## 포맷
```markdown
---
type: prompt
category: {카테고리}
status: draft
updated: 2026-04-15
---

# {액션명}

## 목적
- ...

## 전제조건
- 상태: ...
- 화면: ...

## 단계
1. 감지: ...
2. 실행: ...
3. 확인: ...

## 실패 시
- 재시도: ...
- 복구: ...

## 타이밍
- 대기: ...ms
- 랜덤 딜레이: ...ms

## ADB 명령 예시
- tap(x, y): ...
- swipe(x1, y1, x2, y2): ...

## 관련 state
- ...
```

## 원본 자료 위치
공식 가이드: ~/phone-agent/data/official-guide/
- beginner_항해_TIP.md, beginner_교역은_어떻게_하나요_.md, beginner_전투_방법_안내.md
- beginner_선박_이용_방법.md, beginner_선박_상세.md, beginner_항해사_상세.md
- system_*.md, content_*.md, other_*.md

기존 컨트롤러: ~/phone-agent/daehang-wiki/wiki/bot/controllers/ (참고용 포맷)
state-machine: ~/phone-agent/daehang-wiki/wiki/bot/state-machine.md

공식 가이드에서 관련 정보를 읽어서 정확한 게임 메커니즘을 반영하시오.
분류에 없는 새로운 정보/액션이 나오면 추가하시오.
