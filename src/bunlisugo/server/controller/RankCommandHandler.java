package bunlisugo.server.controller;

import java.util.List;

import bunlisugo.server.entity.Ranking;
import bunlisugo.server.service.GameService;

public class RankCommandHandler implements ClientCommandHandler {
    private final GameService gameService; // 해당 방의 게임 서비스
    private final List<GameClientHandler> clients;

    public RankCommandHandler(GameService gameService, List<GameClientHandler> clients) {
        this.gameService = gameService;
        this.clients = clients;
    }

    @Override
    public void handle(String[] parts, GameClientHandler client) {

        try {
        List<Ranking> list = gameService.getRankingList(10);

        for (Ranking r : list) {
            client.send("RANK|" + r.getUsername() + "|" + r.getRankingScore());
        }

        } catch (Exception e) {
            client.send("RANK|ERROR");
        }
    }

}
