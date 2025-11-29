// bunlisugo.server.service.ScoreManager.java
package bunlisugo.server.service;

public class ScoreManager {

    // 플레이어1 / 플레이어2 점수
    private int player1Score = 0;
    private int player2Score = 0;

    // 점수 조회
    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    // 특정 플레이어(1번/2번)의 점수 조회가 필요하면:
    public int getScore(boolean isPlayer1) {
        return isPlayer1 ? player1Score : player2Score;
    }

    // 점수 업데이트
    public void addScore(boolean isPlayer1, int score) {
        if (isPlayer1) {
            player1Score += score;
        } else {
            player2Score += score;
        }
    }
}
