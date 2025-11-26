package bunlisugo.client.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bunlisugo.client.controller.GameController;
import bunlisugo.server.model.Trash;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameView {
    private JFrame frame;
    private JPanel[] boxes = new JPanel[4];

    //private GameController  gameController;
   
    
    public GameView() {
        frame = new JFrame("Game View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        initialize();
        showTrashes();
        frame.setVisible(true);
    }
    
    private void initialize() {
        JLabel myScoreLabel = new JLabel("내 점수:");
        myScoreLabel.setOpaque(true);
		myScoreLabel.setBackground(Color.DARK_GRAY);
		myScoreLabel.setBounds(71, 34, 166, 53);
		frame.getContentPane().add(myScoreLabel);
		
		JLabel opponentScoreLabel= new JLabel("상대방 점수:");
        opponentScoreLabel.setOpaque(true);
		opponentScoreLabel.setBackground(Color.DARK_GRAY);
		opponentScoreLabel.setBounds(938, 34, 166, 53);
		frame.getContentPane().add(opponentScoreLabel);
		
		JLabel TimeLabel = new JLabel("시간");
		TimeLabel.setBounds(375, 21, 418, 31);
		frame.getContentPane().add(TimeLabel);

        makeTrashBox();
        }
    
    private void makeTrashBox() {   
        
        int boxWidth = 189;
		int boxHeight = 217;
		int startX = 80;
		int gap = 90;

        
        for (int i =0; i<4; i ++){
            JPanel box = new JPanel();
            box.setBounds(startX + i*(boxWidth+gap), 484 , boxWidth, boxHeight);
            box.setLayout(null);
            //box.setOpaque(true);
            //box.setBackground(Color.PINK);
            ImageIcon trashboximage = new ImageIcon(getClass().getResource("/images/교동이.png"));
            JLabel TrashBoxImageLabel = new JLabel(trashboximage);
            TrashBoxImageLabel.setBounds(0, 0 , boxWidth, boxHeight);
            box.add(TrashBoxImageLabel);
            
            boxes[i] = box;
            frame.getContentPane().add(box);
        }

        boxes[0].setName("GENERAL");
        boxes[1].setName("PLASTIC");           
        boxes[2].setName("GLASSCAN");
        boxes[3].setName("PAPER");   

    }



    private void showTrashes() {
        try {
            List<Trash> trashes = new ArrayList<>(); 
            GameController gameController = new GameController();       
            trashes = gameController.getRandomTrashes(10);
            
            for (Trash t : trashes) {
                //System.out.println("Trash: " + trash.getName() + ", Type: " + trash.getType());
               JButton btn = new JButton(t.getName());
               btn.setIcon(new ImageIcon(t.getImagePath()));
               btn.setBounds((int)(100*(Math.random())), (int) (100*(Math.random())), 150, 150);
               //moveButton(btn);
               System.out.println("path " + t.getImagePath());
               //System.out.println("exits"+ new.java.io.File(t.getImagePath()).exists());
               frame.getContentPane().add(btn); 
               
            }

            frame.revalidate();
            frame.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveButton(JButton btn) {
        // TODO Auto-generated method stub
        MouseAdapter ma = new MouseAdapter() {
            Point initialClick;

            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                btn.getComponentAt(initialClick);
            }

            public void mouseDragged(MouseEvent e) {
                // get location of Window
                int thisX = btn.getLocation().x;
                int thisY = btn.getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                btn.setLocation(X, Y);
            }
        }; 
    }   




}