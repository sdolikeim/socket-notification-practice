# WebSocket 상담 알림 실습

Spring Boot와 React를 사용해 상담 문의 등록 및 관리자 답변 흐름에 WebSocket 알림을 적용해본 미니 실습 프로젝트입니다.

사용자가 상담 문의를 등록하면 Oracle DB에 상담 데이터가 저장되고, 관리자 화면에는 WebSocket을 통해 새 상담 문의 알림이 표시됩니다.  
관리자가 답변을 등록하면 상담 상태가 변경되고, 사용자 화면에는 답변 완료 알림이 표시됩니다.

## 프로젝트 목적

기존 HTTP 요청/응답 방식의 상담 문의 기능에서 확장하여, WebSocket 기반 실시간 알림 흐름을 이해하기 위해 진행한 실습 프로젝트입니다.

주요 학습 목표는 다음과 같습니다.

- Spring Boot에서 WebSocket 엔드포인트 구성
- SockJS와 STOMP를 사용한 React WebSocket 연결
- 상담 문의 등록 시 관리자 알림 전송
- 관리자 답변 등록 시 사용자 알림 전송
- Oracle DB에 상담 문의 및 답변 데이터 저장
- HTTP API와 WebSocket 알림 흐름 비교 학습

## 기술 스택

### Backend

- Java
- Spring Boot
- Spring Web
- Spring WebSocket
- STOMP
- Oracle
- JPA 또는 MyBatis

### Frontend

- React
- Vite
- Axios
- SockJS
- STOMP.js
- CSS

### Database

- Oracle

## 주요 기능

- 사용자 상담 문의 등록
- 상담 문의 목록 조회
- 관리자 상담 목록 확인
- 관리자 답변 등록
- 상담 상태 변경
- 새 상담 문의 등록 시 관리자 알림
- 답변 등록 시 사용자 알림

## 프로젝트 구조

```text
websocket-notification-practice
├── backend
│   └── Spring Boot WebSocket API
├── frontend
│   └── React 상담 알림 화면
└── README.md
```

## 실행 방법

### 1. 백엔드 실행

Spring Boot 백엔드를 실행합니다.

```bash
./gradlew bootRun
```

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

백엔드 기본 주소:

```text
http://localhost:8087
```

### 2. 프론트엔드 실행

```bash
cd frontend
npm install
npm run dev
```

프론트엔드 기본 주소:

```text
http://localhost:5173
```

## 프론트엔드 주요 설정

React에서 사용하는 API 주소입니다.

```js
const API_URL = 'http://localhost:8087/api/articles'
```

WebSocket 연결 주소입니다.

```js
new SockJS('http://localhost:8087/ws')
```

관리자 알림 구독 채널입니다.

```js
client.subscribe('/topic/admin', (message) => {
  const article = JSON.parse(message.body)
  setAdminNotice(`새 상담 문의가 등록되었습니다: ${article.title}`)
})
```

사용자 알림 구독 채널입니다.

```js
client.subscribe('/topic/user', (message) => {
  const article = JSON.parse(message.body)
  setUserNotice(`답변이 등록되었습니다: ${article.title}`)
})
```

## API 목록

| Method | Endpoint | 설명 |
|---|---|---|
| GET | /api/articles | 상담 문의 목록 조회 |
| POST | /api/articles | 상담 문의 등록 |
| PUT | /api/articles/answer | 관리자 답변 등록 |

## 상담 문의 등록 요청 예시

```http
POST /api/articles
Content-Type: application/json
```

```json
{
  "memberName": "홍길동",
  "title": "배송 문의",
  "content": "상품 배송은 언제 시작되나요?"
}
```

## 관리자 답변 등록 요청 예시

```http
PUT /api/articles/answer
Content-Type: application/json
```

```json
{
  "articleId": 1,
  "answer": "주문 확인 후 1~2일 내 출고됩니다."
}
```

## WebSocket 알림 흐름

```text
1. 사용자가 상담 문의 등록
2. 백엔드가 상담 데이터를 Oracle DB에 저장
3. 백엔드가 /topic/admin 채널로 새 문의 알림 전송
4. 관리자 화면에서 새 상담 문의 알림 표시
5. 관리자가 상담 문의를 선택하고 답변 등록
6. 백엔드가 답변 내용을 DB에 저장하고 상태를 ANSWERED로 변경
7. 백엔드가 /topic/user 채널로 답변 완료 알림 전송
8. 사용자 화면에서 답변 완료 알림 표시
```

