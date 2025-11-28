package bunlisugo.server.dao;

import java.util.*;

import bunlisugo.server.entity.GameResult;
import bunlisugo.server.entity.GameSession;
import bunlisugo.server.entity.Ranking;
import bunlisugo.server.dao.DBManager;


import java.sql.*;


public class GameDAO {
    // 게임 세션 생성
    public int createGameSession(int player1Id, int player2Id) throws SQLException {
        // DB에 게임 세션 생성
        String sql = "INSERT INTO game_sessions (player1_id, player2_id, started_at) VALUES (?, ?, ?)";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, player1Id);
            pstmt.setInt(2, player2Id);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            int rows = pstmt.executeUpdate();
            
            // 생성된 게임 세션 ID 가져오기
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return -1;
    }

    // 게임 세션 종료 (승자 업데이트)
    public boolean endGameSession(int sessionId, Integer winnerId) throws SQLException {
        // DB에서 게임 세션 종료
        String sql = "UPDATE game_sessions SET winner_id = ?, ended_at = NOW() WHERE session_id = ?";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (winnerId != null) {
                pstmt.setInt(1, winnerId);
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setInt(2, sessionId);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 게임 세션 조회
    public GameSession getGameSessionById(int sessionId) throws SQLException {
        // DB에서 게임 세션 조회
        String sql = "SELECT * FROM game_sessions WHERE session_id = ?";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sessionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    GameSession session = new GameSession(
                        rs.getInt("session_id"),
                        rs.getInt("player1_id"),
                        rs.getInt("player2_id"),
                        (Integer) rs.getObject("winner_id") != null ? rs.getInt("winner_id") : null,
                        rs.getTimestamp("started_at").toLocalDateTime(),
                        rs.getTimestamp("ended_at") != null ? rs.getTimestamp("ended_at").toLocalDateTime() : null
                    );
                    return session;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //게임 기록 저장
    public boolean saveGameRecord(GameResult gameResult) throws SQLException {
        // DB에 게임 기록 저장
        String sql = "INSERT INTO game_results (session_id, player_id, score) VALUES (?, ?, ?)";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameResult.getSessionId());
            pstmt.setInt(2, gameResult.getUserId());
            pstmt.setInt(3, gameResult.getScore());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 특정 유저 게임 기록 조회
    public List<GameResult> getGameRecordsByUserId(int userId) throws SQLException {
        // DB에서 사용자별 게임 기록 조회
        String sql = "SELECT * FROM game_results WHERE player_id = ? ORDER BY session_id DESC";

        List<GameResult> results = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {   
                while (rs.next()) {
                    GameResult gameResult = new GameResult(
                        rs.getInt("session_id"),
                        rs.getInt("player_id"),
                        rs.getInt("score")
                    );
                    results.add(gameResult);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // 랭킹 점수 업데이트
    public boolean updateRankingScore(int userId, int rankingScore) throws SQLException {

        String sql = "UPDATE users SET rankingScore = rankingScore + ? WHERE user_id = ?";

        try (Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, rankingScore);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;
        }
    }


    // 랭킹 리스트 조회
    public List<Ranking> getRankingList(int limit) throws SQLException {

        
        String sql = "SELECT u.user_id, u.username, SUM(g.score) AS total_score " +
                 "FROM game_results g " +
                 "JOIN user u ON g.player_id = u.user_id " +
                 "GROUP BY u.user_id, u.username " +
                 "ORDER BY total_score DESC " +
                 "LIMIT ?";

        List<Ranking> list = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Ranking ranking = new Ranking(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getInt("total_score")
                    );
                    list.add(ranking);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return list;
    }

}