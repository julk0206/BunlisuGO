package bunlisugo.server.entity;

import java.time.LocalDateTime;

public class GameRoom {
    private int roomId;
    private String player1Id;
    private int score1;
    private String player2Id;
    private int score2;
    private String winnerId;
    private LocalDateTime startedAt;

    private boolean score1Received;
    private boolean score2Received;

    public GameRoom(String player1Id, String player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.startedAt = LocalDateTime.now();
        this.score1 = 0;
        this.score2 = 0;
        this.score1Received = false;
        this.score2Received = false;
    }

    public GameRoom(int roomId, String player1Id, int score1, String player2Id,
                    int score2, String winnerId, LocalDateTime startedAt) {
        this.roomId = roomId;
        this.player1Id = player1Id;
        this.score1 = score1;
        this.player2Id = player2Id;
        this.score2 = score2;
        this.winnerId = winnerId;
        this.startedAt = startedAt;
    }

    public int getRoomId() { return this.roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getPlayer1Id() { return this.player1Id; }
    public void setPlayer1Id(String player1Id) { this.player1Id = player1Id; }

    public String getPlayer2Id() { return this.player2Id; }
    public void setPlayer2Id(String player2Id) { this.player2Id = player2Id; }

    public String getWinnerId() { return this.winnerId; }
    public void setWinnerId(String winnerId) { this.winnerId = winnerId; }

    public LocalDateTime getStartedAt() { return this.startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public int getScore1() { return score1; }
    public void setScore1(int score1) { this.score1 = score1; }

    public int getScore2() { return score2; }
    public void setScore2(int score2) { this.score2 = score2; }

    public boolean isScore1Received() { return score1Received; }
    public void setScore1Received(boolean score1Received) { this.score1Received = score1Received; }

    public boolean isScore2Received() { return score2Received; }
    public void setScore2Received(boolean score2Received) { this.score2Received = score2Received; }
}
