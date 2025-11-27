package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import bunlisugo.client.GameClient;

public class MatchingView {

    private JFrame frame;
    private final GameClient client;

    private JLabel waitingPlayerLabel;
    private JLabel waitingMessageLabel;
    private JButton joinButton;

    // 홈에서 부를 때: new MatchingView(client);
    public MatchingView(GameClient client) {
        this.client = client;
        client.setMatchingView(this);
        frame = new JFrame("Matching View");
        frame.setBounds(100, 100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize();
        frame.setVisible(true);
    }

    // 혹시 기존 코드에 new MatchingView()가 남아 있어도 돌아가게 하고 싶으면 추가
    public MatchingView() {
        this(GameClient.getInstance());
    }

    private void initialize() {
        frame.getContentPane().setLayout(null);

        waitingPlayerLabel = new JLabel("대기 중인 플레이어: ");
        waitingPlayerLabel.setBounds(429, 35, 326, 102);
        frame.getContentPane().add(waitingPlayerLabel);

        waitingMessageLabel = new JLabel("잠시 대기하세요");
        waitingMessageLabel.setBounds(426, 248, 332, 66);
        frame.getContentPane().add(waitingMessageLabel);

        // 취소 버튼
        JButton cancelButton = new JButton("취소");
        cancelButton.setBounds(237, 478, 264, 82);
        cancelButton.addActionListener(e -> {
            // 매칭 취소 서버에 알리기
            if (client != null) {
                client.send("MATCH|CANCEL");
            }
            frame.dispose();
            new HomeView(client);
        });
        frame.getContentPane().add(cancelButton);

        // 참여하기 버튼
        joinButton = new JButton("참여하기");
        joinButton.setBounds(645, 478, 264, 82);
        joinButton.addActionListener(e -> {
            // GameView로 바로 가지 않음!
            if (client != null) {
                // 서버에 매칭 요청 보내기
                client.send("MATCH|JOIN");
            }
            // 버튼 비활성화하고 메시지만 바꿔둠
            joinButton.setEnabled(false);
            waitingMessageLabel.setText("상대방을 찾는 중입니다...");
        });
        frame.getContentPane().add(joinButton);
    }

    // (선택) GameClient에서 MATCH_WAITING, MATCH_FOUND 받을 때 호출해줄 메서드도 만들어두면 편해
    public void onMatchWaiting(int waitingCount) {
        waitingPlayerLabel.setText("대기 중인 플레이어: " + waitingCount + "명");
    }

    public void onMatchFound() {
        frame.dispose();
        new GameView();   // 두 명 매칭 완료되었을 때만 게임 화면으로
    }
}
