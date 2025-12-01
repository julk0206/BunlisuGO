package bunlisugo.client.command;

import bunlisugo.client.GameClient;

public class MatchWaitingCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        int waiting = Integer.parseInt(parts[1]);
        if (client.getMatchingView() != null) {
            client.getMatchingView().onMatchWaiting(waiting);
        }
    }
}
