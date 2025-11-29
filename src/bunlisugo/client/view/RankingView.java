package bunlisugo.client.view;

import java.util.List;
import javax.swing.*;

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
        frame.getContentPane().setLayout(null); 

        JButton backButton = new JButton("돌아가기");
        backButton.setBounds(38, 35, 158, 64);
        backButton.addActionListener(e -> {
            frame.dispose();
            new HomeView(client);
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

    // GameClient에서 랭킹 패킷 받으면 여기로 들어옴
    public void updateRanking(List<String> items) {
        model.clear();
        int rank = 1;
        for (String s : items) { // "username,score"
            String[] sp = s.split(",");
            if (sp.length < 2) continue;
            String name = sp[0];
            String score = sp[1];
            model.addElement(rank + "위 - " + name + " - " + score + "점");
            rank++;
        }
    }
}
