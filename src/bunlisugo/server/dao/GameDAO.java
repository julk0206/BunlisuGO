package bunlisugo.server.dao;

import java.util.*;
import bunlisugo.server.entity.GameRoom;
import bunlisugo.server.entity.Ranking;
import bunlisugo.server.dao.DBManager;

import java.sql.*;


public class GameDAO {
    // 게임 룸 생성
    public int createGameRoom(int player1Id, int player2Id) throws SQLException {
        // DB에 게임 세션 생성
        String sql = "INSERT INTO game_rooms (player1_id, player2_id, started_at) VALUES (?, ?, ?)";

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

    // 게임 기록 저장
    public boolean endGameRoom(GameRoom room) throws SQLException {
        // DB에서 게임 세션 종료
        String sql = "INSERT INTO game_rooms" + 
                        "(room_id, player1_id, score1, player2_id, score2, winner_id, started_at)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, room.getRoomId());
            pstmt.setString(2, room.getPlayer1Id());
            pstmt.setInt(3, room.getScore1());
            pstmt.setString(4, room.getPlayer2Id());
            pstmt.setInt(5, room.getScore2());

            if (room.getWinnerId() != null) {
                pstmt.setString(1, room.getWinnerId());
            } else {
                pstmt.setNull(1, Types.VARCHAR);
            }
            pstmt.setInt(2, room.getRoomId());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 랭킹 점수 업데이트
    public boolean updateRankingScore(String username, int rankingScore) throws SQLException {

        String sql = "UPDATE users SET rankingScore = rankingScore + ? WHERE username = ?";

        try (Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, rankingScore);
            pstmt.setString(2, username);

            return pstmt.executeUpdate() > 0;
        }
    }


    // 랭킹 리스트 조회
    public List<Ranking> getRankingList(int limit) throws SQLException {

        
        String sql = "SELECT u.username, ranking_score " +
                 "FROM users u " +
                 "ORDER BY ranking_score DESC " +
                 "LIMIT ?";

        List<Ranking> list = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    Ranking ranking = new Ranking(
                        rs.getString("username"),
                        rs.getInt("ranking_score")
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