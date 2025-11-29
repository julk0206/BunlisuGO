package bunlisugo.client.view;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.GameClient;
import bunlisugo.client.controller.GameController;
import bunlisugo.client.view.game.TimePanel;
import bunlisugo.client.view.game.TrashBoxPanel;

public class HomeView {
    private JFrame frame;
    private final GameClient client;
    
    public HomeView(GameClient client) {
        // 넘겨받은 client를 그대로 사용 (getInstance()로 다시 안 받기)
        this.client = client;
        this.client.setHomeView(this);   // GameClient에 홈뷰 등록

        frame = new JFrame("Home View");
        frame.setBounds(100, 100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame.getContentPane().setLayout(null);

        // 닉네임 라벨 (나중에 client에서 닉네임 받아서 setText 하면 됨)
        JLabel nickNameLabel = new JLabel("닉네임");
        nickNameLabel.setBounds(38, 35, 187, 68);
        frame.getContentPane().add(nickNameLabel);
		
        // 게임 제목
        JLabel titleLabel = new JLabel("분리수GO");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setBounds(624, 230, 348, 208);
        frame.getContentPane().add(titleLabel);

        // 게임 로고 이미지
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
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

                TimePanel timePanel = new TimePanel();
                TrashBoxPanel trashBox = new TrashBoxPanel();
                GameController gameController = new GameController();

                // GameController에 화면 요소 연결
                gameController.setTimePanel(timePanel);
                gameController.setTrashBoxPanel(trashBox);

                // 매칭 화면으로 이동
                // (여기서 MatchingView 쪽에서 gameController.setClient(client) 해주면 됨)
                new MatchingView(client, timePanel, gameController, trashBox);
            }
        });
        startButton.setBounds(454, 448, 276, 117);
        frame.getContentPane().add(startButton);
		
        // 랭킹 화면 이동 버튼
        JButton goRankingViewButton = new JButton("현재 랭킹 2위");
        goRankingViewButton.setBounds(956, 35, 187, 68);
        goRankingViewButton.addActionListener(e -> {
            frame.dispose();

   
            String username = client.getNickname();   // 예: "yeeun"
            int lastScore   = client.getLastScore();  // 예: 방금 게임 끝난 최종 점수

            // 우리가 앞에서 설계한 형태: RankingView(GameClient, String, int)
            new RankingView(client, username, lastScore);
        });
        frame.getContentPane().add(goRankingViewButton);
    }
}
