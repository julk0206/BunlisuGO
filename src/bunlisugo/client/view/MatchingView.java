package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.controller.GameClient;

public class MatchingView {

private JFrame frame;
private GameClient client;
    
    public MatchingView() {
        frame = new JFrame("Matching View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        // Initialization
        frame.getContentPane().setLayout(null);

        JLabel WaitingPlayer = new JLabel("대기 중인 플레이어: ");
        WaitingPlayer.setBounds(429, 35, 326, 102);
        frame.getContentPane().add(WaitingPlayer);
		
        JLabel WaitingMessage = new JLabel("잠시 대기하세요");
        WaitingMessage.setBounds(426, 248, 332, 66);
        frame.getContentPane().add(WaitingMessage);
		
        JButton CancelButton = new JButton("취소");
        CancelButton.setBounds(237, 478, 264, 82);
        CancelButton.addActionListener(e -> {
            // 취소 버튼 클릭 시 동작
            // 예: 매칭 취소 및 홈 화면으로 돌아가기
            //frame.dispose(); // 현재 매칭 뷰 닫기
            new HomeView(client);
            frame.dispose();
            // 홈 뷰 열기 (HomeView 클래스가 있다고 가정)이미 로그인된 상태인데 new 가 맞나?
        });
        frame.getContentPane().add(CancelButton);
		
        JButton JoinButton = new JButton("참여하기");
        JoinButton.setBounds(645, 478, 264, 82);
        JoinButton.addActionListener(e -> {
            // 참여하기 버튼 클릭 시 동작
            // 예: 게임 화면으로 전환
            //frame.dispose(); // 현재 매칭 뷰 닫기
            frame.dispose();
            new GameView(); // 게임 뷰 열기 (GameView 클래스가 있다고 가정)

        });
        frame.getContentPane().add(JoinButton);


        
    }
}