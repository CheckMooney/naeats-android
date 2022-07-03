# Na eat's Android App

## 개요
오늘 먹을 메뉴를 추천해드리는 서비스 입니다.
  
## 적용 기술
- Jetpack Compose
    - 선언형 UI를 적용함으로써 보다 구현 속도가 빠르고 유지보수가 쉬운 방식으로 UI를 구현합니다.
- 관심사 분리 원칙 기반의 아키텍쳐 구성
   - 각각의 클래스가 가져야 하는 역할을 UI 레이어 및 데이터 레이어 중 하나로 명확히 합니다.
   - 역할에 따라 `*Screen`, `*ViewModel`, `*Repository`, `*DataSource` 로 구분됩니다.
- Hilt
    - DI 라이브러리를 적용하여 객체간 필요로 하는 의존성을 외부에서 주입합니다.
- Google SignIn
    - 구글 로그인을 이용하여 손쉽게 로그인이 가능합니다.
