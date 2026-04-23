# Phase 4: Python 런타임 구현 — Claude Code 프롬프트

## 작업
wiki 기반 게임 자동제어 런타임을 구현하시오.
recognition-map.md를 로드하여 상태 분류→전략 판단→액션 실행→검증 사이클을 돈다.

## 기존 코드 (반드시 재사용)
- `src/screen.py` — ADB 스크린샷 캡처 (capture(), dhash())
- `src/control.py` — ADB 탭/스와이프/오버레이 통신 (tap, swipe, bubble_show)
- `src/template_matcher.py` — NCC 템플릿 매칭 (match_template(), match_template_file(), detect_state())
- `src/llm_client.py` — gemma LLM 클라이언트
- `src/telegram_bot.py` — 텔레그램 봇
- `config.py` — 설정 (TELEGRAM_BOT_TOKEN, TELEGRAM_CHAT_ID)

## 새로 작성할 파일

### 1. `src/runtime.py` — 메인 실행 루프
```
class GameRuntime:
    def __init__(self):
        - recognition_map 로드 (wiki/compiled/recognition-map.md)
        - decision_tables 로드 (wiki/compiled/decision_tables/)
        - strategy_mode: BOOTSTRAP → MONEY_MAKING
        - current_state: unknown
        - facts: {} (추출된 팩트)
    
    async def run_loop(self):
        while running:
            1. capture screenshot
            2. classify_state() → current_state
            3. extract_facts() → facts dict
            4. evaluate_strategy() → mode 전환?
            5. select_action() → 액션 결정
            6. execute_action() → ADB 실행
            7. verify_result() → 성공/실패
            8. handle_failure() → fallback
            9. report to telegram (주기적)
    
    def classify_state(self):
        - template_matcher.detect_state() 우선
        - 실패 시 llm_client로 분류 (classify-state 프롬프트)
    
    def extract_facts(self):
        - checklist에 정의된 항목을 OCR/템플릿으로 추출
        - OCR: subprocess로 tesseract 호출
    
    def evaluate_strategy(self):
        - modes.md의 전환 조건을 룰로 평가
        - facts 기반으로 모드 전환 판단
    
    def select_action(self):
        - decision_table에서 (state, mode) → goal 점수 계산
        - 최고 점수 goal의 액션 선택
        - 후보가 2~4개면 llm_client로 선택 (select-action 프롬프트)
    
    def execute_action(self, action_name):
        - recognition_map에서 action의 precondition 확인
        - execution 단계 수행 (탭, 스와이프 등)
        - verification로 성공 판정
    
    def verify_result(self, action_name):
        - recognition_map의 verification 항목 확인
        - template/ocr/llm으로 성공 여부 판정
    
    def handle_failure(self, action_name):
        - on_fail 정책에 따라 재시도/abort/복구
```

### 2. `src/recognition_loader.py` — recognition-map 파서
```python
def load_recognition_map(path) -> dict:
    """recognition-map.md → {action_name: {precondition: [...], execution: [...], verification: [...]}}"""
    # YAML 블록 파싱
    # 각 항목을 RecognitionItem으로 변환
```

### 3. `src/ocr_reader.py` — Tesseract OCR 래퍼
```python
def extract_text(image_path, region=None, lang="kor+eng") -> str:
    """이미지에서 텍스트 추출. region=[x,y,w,h]로 영역 제한 가능."""

def match_pattern(text, pattern) -> bool:
    """텍스트가 패턴(정규식)과 매치되는지 확인"""
```

## 참고 파일 경로
- Wiki 경로: `~/y700_public/wiki/`
- recognition-map: `~/y700_public/wiki/compiled/recognition-map.md`
- decision_tables: `~/y700_public/wiki/compiled/decision_tables/` (6개 .md)
- prompts: `~/y700_public/wiki/compiled/prompts/` (5개 .md)
- checklists: `~/y700_public/wiki/compiled/checklists/` (8개 .md)
- modes: `~/y700_public/wiki/strategies/modes.md`
- 템플릿: `~/phone-agent/templates/`

## 구현 규칙
1. 기존 코드(screen.py, control.py, template_matcher.py)는 수정하지 말고 import하여 사용
2. 모든 ADB 통신은 기존 함수 재사용
3. OCR은 subprocess로 tesseract 직접 호출 (설치됨: tesseract 5.5.2)
4. LLM 호출은 llm_client 재사용
5. 로깅 충분히 추가 (각 단계마다 logger.info)
6. asyncio 기반 (텔레그램 봇과 병행)
7. 설정값은 상수로 정의 (LOOP_INTERVAL=4, MAX_RETRIES=3 등)
8. 인식 실패 시 fallback 체인: template → ocr → llm

## 산출물
- `src/runtime.py` (메인 런타임, 약 300~400줄 예상)
- `src/recognition_loader.py` (파서, 약 100줄)
- `src/ocr_reader.py` (OCR 래퍼, 약 50줄)

## 검증
작성 완료 후:
1. 각 파일이 import 에러 없는지 확인
2. `python -c "from src.runtime import GameRuntime; print('OK')"` 로 테스트
3. `python -c "from src.recognition_loader import load_recognition_map; print(load_recognition_map('~/y700_public/wiki/compiled/recognition-map.md').keys())"` 로 테스트
