package bunlisugo.model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private int rankingScore;
    private LocalDateTime createdAt;

    public User() {}

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.rankingScore = 0;
        this.createdAt = LocalDateTime.now();
    }

    public User(int userId, String username, int rankingScore) {
        this.userId = userId;
        this.username = username;
        this.rankingScore = rankingScore;
        this.rankingScore = 0;
        this.createdAt = LocalDateTime.now();
    }

    public User(int userId, String username, String passwordHash, int rankingScore, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rankingScore = rankingScore;
        this.createdAt = createdAt;
    }

    //Getters and Setters
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

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getRankingScore() {
        return this.rankingScore;
    }

    public void setRankingScore(int rankingScore) {
        this.rankingScore = rankingScore;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", rankingScore=" + rankingScore +
                ", createdAt=" + createdAt +
                '}';
    }

}   