package bunlisugo.server.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import bunlisugo.server.dao.GameDAO;
import bunlisugo.server.entity.Ranking;

public class RankingService {

    private final GameDAO gameDAO = new GameDAO();

    public List<Ranking> getTopRanking(int limit) {
        try {
            return gameDAO.getRankingList(limit);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
