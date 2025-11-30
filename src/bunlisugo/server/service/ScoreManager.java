package bunlisugo.server.service;

import bunlisugo.server.entity.GameRoom;

public class ScoreManager {

    private final GameRoom room;

    public ScoreManager(GameRoom room) {
        this.room = room;
    }

    // 현재 점수 조회
    public int getScore(String playerId) {
        if (room == null || playerId == null) {
            return 0;
        }

        String p1 = room.getPlayer1Id();
        String p2 = room.getPlayer2Id();

        if (playerId.equals(p1)) {
            return room.getScore1();
        } else if (playerId.equals(p2)) {
            return room.getScore2();
        } else {
            return 0;
        }
    }

    public void setScore(String playerId, int score) {
        if (room == null || playerId == null) {
            return;
        }

        if (score < 0) {
            score = 0;   // 음수 방지
        }

        String p1 = room.getPlayer1Id();
        String p2 = room.getPlayer2Id();

        if (playerId.equals(p1)) {
            room.setScore1(score);
        } else if (playerId.equals(p2)) {
            room.setScore2(score);
        }
    }

    public void addScore(String playerId, int delta) {
        if (room == null || playerId == null) {
            return;
        }

        int current = getScore(playerId);
        int next = current + delta;
        if (next < 0) {
            next = 0;
        }
        setScore(playerId, next);
    }
}
