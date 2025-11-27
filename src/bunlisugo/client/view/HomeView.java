package bunlisugo.client.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.controller.GameClient;

public class HomeView {
    private JFrame frame;
    private final GameClient client;
    
    public HomeView(GameClient client) {
    	this.client = client;
    	this.client.setHomeView(this);
    	
        frame = new JFrame("Home View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        // Initialization
        frame.getContentPane().setLayout(null);

        //닉네임라벨 생성
        JLabel NickNameLabel = new JLabel("닉네임");
        NickNameLabel.setBounds(38, 35, 187, 68);
        frame.getContentPane().add(NickNameLabel);
		
        //게임제목
        JLabel TitleLabel = new JLabel("분리수GO");
        TitleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        TitleLabel.setBounds(624, 230, 348, 208);
        frame.getContentPane().add(TitleLabel);

        //게임 로고 이미지
        JLabel LogoImageLabel = new JLabel();
        ImageIcon LogoImage = new ImageIcon("images/LogoImage.png");
		//logo.setImage(logo.getImage().getScaledInstance(348, 208, java.awt.Image.SCALE_SMOOTH));
        LogoImageLabel.setIcon(LogoImage);
		LogoImageLabel.setBounds(266, 230, 348, 208);
        frame.getContentPane().add(LogoImageLabel);
		
        //게임 시작버튼
        JButton StartButton = new JButton("게임 시작");
        StartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                frame.dispose();
                new MatchingView(client);
            }
        });
        StartButton.setBounds(454, 448, 276, 117);
        frame.getContentPane().add(StartButton);
		
        //랭킹 화면 이동 버튼
        JButton goRankingViewButton = new JButton("현재 랭킹 2위");
		goRankingViewButton.setBounds(956, 35, 187, 68);
        goRankingViewButton.addActionListener(e -> {
            // 랭킹 화면 버튼 클릭 시 동작
            // 예: 랭킹 화면으로 전환
            frame.dispose(); // 현재 홈 뷰 닫기
            new RankingView(); // 랭킹 뷰 열기 (RankingView 클래스가 있다고 가정)
        });     
		frame.getContentPane().add(goRankingViewButton);
    }
}