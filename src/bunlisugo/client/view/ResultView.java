package bunlisugo.client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.GameClient;
import bunlisugo.client.controller.GameController;
import bunlisugo.client.model.GameState;
import bunlisugo.client.view.game.CountdownPanel;
import bunlisugo.client.view.game.TimePanel;
import bunlisugo.client.view.game.TrashBoxPanel;

public class ResultView {

    private JFrame frame;
    private GameClient client;

    // 화면에서 쓸 데이터들을 필드로 보관
    private String resultText;
    private String myName;
    private String otherName;
    private int myScore;
    private int opponentScore;

    // 옛날이랑 똑같이: 결과문구 + 내점수 + 상대점수만 받기
    public ResultView(GameClient client, String resultText, int myScore, int opponentScore) {
        this.client = client;
        this.resultText = resultText;
        this.myScore = myScore;
        this.opponentScore = opponentScore;

        // 기본값
        this.myName = "나";
        this.otherName = "상대 플레이어";

        // GameState에서 이름 가져오기
        if (client != null) {
            GameState state = client.getGameState();
            if (state != null) {
                this.myName = state.getMyName();
                this.otherName = state.getOpponentName();
            } else if (client.getCurrentUser() != null) {
                this.myName = client.getCurrentUser().getUsername();
            }
        }

        frame = new JFrame("Result View");
        frame.setBounds(100, 100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize();
        frame.setVisible(true);
    }

    // 테스트용 기본 생성자 (굳이 안 써도 됨)
    public ResultView() {
        frame = new JFrame("Result View");
        frame.setBounds(100,100,1200,750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initialize() {
        frame.getContentPane().setLayout(null);

        // WIN / LOSE / DRAW
        JLabel resultLabel = new JLabel(resultText, JLabel.CENTER);
        resultLabel.setBounds(369, 94, 446, 182);
        frame.getContentPane().add(resultLabel);

        // 내 점수
        JLabel myScoreLabel = new JLabel(myName + " 점수 : " + myScore);
        myScoreLabel.setBounds(253, 303, 304, 114);
        frame.getContentPane().add(myScoreLabel);

        // 상대 점수
        JLabel opponentScoreLabel = new JLabel(otherName + " 점수 : " + opponentScore);
        opponentScoreLabel.setBounds(607, 303, 304, 114);
        frame.getContentPane().add(opponentScoreLabel);

        // 랭킹 확인 버튼
        JButton goRankingButton = new JButton("랭킹 확인");
        goRankingButton.setBounds(291, 478, 264, 82);
        goRankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                String username = myName;
                new RankingView(client, username, myScore);
            }
        });
        frame.getContentPane().add(goRankingButton);

        // 다시하기 버튼 (매칭뷰로)
        JButton goMatchingButton = new JButton("다시하기");
        goMatchingButton.setBounds(605, 478, 264, 82);
        goMatchingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

                TimePanel timePanel = new TimePanel();
                CountdownPanel countdownPanel = new CountdownPanel();
                TrashBoxPanel trashBoxPanel = new TrashBoxPanel();

                // GameController는 GameClient만 받는 생성자 사용
                GameController gameController = new GameController(client);
                gameController.setTimePanel(timePanel);
                gameController.setCountdownPanel(countdownPanel);
                gameController.setTrashBoxPanel(trashBoxPanel);

                client.setTimePanel(timePanel);
                client.setCountdownPanel(countdownPanel);
                client.setGameController(gameController);

                new MatchingView(client, timePanel, countdownPanel, gameController, trashBoxPanel);
            }
        });
        frame.getContentPane().add(goMatchingButton);

        // 홈 화면 버튼
        JButton goHomeButton = new JButton("홈 화면");
        goHomeButton.setBounds(38, 35, 158, 64);
        goHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new HomeView(client);
            }
        });
        frame.getContentPane().add(goHomeButton);
    }
}
