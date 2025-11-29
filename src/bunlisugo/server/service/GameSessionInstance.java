// BunlisuGo/src/bunlisugo/server/service/GameSessionInstance.java
package bunlisugo.server.service;

import bunlisugo.server.controller.GameClientHandler;
import bunlisugo.server.entity.GameSession;

// 게임 세션 하나의 상태 관리
public class GameSessionInstance {

    // 현재 진행 중인 게임(한 판만 있다고 가정)
    private static GameSessionInstance instance;

    public static GameSessionInstance getInstance() {
        return instance;
    }

    // -------- 인스턴스 필드 --------
    private final GameSession session;          // 어떤 게임인지
    private GameClientHandler player1Handler;   // player1 핸들러
    private GameClientHandler player2Handler;   // player2 핸들러

    private TrashManager trashManager;
    private ScoreManager scoreManager;
    private TimerManager timerManager;

    // 결과 제출 여부 + 결과 이미 전송했는지
    private boolean p1Submitted = false;
    private boolean p2Submitted = false;
    private boolean resultSent  = false;

    public GameSessionInstance(int maxX, int maxY, GameSession session) {
        this.session = session;
        this.trashManager = new TrashManager(maxX, maxY);
        this.scoreManager = new ScoreManager();
        this.timerManager = new TimerManager(60); // 60초 게임

        // 이 인스턴스를 "현재 게임"으로 등록
        instance = this;
    }

    // -------- 결과 제출 플래그 --------
    public boolean isP1Submitted() { return p1Submitted; }
    public void setP1Submitted(boolean p1Submitted) { this.p1Submitted = p1Submitted; }

    public boolean isP2Submitted() { return p2Submitted; }
    public void setP2Submitted(boolean p2Submitted) { this.p2Submitted = p2Submitted; }

    public boolean isResultSent() { return resultSent; }
    public void setResultSent(boolean resultSent) { this.resultSent = resultSent; }

    // -------- 세션/핸들러 관련 --------
    public GameSession getSession() {
        return session;
    }

    public void setPlayerHandlers(GameClientHandler p1, GameClientHandler p2) {
        this.player1Handler = p1;
        this.player2Handler = p2;
    }

    public GameClientHandler getPlayer1Handler() {
        return player1Handler;
    }

    public GameClientHandler getPlayer2Handler() {
        return player2Handler;
    }

    // -------- 기존 관리 객체들 --------
    public TrashManager getTrashManager() {
        return this.trashManager;
    }
    public void setTrashManager(TrashManager trashManager) {
        this.trashManager = trashManager;
    }

    public ScoreManager getScoreManager() {
        return this.scoreManager;
    }
    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public TimerManager getTimerManager() {
        return this.timerManager;
    }
    public void setTimerManager(TimerManager timerManager) {
        this.timerManager = timerManager;
    }
}
