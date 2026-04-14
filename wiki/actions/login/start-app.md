---
type: action
category: login
status: draft
updated: 2026-04-15
---

# 앱 실행

## 목적
- 대항해시대 오리진 앱을 실행하고 로딩 완료를 대기한다

## 전제조건
- 상태: 앱 미실행 상태
- 화면: Android 홈 화면 또는 임의 화면

## 단계
1. 감지: 앱 프로세스 미실행 확인 (pidof 또는 dumpsys)
2. 실행: am start로 앱 실행 → 스플래시/로고 출현 대기 → 로딩 화면 대기
3. 확인: 로딩 완료 후 타이틀/로그인 화면 출현 확인

## 실패 시
- 재시도: 앱 미실행 시 5초 후 재시도 (최대 3회)
- 복구: 반복 실패 시 기기 재부팅 고려

## 타이밍
- 대기: 15000ms (로딩 완료까지)
- 랜덤 딜레이: 없음

## ADB 명령 예시
- am start -n com.bluepotion.uwom2/.MainActivity: 앱 실행
- pidof com.bluepotion.uwom2: 프로세스 확인

## 관련 state
- app_running: false → true
- screen: home → splash → loading → title
