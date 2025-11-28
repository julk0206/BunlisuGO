package bunlisugo.server.entity;

public class Ranking {
    private int userId;
    private String username;
    private int rankingScore;

    public Ranking(int userId, String username, int rankingScore) {
        this.userId = userId;
        this.username = username;
        this.rankingScore = rankingScore;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRankingScore() {
        return this.rankingScore;
    }

    public void setRankingScore(int RankingScore) {
        this.rankingScore = RankingScore;
    }

}

