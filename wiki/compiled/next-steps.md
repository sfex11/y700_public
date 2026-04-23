---
type: plan
status: active
updated: 2026-04-15
---

# 다음 단계: 템플릿 수집 및 런타임 테스트

## 현재 상태

### 완료됨 ✅
- Phase 1: recognition-map.md (21개 액션, 89개 인식 항목)
- Phase 2: template-inventory.md (52개 신규 수집 필요)
- Phase 3: 수집 시스템 (collector_server + telegram_approver + overlay_bridge + template_saver)
- Phase 4: 런타임 (runtime.py 943줄 + recognition_loader.py + ocr_reader.py)

### 다음 해야 할 일

#### Step 1: 오버레이 APK 확장
현재 오버레이 APK는 손가락 커서 + 말풍선만 가능.
collector_server와 통신하려면:
- APK에서 탭 이벤트를 Python 서버로 전송하는 기능 추가
- 또는: Python에서 adb getevent로 탭 감지 (overlay_bridge.py에 이미 구현됨)
- **또는: Python에서 직접 스크린샷+탭 좌표 처리 (APK 수정 불필요)**

가장 간단한 방법:
```
회장님이 게임 플레이 → 특정 화면에서 스크린샷 전송 (텔레그램)
→ 봇이 "이 영역을 템플릿으로 등록할까요?" → 회장님 영역 선택 → 저장
```

#### Step 2: 템플릿 수집 (52개)
순서: login(14) → in-port(11) → sailing(10) → trade(17)

방법 A: 텔레그램으로 스크린샷 전송 → 봇이 크롭 제안
방법 B: 오버레이 APK에서 직접 탭
방법 C: 회장님이 adb로 스크린샷 캡처 후 수동 크롭

#### Step 3: 런타임 테스트
- login 루프: 앱 실행 → 로그인 → 서버 선택 → 팝업 처리
- in-port 루프: 입항 → 보급 → 출항
- 교역 루프: 교역소 진입 → 시세 확인 → 매입/매도

#### Step 4: 튜닝
- 템플릿 매칭 임계값 조정
- OCR 정확도 향상
- 전략 모드 전환 조건 튜닝
- 타이밍(대기시간) 조정

## 즉시 가능한 것
텔레그램 봇 기반 템플릿 수집은 오버레이 APK 수정 없이 바로 가능.
회장님이 게임 화면 스크린샷을 텔레그램으로 보내면 됨.
