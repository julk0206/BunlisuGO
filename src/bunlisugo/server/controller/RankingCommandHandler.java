package bunlisugo.server.controller;

import java.util.List;
import java.util.logging.Logger;

import bunlisugo.server.entity.Ranking;
import bunlisugo.server.service.RankingService;

public class RankingCommandHandler implements ClientCommandHandler {
	private static final int DEFAULT_RANKING_LIMIT = 10;
    private static final Logger logger =
            Logger.getLogger(RankingCommandHandler.class.getName());

    private final RankingService rankingService;

    public RankingCommandHandler(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {
    	int limit = DEFAULT_RANKING_LIMIT;
        if (parts.length >= 2) {
            try {
                limit = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                // 무시
            }
        }

        List<Ranking> list = rankingService.getTopRanking(limit);

        StringBuilder sb = new StringBuilder("RANKING_RES");
        for (Ranking r : list) {
            sb.append("|")
              .append(r.getUsername())
              .append(",")
              .append(r.getRankingScore());
        }

        session.send(sb.toString());
    }

}
