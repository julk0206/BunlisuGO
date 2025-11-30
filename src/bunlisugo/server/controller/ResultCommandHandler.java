package bunlisugo.server.controller;

import java.sql.SQLException;     

import bunlisugo.server.entity.GameRoom;
import bunlisugo.server.service.GameService;

public class ResultCommandHandler implements ClientCommandHandler {

    private final GameService gameService;
    
    public ResultCommandHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {

        if (parts.length < 2) return;

        int finalScore = Integer.parseInt(parts[1]);

        GameRoom room = session.getCurrentRoom();
        if (room == null) {
            session.send("RESULT_FAIL|NO_ROOM");
            return;
        }

        // 누가 보냈는지 세션으로 판단 + 결과 수신 플래그 세팅
        if (session.getPlayerId().equals(room.getPlayer1Id())) {
            room.setScore1(finalScore);
            room.setScore1Received(true);
        } else {
            room.setScore2(finalScore);
            room.setScore2Received(true);
        }

        // 두 사람 모두 결과 보냈을 때 한 번만 endGame 호출
        if (room.isScore1Received() && room.isScore2Received()) {
            String winnerId = gameService.determineWinner(room);
            try {
                gameService.endGame(room, winnerId);
            } catch (SQLException e) {
                e.printStackTrace();   // 필요하면 로그로 바꿔도 됨
            }
        }
    }
}
