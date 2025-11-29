// bunlisugo.client.view.game.GameScorePanel.java
package bunlisugo.client.view.game;

import javax.swing.JLabel;
import javax.swing.JPanel;

import bunlisugo.client.GameClient;
import bunlisugo.client.model.User;

public class GameScorePanel {
    private JPanel player1Panel;
    private JPanel player2Panel;

    public GameScorePanel() {
        player1Panel = new JPanel();
        player2Panel = new JPanel();

        player1Panel.setBounds(71, 34, 166, 53); //임시로 위치 잡아둠
        player2Panel.setBounds(938, 34, 166, 53);
    }

    // 점수를 외부에서 넘겨받도록 변경 (예: GameClient 가 주기적으로 호출)
    public void setupPlayer1Panel(int score) {
        player1Panel.setLayout(null);

        User user = GameClient.getInstance().getCurrentUser();
        String player1name = user.getUsername();
        JLabel player1NameLabel = new JLabel(player1name + " 점수: " + score);

        player1NameLabel.setBounds(10, 10, 150, 30);
        player1Panel.add(player1NameLabel);
    }

    public void setupPlayer2Panel(String opponentName, int score) {
        player2Panel.setLayout(null);

        JLabel player2NameLabel = new JLabel(opponentName + " 점수: " + score);
        player2NameLabel.setBounds(10, 10, 150, 30);

        player2Panel.add(player2NameLabel);
    }

    public JPanel getplayer1JPanel() {
        return player1Panel;
    }

    public JPanel getplayer2JPanel() {
        return player2Panel;
    }
}
