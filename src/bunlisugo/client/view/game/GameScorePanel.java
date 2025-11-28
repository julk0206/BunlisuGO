package bunlisugo.client.view.game;

import javax.swing.JLabel;
import javax.swing.JPanel;

import bunlisugo.client.GameClient;
import bunlisugo.client.model.User;
import bunlisugo.server.service.ScoreManager;

public class GameScorePanel {
    private JPanel player1Panel;
    private JPanel player2Panel;

    
    public GameScorePanel() {

        player1Panel = new JPanel();        
        player2Panel = new JPanel();

        player1Panel.setBounds(71, 34, 166, 53); //임시로 위치 잡아둠
        player2Panel.setBounds(938, 34, 166, 53); 
    } 

    private void player1Panel(JPanel playerpanel) {
        this.player1Panel = playerpanel;
        player1Panel.setLayout(null); //이건 언제? 몇 번째줄에?

        User user = GameClient.getInstance().getCurrentUser();
        String player1name = user.getUsername();
        JLabel player1NameLabel = new JLabel(player1name + "\n 점수: " );
        
        player1Panel.add(player1NameLabel);
        
        //점수 받아오는 거
        //저기서도 세션 받아와야 할 것 같은데 일단 null로 해놨음
        JLabel player1ScoreLabel = new JLabel();
        ScoreManager scoreManager = new ScoreManager(null); // ScoreManager 인스턴스 받아오기
        int player1Score = scoreManager.getScore(1); // 플레이어 1의

        player1ScoreLabel.setText(player1Score+"점");
        player1ScoreLabel.setBounds(10, 50, 150, 30);

        player1Panel.add(player1ScoreLabel);


    }

    private JPanel player2Panel() {
        JPanel player2Panel = new JPanel();
        //player1ScorePanel.setBounds(0, 0, 300, 300);
        player2Panel.setLayout(null);

        //예은님꺼랑 연결해서 이름 받아와야할것같음 아직 잘 모르겟어 
        JLabel player2NameLabel = new JLabel("Player 1 Score:");
        player2NameLabel.setBounds(10, 10, 150, 30);
        player2Panel.add(player2NameLabel);
        
        //점수 받아오는 거
        //저기서도 세션 받아와야 할 것 같은데 일단 null로 해놨음
        JLabel player2ScoreLabel = new JLabel();
        ScoreManager scoreManager = new ScoreManager(null); // ScoreManager 인스턴스 받아오기
        int player2Score = scoreManager.getScore(1); // 플레이어 1의

        player2NameLabel.setText(player2Score+"점");
        player2NameLabel.setBounds(10, 50, 150, 30);

        player2Panel.add(player2NameLabel);


        return player2Panel;
    }


    public JPanel getplayer1JPanel(){
        return player1Panel;
    }
    public JPanel getplayer2JPanel(){
        return player2Panel;
    }



}