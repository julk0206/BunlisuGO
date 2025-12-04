package bunlisugo.client.command;

import bunlisugo.client.GameClient;
import bunlisugo.client.controller.GameController;

public class CountdownCommand implements Command {

    private GameController gameController;

    @Override
    public void execute(String[] parts, GameClient client) {
        
        int sec = Integer.parseInt(parts[1]);

        GameController gameController = client.getGameController();

        gameController.showCountdown(sec);
    }

}