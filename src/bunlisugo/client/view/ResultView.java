package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.controller.GameClient;

public class ResultView {

private JFrame frame;
private GameClient client;
    
    public ResultView() {
        frame = new JFrame("Result View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        // Initialization
        frame.getContentPane().setLayout(null); 


		//승패 결과 라벨    
		JLabel ResultLabel = new JLabel("New label"); //결과에 따라 다름 if 문으로 넘겨받아서 그때 Text설정
		ResultLabel.setBounds(369, 94, 446, 182);
		frame.getContentPane().add(ResultLabel);
		
		//플레이어1 정보 라벨
		JLabel Player1Label = new JLabel("Player1 Info"); //플레이어 정보 받아서 설정
		Player1Label.setBounds(253, 303, 304, 114);
		frame.getContentPane().add(Player1Label);
		
		JLabel Player2Label = new JLabel("Player2 Info"); //플레이어 정보 받아서 설정   
		Player2Label.setBounds(607, 303, 304, 114);
		frame.getContentPane().add(Player2Label);
		
        //랭킹 화면 가기 버튼
		JButton goRankingButton = new JButton("랭킹 확인");
		goRankingButton.setBounds(291, 478, 264, 82);
        goRankingButton.addActionListener(e -> {
            // 홈 화면 버튼 클릭 시 동작
            // 예: 홈 화면으로 전환
            frame.dispose(); // 현재 결과 뷰 닫기
            new RankingView(); // 홈 뷰 열기 (HomeView 클래스가 있다고 가정)
        }); 
		frame.getContentPane().add(goRankingButton);
		
        //다시 매칭 버튼(다시하기)
		JButton goMatchingButton = new JButton("다시하기");
		goMatchingButton.setBounds(605, 478, 264, 82);
		frame.getContentPane().add(goMatchingButton);
        goMatchingButton.addActionListener(e -> {
            // 다시 매칭 버튼 클릭 시 동작
            // 예: 매칭 화면으로 전환
            frame.dispose(); // 현재 결과 뷰 닫기
            new MatchingView(); // 매칭 뷰 열기 (MatchingView 클래스가 있다고 가정)
        }); 

        //홈화면 가는 버튼 추가하기  + 사진도 붙일까?
        JButton goHomeButton = new JButton("홈 화면");
		goHomeButton.setBounds(38, 35, 158, 64);
        goHomeButton.addActionListener(e -> {
            // 홈 화면 버튼 클릭 시 동작
            // 예: 홈 화면으로 전환
            frame.dispose();
            new HomeView(client); // 홈 뷰 열기 (HomeView 클래스가 있다고 가정)
        }); 
		frame.getContentPane().add(goHomeButton);

    }
}