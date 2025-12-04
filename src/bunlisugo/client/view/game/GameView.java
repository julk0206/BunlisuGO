package bunlisugo.client.view.game;

import javax.swing.JFrame;

import bunlisugo.client.controller.GameController;

public class GameView {

    private JFrame frame;
    private TimePanel timePanel;
    private CountdownPanel countdownPanel;
    private GameController gameController;
    private TrashBoxPanel trashBoxPanel;  
    private GameScorePanel gamescorePanel;

    public GameView(TimePanel timePanel, CountdownPanel countdownPanel, GameController gameController, TrashBoxPanel trashBoxPanel, GameScorePanel gamescorePanel) {
        this.timePanel = timePanel;
        this.countdownPanel = countdownPanel;
        this.gameController = gameController;
        this.trashBoxPanel = trashBoxPanel;
        this.gamescorePanel = gamescorePanel;

        makeGameView();
        addCountdownPanel();
        addTimePanel();
        addTrashBoxPanel();
        addScorePanel();

        // 컨트롤러에 프레임/패널 연결 후 게임 시작
        gameController.setGameFrame(frame);   // ★ 여기 수정
        gameController.setCountdownPanel(countdownPanel);
        gameController.setTimePanel(timePanel);
        gameController.setTrashBoxPanel(trashBoxPanel);
        gameController.setGameScorePanel(gamescorePanel);

        gameController.startGame();

        frame.setVisible(true);
    }
    
    private void makeGameView() {
        frame = new JFrame("Game View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.getContentPane().setLayout(null);
    }

    private void addTimePanel() {
        timePanel.setBounds(400, 10, 200, 50);
        frame.getContentPane().add(timePanel);
    }

    private void addTrashBoxPanel() {
        frame.getContentPane().add(trashBoxPanel);
    }

    private void addScorePanel(){
        frame.getContentPane().add(gamescorePanel.getplayer1JPanel());
        frame.getContentPane().add(gamescorePanel.getplayer2JPanel());
    }

    private void addCountdownPanel() {
        countdownPanel.setBounds(400, 250, 400, 200);
        frame.getContentPane().add(countdownPanel);
    }
}