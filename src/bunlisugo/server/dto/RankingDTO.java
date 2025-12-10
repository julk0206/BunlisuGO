package bunlisugo.server.dto;

public class RankingDTO {
    private String username;
    private int rankingScore;

    public RankingDTO(String username, int rankingScore) {
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
