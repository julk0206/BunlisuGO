package bunlisugo.server.entity;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private int rankingScore;
    private LocalDateTime createdAt;

    public User() {}

    // 회원가입용 생성자
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.rankingScore = 0;
        this.createdAt = LocalDateTime.now();
    }

    // 랭킹 리스트용 생성자 (createdAt은 DB에서 가져오지 않으므로 null 유지)
    public User(int userId, String username, int rankingScore) {
        this.userId = userId;
        this.username = username;
        this.rankingScore = rankingScore;
        this.createdAt = null;
    }

    // DB 조회용 생성자
    public User(int userId, String username, String passwordHash, int rankingScore, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rankingScore = rankingScore;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserId() { return this.userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return this.passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getRankingScore() { return this.rankingScore; }

    public void setRankingScore(int rankingScore) { this.rankingScore = rankingScore; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", rankingScore=" + rankingScore +
                ", createdAt=" + createdAt +
                '}';
        // passwordHash는 보안을 위해 출력하지 않음
    }
}
