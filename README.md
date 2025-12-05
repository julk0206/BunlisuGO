# 🗑️ BunlisuGO: 실시간 분리수거 경쟁 게임

**"시간이 없다! 숨 막히는 1:1 분리수거 대결!"**

BunlisuGO는 실시간으로 화면에 나타나는 쓰레기를 올바른 분리수거함에 넣어 점수를 경쟁하는 **2인용 멀티플레이어 교육 게임**입니다.  
클라이언트–서버 간 **TCP 소켓 통신**을 사용하며, 제한 시간 **60초** 동안 더 높은 점수를 얻은 플레이어가 승리합니다.

---

## 🛠 Tech Stack

| 구분 | 기술 / 환경 | 상세 |
|---|---:|---|
| Language | Java 17 | 프로젝트 메인 언어 |
| GUI | Java Swing (JFrame, JPanel, JButton) | UI 및 드래그앤드롭 구현 |
| Network | Java Socket (TCP/IP), Multi-threading | 실시간 매칭, 동기화 |
| Persistence | JDBC / DAO Pattern | 인증, 랭킹, 게임 결과 저장 |
| IDE | IntelliJ IDEA / Eclipse | 개발 환경 |

---

## 실행 화면



---

## 💡 Key Features

- **실시간 매칭 & 채팅** — TCP 소켓 기반 멀티플레이 지원  
- **서버 주도 게임 진행** — COUNTDOWN, TIME_UPDATE, TRASH 등 모든 게임 상태는 서버가 제어  
- **실시간 점수 동기화** — 플레이어 판정 즉시 서버에 전송, 서버가 양쪽 클라이언트에 동기화 전송  
- **드래그 앤 드롭** — 마우스 조작으로 쓰레기 이동/투척  
- **정오답 판정 및 점수 반영**  
  - 정답: **+5점**  
  - 오답: **−2점** (점수는 최소 0 유지)

---

## 🏗 Architecture & Patterns

### Design Patterns
- **MVC**: client 패키지에서 Model / View / Controller 분리  
- **Singleton**: GameClient / GameServer 인스턴스 단일 유지  
- **Command Pattern**: 서버/클라이언트 메시지를 Command 객체로 처리 (`client.command`, `server.controller`)  
- **DAO Pattern**: 데이터베이스 접근 분리 (`server.dao`)

### Package Structure (핵심 디렉토리)
```
src
├── client
│   │   
│   ├───command       # 서버 응답 프로토콜 처리부
│   ├───controller    # UI 제어 및 인게임 로직
│   ├───model         # 클라이언트 모델
│   └───view          # UI 화면 및 컴포넌트
│       └───game      # 인게임 전용 패널
│
└───server
    ├───controller    # 클라이언트 요청 처리 (CommandHandler) 및 세션 관리
    ├───dao           # 데이터베이스 접근 객체
    ├───dto           # 데이터 전송 객체
    ├───entity        # 데이터베이스 및 게임 엔티티
    ├───service       # 핵심 비즈니스 로직
    ├───socket        # 서버 통신 초기화
    └───util          # 유틸리티 (예: 비밀번호 암호화)
```

## 🔁 핵심 흐름 (카운트다운 → 게임 시작)

1. **매칭 성사**: `MatchingService`가 `GameRoom` 생성 → `GameService.startGameLoop(room)` 호출  
2. **서버 카운트다운**: 서버에서 `COUNTDOWN|3`, `COUNTDOWN|2`, `COUNTDOWN|1`, `COUNTDOWN|0` 를 각 클라이언트에 브로드캐스트 (1초 간격)  
3. **클라이언트 카운트다운 처리**: 클라이언트는 `COUNTDOWN` 메시지를 받아 `CountdownCommand`가 `GameController.showCountdown()` 호출 → UI 갱신  
4. **게임 시작**: 카운트다운 완료 후 서버가 `timerManager.startTimer()` 호출 및 `TIME_UPDATE|60` 전송 → 메인 루프에서 주기적으로 `TIME_UPDATE` 와 `TRASH` 전송하여 실제 게임 진행

---

## 🏃 실행 방법

### 요구 사항
- JDK 17 이상
- MariaDB (또는 설정한 RDBMS) 및 DB 계정 설정
- `DBManager.java` 또는 환경 파일에 DB 접속 정보 설정

### 서버 실행
1. DB 설정 완료
2. `src/server/socket/GameServer.java` 실행

### 클라이언트 실행
1. `src/client/Main.java` 실행
2. 로그인 → 홈 → 게임 시작 → 매칭 → 게임 플레이

---

## 📁 주요 파일 (참고)

- Server
  - `server/socket/GameServer.java` — 서버 엔트리
  - `server/service/GameService.java` — 게임 루프/카운트다운/쓰레기 생성
  - `server/controller/SendCommandHandler.java` — 브로드캐스트 (COUNTDOWN, TIME_UPDATE, TRASH)
  - `server/dao/*` — DB 접근

- Client
  - `client/GameClient.java` — 소켓 리스너 및 Command 디스패치
  - `client/command/*` — 서버 메시지 처리(Command 구현)
  - `client/controller/GameController.java` — 게임 로직, UI 제어
  - `client/view/*` — `HomeView`, `MatchingView`, `GameView`, 인게임 패널들
