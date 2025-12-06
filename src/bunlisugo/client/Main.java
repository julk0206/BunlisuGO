package bunlisugo.client;

import bunlisugo.client.view.LoginView;

public class Main {
    public static void main(String[] args) {
        GameClient client = GameClient.getInstance();
        new LoginView(client); 
    }
}
