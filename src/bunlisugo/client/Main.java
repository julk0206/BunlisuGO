package bunlisugo.client;
//import main.client.view.GameView;

import bunlisugo.client.view.LoginView;
import bunlisugo.client.view.game.GameView;

public class Main {
    private static GameClient client;
    

	public static void main(String[] args) {
		GameClient client = GameClient.getInstance();
        new LoginView(client);

    }
        }