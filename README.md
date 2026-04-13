# 젬대버 오버레이 손가락 커서 (Overlay Finger Cursor)

대항해시대 자동화 시, 봇이 클릭하는 위치에 귀여운 하얀 장갑 손가락 커서를
실시간으로 게임 화면 위에 표시하는 안드로이드 오버레이 앱.

## 동작 방식

1. Termux (젬대버 봇) → TCP 소켓 (localhost:9999) → "x,y" 전송
2. 오버레이 앱이 손가락 이미지를 해당 좌표에 표시
3. 손가락은 클릭 전까지 계속 표시 유지
4. 실제 ADB tap 실행 후 → 손가락 사라짐 (또는 "clear" 명령 수신 시)

## 아키텍처

```
┌─────────────────┐    TCP :9999    ┌─────────────────────┐
│   젬대버 봇      │ ──────────────→ │  OverlayService      │
│   (Termux)       │   "x,y\n"      │  (Android Foreground) │
│                  │ ←────────────── │  ┌─────────────────┐ │
│  main_poc.py     │   "ok\n"       │  │ 투명 오버레이    │ │
│  cursor.py       │                │  │ 손가락 ImageView │ │
└─────────────────┘                │  └─────────────────┘ │
                                   └─────────────────────┘
```

## 기술 스펙

- **언어**: Kotlin
- **minSdk**: 26 (Android 8.0)
- **targetSdk**: 34
- **빌드**: Gradle (Gradle Wrapper 포함)
- **크기 목표**: ~500KB 이하
- **권한**: SYSTEM_ALERT_WINDOW (오버레이), FOREGROUND_SERVICE

## 프로젝트 구조

```
overlay-cursor/
├── app/
│   ├── src/main/
│   │   ├── java/com/gemmaton/cursor/
│   │   │   ├── MainActivity.kt          # 권한 요청 + 서비스 시작
│   │   │   ├── OverlayService.kt        # 포그라운드 서비스 + 오버레이
│   │   │   ├── SocketServer.kt          # TCP 서버 (x,y 수신)
│   │   │   └── CursorView.kt            # 손가락 커서 뷰
│   │   ├── res/
│   │   │   ├── drawable/
│   │   │   │   └── ic_finger.png        # 하얀 장갑 손가락 (80x120)
│   │   │   ├── layout/
│   │   │   │   └── overlay_cursor.xml   # 오버레이 레이아웃
│   │   │   └── values/
│   │   │       └── strings.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## 구현 단계

### Phase 1: 오버레이 기본 (APK 빌드 + 설치)
- [ ] Gradle 프로젝트 셋업 (Kotlin)
- [ ] MainActivity: 오버레이 권한 요청
- [ ] OverlayService: 포그라운드 서비스 + 투명 창
- [ ] CursorView: 손가락 이미지 표시
- [ ] APK 빌드 → adb install

### Phase 2: 소켓 통신
- [ ] SocketServer: localhost:9999 TCP 리슨
- [ ] 프로토콜: "x,y\n" → 손가락 이동, "clear\n" → 숨기기
- [ ] Termux cursor.py 클라이언트 작성

### Phase 3: 젬대버 통합
- [ ] main_poc.py에 cursor.show(x, y) / cursor.clear() 연동
- [ ] 탭 전송 시: 손가락 표시 → 0.5초 후 tap → 손가락 숨기기
- [ ] 텔레그램으로 손가락 합성 이미지도 전송

### Phase 4: 다듬기
- [ ] 손가락 등장/사라짐 애니메이션 (fade in/out)
- [ ] 터치 피드백 (클릭 시 살짝 크기 변화)
- [ ] 자동 시작 (부팅 시)

## 통신 프로토콜

```
Termux → App:
  "x,y\n"          → 화면 좌표 (x,y)에 손가락 표시 (실제 픽셀)
  "clear\n"        → 손가락 숨기기
  "tap,x,y\n"      → 손가락 표시 + 0.5초 후 자동 clear
  "ping\n"         → "pong\n" 응답 (연결 확인)

App → Termux:
  "ok\n"           → 명령 수신 확인
  "pong\n"         → ping 응답
  "error,msg\n"    → 에러
```

## 빌드 방법 (Termux)

```bash
# 사전 요구: Termux에서 Gradle 빌드
pkg install openjdk-17 -y  # 완료
# Gradle Wrapper가 자동 다운로드됨

cd ~/phone-agent/overlay-cursor
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 빌드 방법 (PC / GitHub Actions)

PC에서 빌드 후 APK만 Termux로 전송:
```bash
./gradlew assembleDebug
# app/build/outputs/apk/debug/app-debug.apk → adb push 또는 scp
```

GitHub Actions로 빌드 자동화도 가능 (aarch64 아닌 x86_64에서 빌드 후 artifact로 다운로드).

## 권한 요청 흐름

1. 앱 설치 후 실행 → SYSTEM_ALERT_WINDOW 권한 대화상자
2. 허용 → 알림 표시 + 오버레이 서비스 시작
3. 이후 자동 시작 (포그라운드 서비스)

## 참고

- 참고 레포: https://github.com/sfex11/y700_public
- 젬대버 봇: ~/phone-agent/
- 손가락 이미지: ~/phone-agent/overlay-cursor/ref-project/ic_finger.png
