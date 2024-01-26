## Project 구성
구조와 계층을 분리하여 각 Layer 별 역할을 명확하게 하기 위해 Proejct 를 Clean Architecture Based Multi Module 로 구성하였고, 각 UI 는 feature module 로 관리합니다.


[ Module List ]
- app : Application 의 전반적인 구성을 담당합니다.
- core:common : 공통으로 사용할 Dispatchers 및 Extension Function 을 관리합니다.
- core:data : Repository 및 Domain Layer 의 Data 를 관리합니다.
- core:designsystem : DesignSystem 의 Component 들을 관리합니다.
- core:httpclient : Okhttp Module 을 관리합니다.
- core:json : json 을 처리하기 위한 Module 을 관리합니다.
- core:local : SharedPreferences 를 통해 전달 받은 Data 를 관리합니다.
- core:network : Retrofit 및 통신을 위한 DataSource 를 관리합니다.
- core:persistence : SharedPreferences 를 관리합니다.
- core:social : Social 로그인을 처리하기 위한 모듈입니다.
- core:ui : feature 단에서 공통으로 사용할 UI Component 를 관리합니다.
- env : 환경 변수들을 관리합니다.
- feature:block : 차단 목록 화면을 구성하고 있는 Feature Module 입니다.
- feature:gate : “OAuth 로그인” 버튼과 “앱 살펴보기” 버튼이 포함된 랜딩 화면을 구성하고 있는 Feature Module 입니다.
- feature:imageviewer : 이미지 상세 화면을 구성하고 있는 Feature Module 입니다.
- feature:main : 메인 화면(검색/저장/마이페이지)을 구성하고 있는 Feature Module 입니다.


[ Flow ]
- app -> feature(ui layer) -> data(domain layer) -> network(network layer)


## 주요 기능 설명
- SharedPreferences 를 통해 저장 및 차단한 Document Data 를 내부에 적재합니다.
- Repository 에서는 조회해온 데이터를 Domain Model 로 변환합니다.
- Repository 를 통해 Data 를 관리하고, Document Data 의 저장, 차단 Event 를 처리합니다.
- 앱 최초 진입 시 내부 적재되어있는 token 값 존재 여부를 판단하여 Gate 또는 Main 화면으로 이동합니다.
- Gate 화면의 "카카오 로그인" 버튼 클릭 시 KakaoSdkDelegate 를 통해 로그인을 시도합니다.
- Gate 화면의 "살펴보기" 버튼 클릭 시 Main 화면으로 이동합니다.
- Main 화면의 경우 Jetpack Navigation 으로 BottomBar 및 화면을 구성하였습니다.
- Main 화면 하단 BottomBar 아이템 클릭 시 SearchScreen/SavedScreen/MyPageScreen 으로 전환합니다.
- Main 화면 하단 BottomBar 아이템 클릭 시 needLogin 값을 통해 로그인이 필요한 화면을 체크하고, 내부 적재 되어있는 token 값이 존재하지 않는 경우 dialog 를 노출합니다.
- dialog 의 "카카오 로그인" 버튼 클릭 시 KakaoSdkDelegate 를 통해 로그인을 시도합니다.
- SearchScreen 의 경우, TextField 에 입력된 값으로 카카오 이미지 검색 API 를 호출하고, 응답 목록 값 을 LazyVerticalGrid 를 사용하여 UI 를 구성하였고, Jetpack Paging 을 사용하여 무한 스크롤 기능을 구현하였습니다.
- SavedScreen 의 경우, LocalSupplementRepository 의 savedDocumentList 를 수집하여 Grid 형태의 UI 를 구성합니다.
- MyPageScreen 의 "차단 리스트" 버튼 클릭 시 Block 화면으로 이동합니다.
- MyPageScreen 의 "로그아웃" 버튼 클릭 시 내부 적재되어 있는 oAuthToken 를 제거하고, 앱을 재실행합니다.
- 저장하기 및 차단 아이콘 클릭 시 LocalSupplementRepository 를 통해 SaveAction 및 BlockAction 를 방출하고, saveActionFlow 및 blockActionFlow 를 수집하고 있는 화면에서 데이터를 처리합니다.
- ImageViewer 화면 이동 시 newIntent 를 통해 Parcelable 처리된 DocumentData 를 매개 변수로 담아 이동합니다.
- ImageViewer 화면 진입 시 intent 를 통해 전달받은 DocumentData 를 ViewModelFactory 를 사용하여 ViewModel 에 전달합니다.
- ImageViewerViewModel 생성 시 ViewModelFactory 를 통해 전달받은 DocumentData 로 StateFlow 를 구성합니다.
- Block 화면 진입 시 LocalSupplementRepository 의 blockedDocumentList 를 수집하여 Grid 형태의 UI 를 구성합니다.


## 특이사항
- 카카오 이미지 검색 결과중 이미지의 url 이 http 가 존재하여 http 통신을 허용 하였습니다.
- 로그아웃 시 Singleton 으로 구성되어 있는 Repository 의 데이터 및 메모리상의 데이터를 초기화하기 위해 앱을 재실행합니다.
- 요구 사항 문서 내용중 "저장하기 버튼을 누르면 앱내 스토리지에 이미지 데이터(Document 전체, 카카오 api docs 참조)가 유저의 로그인정보(Oauth Token)과 연관되어 앱 내 스토리지에 저장된 이미지 항목으로 저장되어야 함." 라고 명시되어 있지만, 카카오 로그인의 경우 로그인 시 마다 Token 값이 변경됩니다. 그러다보니 로그아웃 후 재로그인 하더라도 저장된 데이터가 보이지 않는 이슈가 있습니다.
- thumbnailUrl 를 통해 데이터를 검증 또는 변환 및 조작하는 로직이 존재합니다. 보통의 경우엔 id 값을 key 값으로 처리하나, 카카오 이미지 검색 api 의 응답 값에 id 값이 없고, thumbnailUrl 의 경우 중복되지 않을거라 판단하여 key 값으로 사용하였습니다.
- 미로그인 상태에서, 로그인이 필요한 메뉴 접근 또는 기능 동작에 대한 처리 방식이 요구 사항 문서에 존재하지 않아, 임의로 Dialog 를 노출시키고 로그인을 할 수 있도록 유도하였습니다.
