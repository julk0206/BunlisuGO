package bunlisugo.client.view;

import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.GameClient;
import bunlisugo.client.controller.GameController;
import bunlisugo.client.view.game.CountdownPanel;
import bunlisugo.client.view.game.TimePanel;
import bunlisugo.client.view.game.TrashBoxPanel;

public class HomeView {
    private JFrame frame;
    private final GameClient client;

    public HomeView(GameClient client) {
        this.client = GameClient.getInstance();
        this.client.setHomeView(this);

        frame = new JFrame("Home View");
        frame.setBounds(100, 100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame.getContentPane().setLayout(null);

        JLabel nickNameLabel = new JLabel("닉네임");
        nickNameLabel.setBounds(38, 35, 187, 68);
        // 현재 로그인한 유저 닉네임 띄우기
        String nickname = client.getNickname();
        if (nickname != null && !nickname.isEmpty()) {
            nickNameLabel.setText("환영합니다, " + nickname + "님!");
        }
        
        frame.getContentPane().add(nickNameLabel);

        JLabel titleLabel = new JLabel("분리수GO");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setBounds(624, 230, 348, 208);
        frame.getContentPane().add(titleLabel);

        // 로고 라벨 설정
        JLabel logoImageLabel = new JLabel();
        logoImageLabel.setBounds(266, 230, 348, 208);  // 라벨 먼저 크기 설정

        java.net.URL imgUrl = getClass().getResource("/images/logo.png");
        if (imgUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imgUrl);

            // 라벨 크기에 맞게 이미지 스케일링
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                logoImageLabel.getWidth(),
                logoImageLabel.getHeight(),
                Image.SCALE_SMOOTH
            );

            logoImageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            System.out.println("이미지 파일을 찾을 수 없습니다: /images/logo.png");
        }

        frame.getContentPane().add(logoImageLabel);

        // 게임 시작 버튼
        JButton startButton = new JButton("게임 시작");
        startButton.addActionListener(e -> {
            frame.dispose();

            GameController gameController = new GameController(client);
            client.setGameController(gameController);

            TimePanel timePanel = new TimePanel();
            CountdownPanel countdownPanel = new CountdownPanel();
            TrashBoxPanel trashBoxPanel = new TrashBoxPanel();

            gameController.setCountdownPanel(countdownPanel);
            gameController.setTimePanel(timePanel);
            gameController.setTrashBoxPanel(trashBoxPanel);

            client.setCountdownPanel(countdownPanel);
            client.setTimePanel(timePanel);
            client.setTrashBoxPanel(trashBoxPanel);

            new MatchingView(client, timePanel, countdownPanel, gameController, trashBoxPanel);
        });
        startButton.setBounds(454, 448, 276, 117);
        frame.getContentPane().add(startButton);

        // 랭킹 버튼
        JButton goRankingViewButton = new JButton("랭킹 보기");
        goRankingViewButton.setBounds(956, 35, 187, 68);
        goRankingViewButton.addActionListener(e -> {
            frame.dispose();

            String username = client.getNickname();
            int finalScore = client.getLastScore();

            new RankingView(client, username, finalScore);
        });
        frame.getContentPane().add(goRankingViewButton);
    }
}
