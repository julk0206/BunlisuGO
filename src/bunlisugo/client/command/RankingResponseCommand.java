package bunlisugo.client.command;

import bunlisugo.client.GameClient;

import java.util.ArrayList;
import java.util.List;

public class RankingResponseCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        List<String> items = new ArrayList<>();

        for (int i = 1; i < parts.length && i <= GameClient.RANKING_LIMIT; i++) {
            items.add(parts[i]);
        }

        if (client.getRankingView() != null) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                client.getRankingView().updateRanking(items);
            });
        }
    }
}
