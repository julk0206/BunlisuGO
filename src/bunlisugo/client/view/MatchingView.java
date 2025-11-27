package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.GameClient;
import bunlisugo.client.controller.GameController;
import bunlisugo.client.view.game.GameView;
import bunlisugo.client.view.game.TimePanel;
import bunlisugo.client.view.game.TrashBoxPanel;

public class MatchingView {

    private JFrame frame;
    private final GameClient client;
    private final TimePanel timePanel;
    private final GameController gameController;
    private final TrashBoxPanel trashBox;

    private JLabel waitingPlayerLabel;
    private JLabel waitingMessageLabel;
    private JButton joinButton;

    public MatchingView(GameClient client,
                        TimePanel timePanel,
                        GameController gameController,
                        TrashBoxPanel trashBox) {

        this.client = client;
        this.timePanel = timePanel;
        this.gameController = gameController;
        this.trashBox = trashBox;

        // GameClient에 자기 자신 등록 (MATCH_* 이벤트 받을 수 있게)
        this.client.setMatchingView(this);

        frame = new JFrame("Matching View");
        frame.setBounds(100, 100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize();
        frame.setVisible(true);
    }

    // 필요하면 기존 no-arg 생성자도 살려서 ResultView 같은 데서 사용:
    public MatchingView() {
        this(GameClient.getInstance(),
             new TimePanel(),
             new GameController(),
             new TrashBoxPanel());
    }

    private void initialize() {
        frame.getContentPane().setLayout(null);

        waitingPlayerLabel = new JLabel("대기 중인 플레이어: ");
        waitingPlayerLabel.setBounds(429, 35, 326, 102);
        frame.getContentPane().add(waitingPlayerLabel);

        waitingMessageLabel = new JLabel("잠시 대기하세요");
        waitingMessageLabel.setBounds(426, 248, 332, 66);
        frame.getContentPane().add(waitingMessageLabel);

        JButton cancelButton = new JButton("취소");
        cancelButton.setBounds(237, 478, 264, 82);
        cancelButton.addActionListener(e -> {
            if (client != null) {
                client.send("MATCH|CANCEL");
            }
            frame.dispose();
            new HomeView(client);
        });
        frame.getContentPane().add(cancelButton);

        joinButton = new JButton("참여하기");
        joinButton.setBounds(645, 478, 264, 82);
        joinButton.addActionListener(e -> {
            if (client != null) {
                client.send("MATCH|JOIN");
            }
            joinButton.setEnabled(false);
            waitingMessageLabel.setText("상대방을 찾는 중입니다...");
        });
        frame.getContentPane().add(joinButton);
    }

    public void onMatchWaiting(int waitingCount) {
        waitingPlayerLabel.setText("대기 중인 플레이어: " + waitingCount + "명");
    }

    public void onMatchFound() {
        frame.dispose();
        new GameView(timePanel, gameController, trashBox);
    }
}
