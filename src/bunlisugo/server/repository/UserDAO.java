package bunlisugo.server.repository;

import java.sql.*;
import java.util.*;

import bunlisugo.server.model.User;


public class UserDAO {
    
    // 회원가입
    public boolean createUser(User user) throws SQLException {
        // DB에 사용자 생성
        String sql = "INSERT INTO users (username, password_hash, created_at) VALUES (?, ?, ?)";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setTimestamp(3, Timestamp.valueOf(user.getCreatedAt()));

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        
    }

    // 로그인
    public User getUserByUsername(String username) throws SQLException {
        // DB에서 사용자 조회
        String sql = "SELECT * FROM users WHERE username = ?";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getInt("ranking_score"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    return user;
                }
                
            } 
    
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 사용자 ID로 조회
    public User getUserById(int userId) throws SQLException {
        // DB에서 사용자 조회
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getInt("ranking_score"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
                
            } 
    
        } 
    


    // 랭킹 조회
    public List<User> getTopRanking() {
        // DB에서 상위 랭킹 사용자 조회

        String sql = "SELECT user_id, username, ranking_score FROM users ORDER BY ranking_score DESC LIMIT 10";

        List<User> rankingList = new ArrayList<>();

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getInt("ranking_score")
                    );
                    rankingList.add(user);
                }
            }

        } catch (SQLException e) {
        e.printStackTrace();
        } 

        return rankingList;
    }

    // 랭킹 점수 업데이트
    public boolean updateRankingScore(int userId, int newScore) {
        // DB에서 사용자 랭킹 점수 업데이트
        String sql = "UPDATE users SET ranking_score = ? WHERE user_id = ?";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newScore);
            pstmt.setInt(2, userId);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}