package bunlisugo.server.controller;

import java.sql.SQLException;

import bunlisugo.server.entity.GameRoom;
import bunlisugo.server.service.GameService;

public class ResultCommandHandler implements ClientCommandHandler{

    private final GameService gameService;
    
    public ResultCommandHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {
        
        if (parts.length < 2) return;

        int finalScore = Integer.parseInt(parts[1]);
        
        // 세션에서 룸 정보 가져오기
        GameRoom room = session.getCurrentRoom();
        if (room == null) {
            session.send("RESULT_FAIL|NO_ROOM");
            return;
        }

        // 누가 보냈는지 세션으로 판단
        if (session.getPlayerId().equals(room.getPlayer1Id())) {
            room.setScore1(finalScore);
        } else {
            room.setScore2(finalScore);
        }

        String winnerId = gameService.determineWinner(room);
        
        try {
            gameService.endGame(room, winnerId);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
