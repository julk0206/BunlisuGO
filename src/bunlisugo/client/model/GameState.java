package bunlisugo.client.model;

public class GameState {
    private int myScore;
    private int opponentScore;
    private String myName;
    private String opponentName;

    public GameState() {}
    
    public GameState(String myName, String opponentName) {
        this.myName = myName;
        this.opponentName = opponentName;
        this.myScore = 0;
        this.opponentScore = 0;
    }

    public int getMyScore() {
        return this.myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    public int getOpponentScore() {
        return this.opponentScore;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    public String getMyName() {
        return this.myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getOpponentName() {
        return this.opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }
    
}
