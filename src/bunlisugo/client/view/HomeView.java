package bunlisugo.client.view;

import java.awt.Font;

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
        // 싱글톤 그대로 쓰는 구조 유지
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
        frame.getContentPane().add(nickNameLabel);

        JLabel titleLabel = new JLabel("분리수GO");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setBounds(624, 230, 348, 208);
        frame.getContentPane().add(titleLabel);

        JLabel logoImageLabel = new JLabel();
        ImageIcon logoImage = null;

        java.net.URL imgUrl = getClass().getResource("/images/logo.png");
        if (imgUrl != null) {
            logoImage = new ImageIcon(imgUrl);
        } else {
            System.out.println("이미지 파일을 찾을 수 없습니다: /images/logo.png");
        }

        logoImageLabel.setIcon(logoImage);
        logoImageLabel.setBounds(266, 230, 348, 208);
        frame.getContentPane().add(logoImageLabel);

        // 게임 시작 버튼
        JButton startButton = new JButton("게임 시작");
        startButton.addActionListener(e -> {
            frame.dispose();

            // GameController는 이제 (GameClient) 생성자만 사용
            // panel 생성보다 먼저 등록 - 그래야 NPE 발생하지 않음
            GameController gameController = new GameController(client);
            client.setGameController(gameController);

            // 패널 생성
            TimePanel timePanel = new TimePanel();
            CountdownPanel countdownPanel = new CountdownPanel();
            TrashBoxPanel trashBoxPanel = new TrashBoxPanel();

            // 컨트롤러에 화면 요소 연결
            gameController.setCountdownPanel(countdownPanel);
            gameController.setTimePanel(timePanel);
            gameController.setTrashBoxPanel(trashBoxPanel);

            // GameClient에도 공유 (TIME_UPDATE, TRASH, WINNER 처리용)
            client.setCountdownPanel(countdownPanel);
            client.setTimePanel(timePanel);
            client.setTrashBoxPanel(trashBoxPanel);
            

            // 매칭 화면으로 이동
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
