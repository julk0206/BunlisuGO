package bunlisugo.server.controller;

import java.util.List;

import bunlisugo.server.entity.Ranking;
import bunlisugo.server.service.GameService;

public class RankCommandHandler implements ClientCommandHandler {
    private final GameService gameService; 
    
    public RankCommandHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void handle(String[] parts, GameClientHandler client) {
        try {
            List<Ranking> list = gameService.getRankingList(10);

            StringBuilder sb = new StringBuilder("RANKING_RES");
            for (Ranking r : list) {
                sb.append("|")
                  .append(r.getUsername())
                  .append(",")
                  .append(r.getRankingScore());
            }
            client.send(sb.toString());

        } catch (Exception e) {
            client.send("RANKING_RES|ERROR");
        }
    }

}
