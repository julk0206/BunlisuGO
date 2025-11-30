package bunlisugo.server.controller;

import java.util.List;
import java.util.logging.Logger;

import bunlisugo.server.entity.GameRoom;
import bunlisugo.server.service.GameService;

public class ScoreCommandHandler implements ClientCommandHandler {

    private static final Logger logger = Logger.getLogger(ScoreCommandHandler.class.getName());

    private final GameService gameService;
    private final List<GameClientHandler> clients;

    public ScoreCommandHandler(GameService gameService, List<GameClientHandler> clients) {
        this.gameService = gameService;
        this.clients = clients;
    }

    @Override
    public void handle(String[] parts, GameClientHandler sender) {

        if (parts.length < 3) {
            sender.send("SCORE_FAIL|BAD_FORMAT");
            return;
        }

        // SCORE|playerId|score
        String playerId = parts[1];
        int score;
        try {
            score = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            sender.send("SCORE_FAIL|BAD_SCORE");
            return;
        }

        GameRoom room = sender.getCurrentRoom();
        if (room == null) {
            sender.send("SCORE_FAIL|NO_ROOM");
            return;
        }

        // 서버 쪽 GameRoom에도 점수 반영
        gameService.updateScore(room, playerId, score);

        int roomId = room.getRoomId();

        // 같은 방 안의 두 명 모두에게 SCORE_UPDATE 방송
        for (GameClientHandler c : clients) {
            if (!c.isLoggedIn()) continue;
            GameRoom cr = c.getCurrentRoom();
            if (cr == null) continue;
            if (cr.getRoomId() == roomId) {
                c.send("SCORE_UPDATE|" + playerId + "|" + score);
            }
        }

        logger.info("[SCORE_UPDATE BROADCAST] room=" + roomId +
                    ", player=" + playerId + ", score=" + score);
    }
}
