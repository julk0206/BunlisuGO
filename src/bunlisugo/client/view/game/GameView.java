package bunlisugo.client.view.game;

import javax.swing.JFrame;

import bunlisugo.client.controller.GameController;

public class GameView {

    private JFrame frame;
    private TimePanel timePanel;
    private GameController gameController;
    private TrashBoxPanel trashBox;  

    public GameView(TimePanel timePanel, GameController gameController, TrashBoxPanel trashBox) {
        this.timePanel = timePanel;
        this.gameController = gameController;
        this.trashBox = trashBox;

        makeGameView();
        addTimePanel();
        addTrashBox();

        // 🔥 컨트롤러에 프레임/패널 연결 후 게임 시작
        gameController.setFrame(frame);
        gameController.setTimePanel(timePanel);
        gameController.setTrashBoxPanel(trashBox);
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

    private void addTrashBox() {
        frame.getContentPane().add(trashBox);
    }
}
