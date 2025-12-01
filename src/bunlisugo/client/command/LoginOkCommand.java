package bunlisugo.client.command;

import bunlisugo.client.GameClient;
import bunlisugo.client.model.User;

public class LoginOkCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        if (client.getLoginView() != null) {
            String username = parts.length > 1 ? parts[1] : "";
            User user = new User();
            user.setUsername(username);
            client.setCurrentUser(user);
            client.getLoginView().onLoginSuccess(username);
        }
    }
}
