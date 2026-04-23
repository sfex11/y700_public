---
type: meta
status: active
updated: 2026-04-15
---

# Wiki Reader Guide

## 이 wiki는 무엇인가

대항해시대 오리진 게임을 자동으로 플레이하는 봇(에이전트)을 만들기 위한 지식 베이스다.
사람이 읽어 이해할 수도 있고, LLM이 읽고 판단 근거로 쓸 수도 있다.

핵심 아이디어: **3단계 분할 정복**

```
1) 스크린샷 → 현재 상태 판단     (states/)
2) 상태 + states/{state}.md → 다음 액션 선택  (actions/)
3) 액션 + actions/{action}.md → 구체적 실행   (bot/)
```

---

## 전체 구조

```
wiki/
├── readme.md              ← 안내서
├── actions/               ← "무엇을 할 수 있는가" (64개)
│   ├── login/             (4)  앱 실행, 서버선택, 팝업, 출석
│   ├── sailing/           (6)  자동항해, 수동항해, 폭풍회피, 항구감지, 재해, 내구도
│   ├── in-port/           (8)  입항, 출항, 보급, 수리, NPC, 여관, 파견, 일일퀘스트
│   ├── trade/             (8)  교역소진입, 시세, 매입, 매도, 적재, 투자, 물물교환, 밀수
│   ├── combat/            (6)  전투개시, 위임, 보상, 스킬, 접현, 도주
│   ├── shipyard/          (4)  건조, 개조, 해체, 장비
│   ├── navigator/         (5)  고용, 해고, 배치, 성장, 스킬승급
│   ├── chronicle/         (3)  챕터진행, 스토리퀘스트, 스토리완료
│   ├── guild/             (3)  상회가입, 미션, 상점
│   ├── fleet/             (2)  선단편성, 선단연구
│   ├── exchange/          (2)  거래소 구매, 판매
│   ├── social/            (2)  총리투표, 친구
│   ├── pvp/               (1)  모의전
│   ├── event/             (3)  발견물, 퀘스트완료, 보물
│   ├── popup/             (4)  일반팝업, 오류, 점검, 업데이트
│   └── recovery/          (3)  재접속, 재시작, 홈복귀
├── states/                ← "지금 어떤 화면인가" (8개)
│   ├── sailing.md              항해 중
│   ├── sailing-storm.md        폭풍 항해 (서브상태)
│   ├── in-port.md              항구 내
│   ├── trade.md                교역소 내
│   ├── combat.md               전투 중
│   ├── event.md                이벤트/팝업
│   ├── recovery.md             복구 모드
│   └── stopped.md              봇 정지
├── strategies/            ← "어떤 전략을 쓸까" (7개)
│   ├── modes.md                전략 모드 FSM (6모드 정의 + 전이도)
│   ├── survival.md             생존/복구 전략
│   ├── profit.md               수익 극대화 전략 (기본 모드)
│   ├── growth.md               성장 투자 전략
│   ├── story.md                스토리 진행 전략
│   ├── combat.md               전투 전략 (적극/회피)
│   └── bootstrap.md            초기 부팅 전략
├── compiled/              ← "런타임에 쓰는 것" (20개)
│   ├── runtime-flow.md         전체 실행 사이클 정의
│   ├── decision_tables/        (6) state×mode → goal 점수 테이블
│   │   ├── in-port.md
│   │   ├── sailing.md
│   │   ├── trade.md
│   │   ├── combat.md
│   │   ├── event.md
│   │   └── recovery-stopped.md
│   ├── checklists/             (8) state별 팩트 추출 항목
│   └── prompts/                (5) gemma용 초단문 판정 프롬프트
│       ├── classify-state.md
│       ├── select-action.md
│       ├── verify-yesno.md
│       ├── extract-trade-event.md
│       └── classify-popup.md
└── bot/                   ← "어떻게 돌아가는가" (9개)
    ├── state-machine.md        게임 상태기계 전이도
    ├── controllers/            (5) 실행 컨트롤러
    ├── PROMPT_actions.md       액션 생성 프롬프트
    ├── PROMPT_actions_phase2.md Phase2 생성 프롬프트
    └── PROMPT_states.md        상태 생성 프롬프트
```

