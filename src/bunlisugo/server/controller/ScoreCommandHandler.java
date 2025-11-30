package bunlisugo.server.controller;

import java.util.List;

import bunlisugo.server.service.GameService;

public class ScoreCommandHandler implements ClientCommandHandler{

    private final GameService gameService; // 해당 방의 게임 서비스
    private final List<GameClientHandler> clients; // 점수를 브로드캐스트할 클라이언트

    public ScoreCommandHandler(GameService gameService, List<GameClientHandler> clients) {
        this.gameService = gameService;
        this.clients = clients;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {
        if (parts.length < 3) {
            session.send("ERROR|BAD_FORMAT");
            return;
        }

        String playerId = parts[1];
        int score;
        try {
            score = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            session.send("ERROR|INVALID_SCORE");
            return;
        }

        // GameService에 점수 추가 → 내부에서 브로드캐스트
        gameService.addScore(playerId, score);
    }
}
