package bunlisugo.server.entity;

public class Ranking {
    private String username;
    private int rankingScore;

    public Ranking(String username, int rankingScore) {
        this.username = username;
        this.rankingScore = rankingScore;
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
