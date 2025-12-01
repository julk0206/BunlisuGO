package bunlisugo.client.view.game;

import javax.swing.JLabel;
import javax.swing.JPanel;

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

    private void player1Panel() {

        player1Panel.setLayout(null); //이건 언제? 몇 번째줄에?

        User user = GameClient.getInstance().getCurrentUser();
        String player1name = user.getUsername();
        JLabel player1NameLabel = new JLabel(gameState.getMyName()+ "\n 점수: " );
        
        player1Panel.add(player1NameLabel);
        
        //점수 받아오는 거
        //저기서도 세션 받아와야 할 것 같은데 일단 null로 해놨음
        player1ScoreLabel = new JLabel(gameState.getMyScore() + "점");
        player1ScoreLabel.setBounds(10, 50, 150, 30);

        player1Panel.add(player1ScoreLabel);


    }

    private void player2Panel() {
        player2Panel.setLayout(null);

        //예은님꺼랑 연결해서 이름 받아와야할것같음 아직 잘 모르겟어 
        JLabel player2NameLabel = new JLabel(gameState.getOpponentName() + " 점수: ");
        player2NameLabel.setBounds(10, 10, 150, 30);
        player2Panel.add(player2NameLabel);
        
        //점수 받아오는 거
        //저기서도 세션 받아와야 할 것 같은데 일단 null로 해놨음
        player2ScoreLabel = new JLabel(gameState.getOpponentScore() + "점");
        player2NameLabel.setBounds(10, 50, 150, 30);

        player2Panel.add(player2NameLabel);

    }
    
    // 자기 점수 갱신 :: 자기가player1 이라는 설정이 되어 잇나?
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