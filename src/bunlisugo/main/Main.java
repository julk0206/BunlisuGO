package bunlisugo.main;

import bunlisugo.client.controller.GameClient;
import bunlisugo.client.view.LoginView;

public class Main {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	GameClient client = GameClient.getInstance();
        new LoginView(client);

    }
 }