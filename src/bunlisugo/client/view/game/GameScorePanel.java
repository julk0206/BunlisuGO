package bunlisugo.client.view.game;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import bunlisugo.client.GameClient;
import bunlisugo.client.model.GameState;
import bunlisugo.client.model.User;
import bunlisugo.server.service.ScoreManager;

public class GameScorePanel {
    GameState gameState = new GameState();
    
    private JPanel player1Panel;
    private JPanel player2Panel;

    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;

    
    public GameScorePanel(GameState gameState) {
        this.gameState = gameState;

        player1Panel = new JPanel();        
        player2Panel = new JPanel();

        player1Panel.setBounds(71, 34, 166, 53); //임시로 위치 잡아둠
        player2Panel.setBounds(938, 34, 166, 53); 

        initializePanels();
    } 

    private void initializePanels() {
        player1Panel();
        player2Panel();
    }

    // 내 점수 라벨
    private void player1Panel() {
        player1Panel.setLayout(null);

        User user = GameClient.getInstance().getCurrentUser();
        String player1name = user.getUsername();

        // 패널 자체 크기 키우기
        player1Panel.setBounds(71, 34, 166, 70); 

        // 이름 라벨
        JLabel player1NameLabel = new JLabel(player1name + " 점수:");
        player1NameLabel.setBounds(10, 10, 150, 20); // ✅ 위치 지정
        player1NameLabel.setBorder(new LineBorder(Color.GRAY, 1));
        player1Panel.add(player1NameLabel);

        // 점수 라벨
        player1ScoreLabel = new JLabel(gameState.getMyScore() + "점");
        player1ScoreLabel.setBounds(10, 35, 150, 25); // 아래로 약간 내림
        player1ScoreLabel.setBorder(new LineBorder(Color.BLUE, 1));
        player1Panel.add(player1ScoreLabel);
    }


    // 상대 점수 라벨
    private void player2Panel() {
        player2Panel.setLayout(null);

        player2Panel.setBounds(938, 34, 166, 70); 

        // 이름 라벨
        JLabel player2NameLabel = new JLabel(gameState.getOpponentName() + " 점수: ");
        player2NameLabel.setBounds(10, 10, 150, 20);  // ✅ 위치 지정
        player2NameLabel.setBorder(new LineBorder(Color.GRAY, 1));
        player2Panel.add(player2NameLabel);

        // 점수 라벨
        player2ScoreLabel = new JLabel(gameState.getOpponentScore() + "점");
        player2ScoreLabel.setBounds(10, 35, 150, 25); 
        player2ScoreLabel.setBorder(new LineBorder(Color.RED, 1));
        player2Panel.add(player2ScoreLabel);
    }
    
    // 자기 점수 갱신
    public void updateMyScore(int score) {
        player1ScoreLabel.setText(score + "점");
        gameState.setMyScore(score);
    }

    // 상대 점수 갱신 (서버 메시지로)
    public void updateOpponentScore(int score) {
        player2ScoreLabel.setText(score + "점");

        gameState.setOpponentScore(score);
    }


    public JPanel getplayer1JPanel(){
        return player1Panel;
    }
    public JPanel getplayer2JPanel(){
        return player2Panel;
    }

}