package bunlisugo.client.command;

import bunlisugo.client.GameClient;

public class ScoreUpdateCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        String playerId = parts[1];
        int score = Integer.parseInt(parts[2]);

        String myName = (client.getCurrentUser() != null) ? client.getCurrentUser().getUsername() : null;

        if (client.getGameScorePanel() != null && myName != null) {
            if (myName.equals(playerId)) {
                client.getGameScorePanel().updateMyScore(score);
            } else {
                client.getGameScorePanel().updateOpponentScore(score);
            }
        }

        if (client.getGameState() != null && myName != null) {
            if (myName.equals(playerId)) {
                client.getGameState().setMyScore(score);
            } else {
                client.getGameState().setOpponentScore(score);
            }
        }
    }
}
