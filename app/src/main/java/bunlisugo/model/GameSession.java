package bunlisugo.model;

import java.time.LocalDateTime;

public class GameSession {
    private int sessionId;
    private int player1Id;
    private int player2Id;
    private Integer winnerId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public GameSession(int player1Id, int player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.startedAt = LocalDateTime.now();
    }

    public GameSession(int sessionId, int player1Id, int player2Id, Integer winnerId, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.sessionId = sessionId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.winnerId = winnerId;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getPlayer1Id() {
        return this.player1Id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public int getPlayer2Id() {
        return this.player2Id;
    }

    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
    }

    public Integer getWinnerId() {
        return this.winnerId;
    }

    public void setWinnerId(Integer winnerId) {
        this.winnerId = winnerId;
    }

    public LocalDateTime getStartedAt() {
        return this.startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return this.endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }


}
