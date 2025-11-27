package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import bunlisugo.client.GameClient;

public class RankingView {

private JFrame frame;
private GameClient client;
    
    public RankingView() {
        frame = new JFrame("Ranking View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        // Initialization
        frame.getContentPane().setLayout(null); 

        //돌아가기 버튼
        JButton backButton = new JButton("돌아가기");
		backButton.setBounds(38, 35, 158, 64);
        backButton.addActionListener(e -> {
            // 돌아가기 버튼 클릭 시 동작
            // 예: 이전 화면으로 전환
            frame.dispose(); // 현재 랭킹 뷰 닫기
            new HomeView(client); // 홈 뷰 열기 (HomeView 클래스가 있다고 가정)
        }); 
		frame.getContentPane().add(backButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(391, 130, 401, 517);
		frame.getContentPane().add(scrollPane);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(387, 35, 401, 69);
		frame.getContentPane().add(lblNewLabel);

        
    }
}