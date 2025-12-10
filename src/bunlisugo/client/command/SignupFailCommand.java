package bunlisugo.client.command;

import javax.swing.JOptionPane;

import bunlisugo.client.GameClient;

public class SignupFailCommand implements Command {

    @Override
    public void execute(String[] parts, GameClient client) {

        String reason = parts.length > 1 ? parts[1] : "UNKNOWN";

        String message;

        switch (reason) {
            case "USERNAME_EXISTS":
                message = "이미 존재하는 아이디입니다.";
                break;
            case "BAD_FORMAT":
                message = "잘못된 입력 형식입니다.";
                break;
            case "SERVER_ERROR":
                message = "서버 오류가 발생했습니다.";
                break;
            default:
                message = "회원가입 실패: " + reason;
                break;
        }

        JOptionPane.showMessageDialog(
                null,
                message,
                "SIGNUP FAIL",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