---

## 문서 타입

| type | 위치 | 의미 | 누가 읽나 |
|------|------|------|-----------|
| `action` | actions/ | 수행 가능한 단위 행동 | LLM (액션 선택 후 실행) |
| `state` | states/ | 게임 화면 상태 정의 | LLM (화면 분류 후 상태 결정) |
| `bot` | bot/ | 자동화 제어 로직 | 개발자 + LLM |
| `prompt` | bot/ | 문서 생성용 프롬프트 | 개발자 참고용 |
| `meta` | 루트 | 안내/규칙 문서 | 사람 |

---

## 문서 규칙

### 공통 (모든 문서)

1. **Frontmatter 4필드**
   ```yaml
   type: action | state | bot | prompt | meta
   status: draft | active | verified | deprecated
   updated: YYYY-MM-DD
   sources: []  # 근거 문서 (선택)
   ```

2. **15~60줄** — 간결하게. LLM이 한 번에 읽을 수 있는 길이.

3. **독립 가독** — 각 파일은 다른 파일 없이도 이해 가능해야 한다.

4. **한국어로 작성** — 헤딩과 본문 모두 한국어.

5. **status 수명주기**
   - `draft` — 새로 작성됨, 검증 안 됨
   - `active` — 사용 중, 기본 동작 확인됨
   - `verified` — 실제 게임에서 검증됨
   - `deprecated` — 폐기, 다른 문서로 대체됨

### Action 문서 (actions/)

```markdown
# 액션명
## 목적        — 이 액션이 하는 일
## 전제조건    — 상태, 화면, 필요 자원
## 단계        — 1.감지 2.실행 3.확인
## 실패 시     — 재시도/복구 방법
## 타이밍      — 대기시간, 랜덤 딜레이
## ADB 명령    — tap/swipe 좌표 (추정치)
## 관련 state  — 참조할 상태 변수
```

- `category` 필드 = 하위 디렉토리명과 일치
- ADB 좌표는 실측 전까지 추정치
- 내부 마이크로 결정(예: 어떤 품목 고를지)도 단계에 포함

### State 문서 (states/)

```markdown
# 상태명
## 화면 특징   — 화면에서 보이는 요소
## 감지 방법   — 이 상태임을 판단하는 조건
## 전이 조건   — 어떤 상태로 갈 수 있는지
## 하위 상태   — 서브상태가 있으면 기술
## 주의사항    — 예외 케이스
```

### Bot 문서 (bot/)

상태기계, 컨트롤러, 프롬프트 등 자동화 제어에 필요한 설계 문서.

---

## 사용 흐름

```
게임 스크린샷 촬영
    ↓
LLM이 states/ 8개를 읽고 현재 상태 판단
    ↓
해당 상태의 전이 조건 확인 → 가능한 액션 후보 추림
    ↓
actions/ 에서 해당 액션 파일 읽기
    ↓
목적/전제조건/단계 확인 → ADB로 실행
    ↓
결과 확인 → 실패 시 복구 액션 수행
    ↓
모름/예외 → emergency-stop → stopped 상태
```

---

## 카테고리별 자동화 우선순위

| 우선순위 | 카테고리 | 이유 |
|----------|----------|------|
| 1순위 | login, sailing, in-port, trade | 메인 루프 (로그인→항해→입항→교역→반복) |
| 2순위 | combat, recovery, popup | 필수 대응 (전투, 오류복구, 팝업) |
| 3순위 | shipyard, navigator | 성장 (건조, 개조, 항해사 관리) |
| 4순위 | chronicle, guild, fleet, exchange | 부가 콘텐츠 |
| 5순위 | social, pvp | 사교/경쟁 (자동화 가치 낮음) |

---

## 기여 규칙

- 새 문서 작성 시 frontmatter 4필드 필수
- 기존 문서 갱신 시 `updated` 날짜 변경
- 출처 없는 확정 판단 금지 — `status: draft`로 시작
- 모순 발견 시 둘 다 `draft`로 강등 후 재검증
