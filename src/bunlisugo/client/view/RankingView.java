package bunlisugo.client.view;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import bunlisugo.client.GameClient;

public class RankingView {

    private JFrame frame;
    private GameClient client;
    private String username;
    private int finalScore;

    private DefaultListModel<String> model;
    private JList<String> rankingList;
    
    
    public RankingView(GameClient client, String username, int finalScore) {
        this.client = client;
        this.username = username;
        this.finalScore = finalScore;

        client.setRankingView(this);  // GameClient에 등록

        frame = new JFrame("Ranking View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
        frame.setVisible(true);

        // 화면 뜨면서 랭킹 요청
        client.send("RANKING_REQ|" + GameClient.RANKING_LIMIT);
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
		
		JLabel myLabel = new JLabel(username + "님의 점수: " + finalScore);
        myLabel.setBounds(387, 35, 401, 69);
        frame.getContentPane().add(myLabel);

        model = new DefaultListModel<>();
        rankingList = new JList<>(model);

        JScrollPane scrollPane = new JScrollPane(rankingList);
        scrollPane.setBounds(391, 130, 401, 517);
        frame.getContentPane().add(scrollPane);

        
    }

    // RankingView.java
    public void addRanking(String username, String score) {
        int rank = model.getSize() + 1; // 이미 들어간 항목 수 + 1
        model.addElement(rank + "위 - " + username + " - " + score + "점");
    }

}