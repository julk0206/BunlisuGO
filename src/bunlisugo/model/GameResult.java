package bunlisugo.model;

public class GameResult {
    private int sessionId;
    private int userId;
    private int score;

    public GameResult(int sessionId, int userId, int score) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.score = score;
    }

    public GameResult(int session_id, int user_id) {
        this.sessionId = session_id;
        this.userId = user_id;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
}