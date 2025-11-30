package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.GameClient;

public class ResultView {

    private JFrame frame;
    private GameClient client;

    // 결과 텍스트, "내 점수"와 "상대 점수"를 넘겨받도록 변경
    public ResultView(GameClient client, String resultText, int myScore, int opponentScore) {
        this.client = client;

        frame = new JFrame("Result View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize(resultText, myScore, opponentScore);
        frame.setVisible(true);
    }
    
    public ResultView() {
        frame = new JFrame("Result View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initialize(String resultText, int myScore, int opponentScore) {
        frame.getContentPane().setLayout(null); 

        // 승패 결과 라벨    
        JLabel resultLabel = new JLabel(resultText);
        resultLabel.setBounds(369, 94, 446, 182);
        frame.getContentPane().add(resultLabel);
		
        String myName = (client != null && client.getCurrentUser() != null)
                ? client.getCurrentUser().getUsername()
                : "나";
        String otherName = "상대 플레이어";

        // 내 점수 라벨
        JLabel myScoreLabel = new JLabel(myName + " 점수 : " + myScore);
        myScoreLabel.setBounds(253, 303, 304, 114);
        frame.getContentPane().add(myScoreLabel);

        // 상대 점수 라벨
        JLabel opponentScoreLabel = new JLabel(otherName + " 점수 : " + opponentScore);
        opponentScoreLabel.setBounds(607, 303, 304, 114);
        frame.getContentPane().add(opponentScoreLabel);

        // 랭킹 화면 가기 버튼
        JButton goRankingButton = new JButton("랭킹 확인");
        goRankingButton.setBounds(291, 478, 264, 82);
        goRankingButton.addActionListener(e -> {
            frame.dispose();
            String username = client != null && client.getCurrentUser() != null
                    ? client.getCurrentUser().getUsername()
                    : "알 수 없는 사용자";

            // 랭킹에는 "내 점수"를 넘긴다
            new RankingView(client, username, myScore);
        });
        frame.getContentPane().add(goRankingButton);
		
        // 다시 매칭 버튼(다시하기)
        JButton goMatchingButton = new JButton("다시하기");
        goMatchingButton.setBounds(605, 478, 264, 82);
        goMatchingButton.addActionListener(e -> {
            frame.dispose();
            new MatchingView(client, null, null, null);
        });
        frame.getContentPane().add(goMatchingButton);

        // 홈 화면 버튼
        JButton goHomeButton = new JButton("홈 화면");
        goHomeButton.setBounds(38, 35, 158, 64);
        goHomeButton.addActionListener(e -> {
            frame.dispose();
            new HomeView(client);
        });
        frame.getContentPane().add(goHomeButton);
    }
}