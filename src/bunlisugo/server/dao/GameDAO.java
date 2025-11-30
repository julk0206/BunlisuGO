package bunlisugo.server.dao;

import java.util.*;
import bunlisugo.server.entity.GameRoom;
import bunlisugo.server.entity.Ranking;

import java.sql.*;

public class GameDAO {

	public int createGameRoom(String player1Name, String player2Name) throws SQLException {

	        String sql =
	            "INSERT INTO game_rooms " +
	            "  (player1_id, score1, player2_id, score2, winner_id, started_at) " +
	            "VALUES (?, 0, ?, 0, NULL, ?)";

	        try (Connection conn = DBManager.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            pstmt.setString(1, player1Name);
	            pstmt.setString(2, player2Name);
	            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

	            int rows = pstmt.executeUpdate();
	            if (rows == 0) {
	                return -1;
	            }

	            // AUTO_INCREMENT room_id 가져오기
	            try (ResultSet rs = pstmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    return rs.getInt(1);
	                }
	            }
	            return -1;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return -1;
	        }
	    }

    
    public boolean endGameRoom(GameRoom room) throws SQLException {

        String sql =
            "UPDATE game_rooms " +
            "SET score1 = ?, score2 = ?, winner_id = ? " +
            "WHERE room_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, room.getScore1());
            pstmt.setInt(2, room.getScore2());

            if (room.getWinnerId() != null) {
                pstmt.setString(3, room.getWinnerId());
            } else {
                pstmt.setNull(3, Types.VARCHAR);
            }

            pstmt.setInt(4, room.getRoomId());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 랭킹 업데이트
    public boolean updateRankingScore(String username, int rankingDelta) throws SQLException {

        String sql =
            "UPDATE users " +
            "SET ranking_score = GREATEST(ranking_score + ?, 0) " +
            "WHERE username = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, rankingDelta);
            pstmt.setString(2, username);

            return pstmt.executeUpdate() > 0;
        }
    }


    // 랭킹 리스트 조회
    // 게임별 기록 랭킹 (users.ranking_score 안 씀)
    public List<Ranking> getRankingList(int limit) throws SQLException {

        String sql =
            "SELECT username, score " +
            "FROM (" +
            "   SELECT player1_id AS username, GREATEST(score1, 0) AS score, started_at " +
            "   FROM game_rooms " +
            "   UNION ALL " +
            "   SELECT player2_id AS username, GREATEST(score2, 0) AS score, started_at " +
            "   FROM game_rooms " +
            ") t " +
            "ORDER BY score DESC, started_at DESC " +  // 점수 내림차순, 동점이면 최신 먼저
            "LIMIT ?";

        List<Ranking> list = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ranking ranking = new Ranking(
                        rs.getString("username"),
                        rs.getInt("score")        // Ranking의 rankingScore 필드를 "그 게임 점수"로 사용
                    );
                    list.add(ranking);
                }
            }
        }

        return list;
    }

}
