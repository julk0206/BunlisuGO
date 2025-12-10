package bunlisugo.client.view;

import javax.swing.*;

import bunlisugo.client.GameClient;

public class LoginView {

    private JFrame frame;
    private JTextField NameField;
    private JTextField PasswordField;
    private JTextField ipField;

    private final GameClient client;

    int MAX_X = 1200;
    int MAX_Y = 750;

    public LoginView(GameClient client) {
        this.client = client;
        this.client.setLoginView(this);

        frame = new JFrame("Login View");
        frame.setBounds(100, 100, MAX_X, MAX_Y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {

        frame.getContentPane().setLayout(null);

        JLabel title = new JLabel("분리수GO");
        title.setBounds(411, 91, 348, 120);
        frame.getContentPane().add(title);

        JLabel ipLabel = new JLabel("서버 IP");
        ipLabel.setBounds(223, 170, 176, 40);
        frame.getContentPane().add(ipLabel);

        ipField = new JTextField("127.0.0.1");
        ipField.setBounds(430, 170, 323, 40);
        frame.getContentPane().add(ipField);

        JLabel NameMessageLabel = new JLabel("닉네임");
        NameMessageLabel.setBounds(223, 243, 176, 66);
        frame.getContentPane().add(NameMessageLabel);

        JLabel PasswordMessageLabel = new JLabel("비밀번호");
        PasswordMessageLabel.setBounds(223, 345, 176, 66);
        frame.getContentPane().add(PasswordMessageLabel);

        NameField = new JTextField();
        NameField.setBounds(430, 243, 323, 66);
        frame.getContentPane().add(NameField);

        PasswordField = new JTextField();
        PasswordField.setBounds(430, 345, 323, 66);
        frame.getContentPane().add(PasswordField);

        JButton LoginButton = new JButton("로그인");
        LoginButton.setBounds(481, 465, 221, 66);
        frame.getContentPane().add(LoginButton);

        LoginButton.addActionListener(e -> {
            String username = NameField.getText();
            String password = PasswordField.getText();
            String ip = ipField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "아이디 또는 비밀번호를 입력하세요.");
                return;
            }

            try {
                client.connect(ip);
                client.send("LOGIN|" + username + "|" + password + "|" + MAX_X + "|" + MAX_Y);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "서버 연결 실패: " + ex.getMessage());
                return;
            }

            frame.dispose();
        });

        JButton SignButton = new JButton("계정이 없으신가요? 회원가입 하러가기");
        SignButton.setBounds(430, 549, 323, 45);
        SignButton.addActionListener(e -> {

            String ip = ipField.getText().trim();

            if (ip.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "서버 IP를 입력해주세요.");
                return;
            }

            try {
                client.connect(ip);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "서버 연결 실패: " + ex.getMessage());
                return;
            }

            frame.dispose();
            new SignView(client);
        });
        frame.getContentPane().add(SignButton);
    }

    public void onLoginSuccess(String username) {
        JOptionPane.showMessageDialog(frame, "로그인 성공: " + username);
        frame.dispose();
        new HomeView(client);
    }

    public void onLoginFail(String reason) {
        JOptionPane.showMessageDialog(frame, "로그인 실패: " + reason);
    }
}
