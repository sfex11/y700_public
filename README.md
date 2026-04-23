# 젬대버 오버레이 손가락 커서

대항해시대 자동화 시, 봇이 클릭하는 위치에 귀여운 하얀 장갑 손가락 커서를 게임 화면 위에 표시합니다.

## 블로그

작업일지: https://sfex11.github.io/y700_public/

## APK 다운로드

GitHub Actions에서 자동 빌드 → [Actions 페이지](../../actions)에서 APK 다운로드

## 사용법

1. APK 설치 후 실행
2. 오버레이 권한 허용
3. 젬대버 봇이 클릭 시 손가락 커서가 표시됨

## 통신

Termux → localhost:9999 TCP:
- `x,y` → 손가락 표시
- `clear` → 숨기기
- `tap,x,y` → 표시 후 0.5초 뒤 자동 숨기기
