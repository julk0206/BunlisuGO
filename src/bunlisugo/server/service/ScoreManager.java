package bunlisugo.server.service;

import bunlisugo.server.entity.GameRoom;

public class ScoreManager {

    private final GameRoom room;

    public ScoreManager(GameRoom room) {
        this.room = room;
    }

    // 플레이어 점수 초기값 설정
    private int player1Score = 0;
    private int player2Score =0;

    // 플레이어별 점수 조회
    public int getScore(String playerId) {
        if (playerId == room.getPlayer1Id()) {
            return player1Score;
        } else if (playerId == room.getPlayer2Id()) {
            return player2Score;
        } else {
            return 0; 
        }
    }

    // 점수 업데이트
    public void addScore(String playerId, int score) {
        if (playerId == room.getPlayer1Id()) {
            player1Score += score;
        } else if (playerId == room.getPlayer2Id()) {
            player2Score += score;
        }
    }
    
}