## 화면 구성

### 사용자 상담 문의 등록

- 사용자 이름 입력
- 문의 제목 입력
- 문의 내용 입력
- 문의 등록 버튼
- 답변 완료 알림 표시

### 관리자 상담 관리

- 상담 목록 새로고침
- 상담 문의 목록 조회
- 상담 상태 표시
- 상담 선택
- 관리자 답변 등록
- 새 문의 등록 알림 표시

## 상담 상태값

| 상태 | 설명 |
|---|---|
| WAITING | 답변 대기 |
| ANSWERED | 답변 완료 |

## 구현 포인트

### 1. HTTP API와 WebSocket 역할 분리

상담 문의 등록과 답변 등록은 HTTP API로 처리하고, 알림은 WebSocket으로 전송하도록 역할을 분리했습니다.

```text
HTTP API   → 데이터 저장 / 조회 / 수정
WebSocket  → 새 문의 및 답변 완료 알림
```

### 2. 관리자와 사용자 알림 채널 분리

관리자에게 전달되는 새 문의 알림과 사용자에게 전달되는 답변 완료 알림을 각각 다른 채널로 구분했습니다.

```text
/topic/admin  → 관리자 새 문의 알림
/topic/user   → 사용자 답변 완료 알림
```

### 3. DB 저장 후 알림 전송

알림만 보내는 구조가 아니라, 먼저 Oracle DB에 상담 데이터를 저장한 뒤 WebSocket 알림을 전송하도록 구성했습니다.

이를 통해 새로고침 후에도 상담 문의와 답변 내역을 다시 조회할 수 있습니다.

## 학습 내용

이 실습을 통해 HTTP 요청/응답 방식과 WebSocket 방식의 차이를 이해했습니다.

HTTP는 사용자가 요청해야 서버 응답을 받을 수 있지만, WebSocket은 연결을 유지한 상태에서 서버가 클라이언트에게 알림을 보낼 수 있습니다.

이 프로젝트에서는 상담 문의 등록과 답변 등록은 HTTP API로 처리하고, 새 문의 및 답변 완료 알림은 WebSocket으로 전달하는 방식으로 두 통신 방식을 함께 사용했습니다.

## 트러블슈팅

### WebSocket 연결 확인

브라우저 개발자 도구 Console에서 아래 로그를 확인했습니다.

```text
WebSocket connected
```

### CORS 설정

React 개발 서버와 Spring Boot 서버의 포트가 다르기 때문에 CORS 설정이 필요했습니다.

```text
React: http://localhost:5173
Spring Boot: http://localhost:8087
```

### 알림 표시 확인

상담 등록 시 관리자 알림 영역에 메시지가 표시되는지 확인했습니다.

```text
새 상담 문의가 등록되었습니다: 문의 제목
```

답변 등록 시 사용자 알림 영역에 메시지가 표시되는지 확인했습니다.

```text
답변이 등록되었습니다: 문의 제목
```

## 향후 개선 사항

- 로그인 사용자별 알림 채널 분리
- 관리자 권한 기반 상담 관리
- 읽음/안읽음 상태 추가
- 상담 문의 페이징 처리
- 답변 완료 시간 표시
- WebSocket 연결 실패 시 재연결 안내 UI 추가
- Docker Compose 실행 환경 구성

## 포트폴리오 설명

이 프로젝트는 Spring Boot와 React를 사용해 WebSocket 기반 상담 알림 흐름을 실습한 미니 프로젝트입니다.

상담 문의 등록과 관리자 답변 등록은 HTTP API로 처리하고, 새 문의 알림과 답변 완료 알림은 WebSocket으로 전송하도록 구현했습니다.  
Oracle DB에 상담 데이터를 저장하여 새로고침 후에도 상담 내역을 다시 조회할 수 있도록 구성했습니다.

완성형 서비스보다는 HTTP API와 WebSocket의 역할 차이를 이해하기 위한 실습 프로젝트입니다.