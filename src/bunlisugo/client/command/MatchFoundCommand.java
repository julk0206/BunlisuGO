package bunlisugo.client.command;

import bunlisugo.client.GameClient;
import bunlisugo.client.model.GameState;

public class MatchFoundCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        String opponentName = parts[1];

        if (client.getCurrentUser() != null) {
            GameState state = new GameState(client.getCurrentUser().getUsername(), opponentName);
            client.setGameState(state);
        }

        if (client.getMatchingView() != null) {
            client.getMatchingView().onMatchFound(opponentName);
        }
    }
}
