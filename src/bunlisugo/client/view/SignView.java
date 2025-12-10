package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bunlisugo.client.GameClient;

public class SignView {

    private JFrame frame;
    private JTextField signNameTextField;
    private JTextField signPasswordTextField;
    private JTextField checkPasswordTextField;
    private GameClient client;

    public SignView(GameClient client) {
        this.client = client;
        frame = new JFrame("Sign View");
        frame.setBounds(100, 100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
        frame.setVisible(true);

        // 클라이언트에 SignView 저장 (SIGNUP_OK 에서 닫기 위해)
        client.setSignView(this);
    }

    private void initialize() {
        frame.getContentPane().setLayout(null);

        JLabel signNameLabel = new JLabel("닉네임");
        signNameLabel.setBounds(223, 145, 176, 66);
        frame.getContentPane().add(signNameLabel);

        JLabel signPasswordLabel = new JLabel("비밀번호");
        signPasswordLabel.setBounds(223, 243, 176, 66);
        frame.getContentPane().add(signPasswordLabel);

        JLabel checkPasswordLabel = new JLabel("비밀번호 확인");
        checkPasswordLabel.setBounds(223, 345, 176, 66);
        frame.getContentPane().add(checkPasswordLabel);

        // ────────────────────────────────
        // ★★★ 기존에 있던 라벨 복원 ★★★
        // ────────────────────────────────
        JLabel idCheckLabel = new JLabel("사용할 수 있는 아이디");
        idCheckLabel.setBounds(779, 164, 279, 29);
        frame.getContentPane().add(idCheckLabel);

        JLabel pwCheckInfoLabel = new JLabel("비밀번호를 재입력하세요");
        pwCheckInfoLabel.setBounds(779, 358, 279, 29);
        frame.getContentPane().add(pwCheckInfoLabel);

        // 입력 필드들
        signNameTextField = new JTextField();
        signNameTextField.setBounds(430, 146, 323, 66);
        frame.getContentPane().add(signNameTextField);

        signPasswordTextField = new JTextField();
        signPasswordTextField.setBounds(430, 243, 323, 66);
        frame.getContentPane().add(signPasswordTextField);

        checkPasswordTextField = new JTextField();
        checkPasswordTextField.setBounds(430, 345, 323, 66);
        frame.getContentPane().add(checkPasswordTextField);

        // 회원가입 버튼
        JButton signButton = new JButton("회원가입완료");
        signButton.setBounds(481, 465, 221, 66);

        signButton.addActionListener(e -> {
            String username = signNameTextField.getText();
            String pw = signPasswordTextField.getText();
            String pwCheck = checkPasswordTextField.getText();

            if (!pw.equals(pwCheck)) {
                JOptionPane.showMessageDialog(
                        frame,
                        "비밀번호가 일치하지 않습니다.",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 서버에 SIGNUP 전송
            client.send("SIGNUP|" + username + "|" + pw);

            JOptionPane.showMessageDialog(
                    frame,
                    "회원가입 요청을 서버에 전송했습니다.\n잠시 기다려 주세요.",
                    "INFO",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        frame.getContentPane().add(signButton);
    }

    public void close() {
        frame.dispose();
    }
}
