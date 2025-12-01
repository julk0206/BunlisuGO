package bunlisugo.client.command;

import bunlisugo.client.GameClient;

public class LoginFailCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        if (client.getLoginView() != null) {
            String reason = (parts.length > 1) ? parts[1] : "알 수 없는 오류";
            client.getLoginView().onLoginFail(reason);
        }
    }
}
