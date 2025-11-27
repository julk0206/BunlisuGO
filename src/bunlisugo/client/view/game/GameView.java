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
        //showTrashes();   
        
        makeGameView(); //프레임 틀 만들기 
        addTimePanel(); //시간 패널 붙이는 함수 호출  
        addTrashBox();

        frame.setVisible(true);
    }
    
    //기본 화면 만들기 
    private void makeGameView() {
        
        frame = new JFrame("Game View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        frame.getContentPane().setLayout(null);
    }

    //시간패널 붙이기 (근데 얘가 있을 필요가잇나?)
    private void addTimePanel() {
        timePanel.setBounds(400, 10, 200, 50);
        frame.getContentPane().add(timePanel);
    }

    //쓰레기통 패널 붙이기 
    private void addTrashBox() {
        frame.getContentPane().add(trashBox);
    }

    //얘는점수패널, 시간 패널이라서 다시 안 봐도 될 듯 
    // private void initialize() {
    //     JLabel myScoreLabel = new JLabel("내 점수:" );
    //     myScoreLabel.setOpaque(true);
	// 	myScoreLabel.setBackground(Color.BLUE);
	// 	myScoreLabel.setBounds(71, 34, 166, 53);
	// 	frame.getContentPane().add(myScoreLabel);
	// 	JLabel opponentScoreLabel= new JLabel("상대방 점수:");
    //     opponentScoreLabel.setOpaque(true);
	// 	opponentScoreLabel.setBackground(Color.RED);
	// 	opponentScoreLabel.setBounds(938, 34, 166, 53);
	// 	frame.getContentPane().add(opponentScoreLabel);
	// 	JLabel TimeLabel = new JLabel("시간");
	// 	TimeLabel.setBounds(375, 21, 418, 31);
	// 	frame.getContentPane().add(TimeLabel);
    //     makeTrashBox();
    //     }
    
   


    // private void showTrashes() {
    //     try {
    //         List<Trash> trashes = new ArrayList<>(); 
    //         GameController gameController = new GameController();       
    //         trashes = gameController.getRandomTrashes(10);
    //         for (Trash t : trashes) {
    //             //System.out.println("Trash: " + trash.getName() + ", Type: " + trash.getType());
    //            JButton btn = new JButton(t.getName());
    //            btn.setIcon(new ImageIcon(t.getImagePath()));
    //            btn.setBounds((int) (100*(Math.random())), (int) (100*(Math.random())), 150, 150);
    //            moveButton(btn);
    //            System.out.println("path " + t.getImagePath());
    //            //System.out.println("exits"+ new.java.io.File(t.getImagePath()).exists());
    //            frame.getContentPane().add(btn); 
    //         }
    //         frame.revalidate();
    //         frame.repaint();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

//     private void moveButton(JButton btn) {
//         // TODO Auto-generated method stub
//         MouseAdapter ma = new MouseAdapter() {
//             Point initialClick;

//             public void mousePressed(MouseEvent e) {
//                 initialClick = e.getPoint();
//                 btn.getComponentAt(initialClick);
//             }

//             public void mouseDragged(MouseEvent e) {
//                 // get location of Window
//                 int thisX = btn.getLocation().x;
//                 int thisY = btn.getLocation().y;

//                 // Determine how much the mouse moved since the initial click
//                 int xMoved = e.getX() - initialClick.x;
//                 int yMoved = e.getY() - initialClick.y;

//                 // Move window to this position
//                 int X = thisX + xMoved;
//                 int Y = thisY + yMoved;
//                 btn.setLocation(X, Y);
//             }
//         }; 
//     }   

}