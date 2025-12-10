package bunlisugo.client.command;

import javax.swing.JOptionPane;

import bunlisugo.client.GameClient;
import bunlisugo.client.view.LoginView;
import bunlisugo.client.view.SignView;

public class SignupOkCommand implements Command {

    @Override
    public void execute(String[] parts, GameClient client) {

        String username = parts.length > 1 ? parts[1] : "";

        JOptionPane.showMessageDialog(
                null, "SIGNUP SUCCESS",
                username, JOptionPane.INFORMATION_MESSAGE, null
        );

        // SignView 닫기
        SignView sv = client.getSignView();
        if (sv != null) {
            sv.close();
            client.setSignView(null);
        }

        // 로그인 화면 열기
        new LoginView(client);
    }
}
