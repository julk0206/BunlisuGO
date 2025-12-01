package bunlisugo.client.command;

import bunlisugo.client.GameClient;

public class WinnerCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        String winnerId = parts[1];

        if (client.getGameController() != null) {
            client.getGameController().showResult(winnerId);
        }
    }
}
