package bunlisugo.server.service;

import bunlisugo.server.model.GameSession;

public class ScoreManager {

    private final GameSession session;

    public ScoreManager(GameSession session) {
        this.session = session;
    }

    // 플레이어 점수 초기값 설정
    private int player1Score = 0;
    private int player2Score =0;

    // 플레이어별 점수 조회
    public int getScore(int playerId) {
        if (playerId == session.getPlayer1Id()) {
            return player1Score;
        } else if (playerId == session.getPlayer2Id()) {
            return player2Score;
        } else {
            return 0; 
        }
    }

    // 점수 업데이트
    public void addScore(int playerId, int score) {
        if (playerId == session.getPlayer1Id()) {
            player1Score += score;
        } else if (playerId == session.getPlayer2Id()) {
            player2Score += score;
        }
    }
    
}