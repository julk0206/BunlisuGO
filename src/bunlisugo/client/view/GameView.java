package bunlisugo.client.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameView {
    private JFrame frame;
    
    public GameView() {
        frame = new JFrame("Game View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setVisible(true);
        initialize();
    }

    private void initialize() {
        JLabel lblNewLabel_1 = new JLabel("내 점수:");
		lblNewLabel_1.setBackground(Color.BLUE);
		lblNewLabel_1.setBounds(71, 34, 166, 53);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2= new JLabel("상대방 점수:");
		lblNewLabel_2.setBackground(Color.RED);
		lblNewLabel_2.setBounds(938, 34, 166, 53);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel = new JLabel("시간");
		lblNewLabel.setBounds(375, 21, 418, 31);
		frame.getContentPane().add(lblNewLabel);
    
    }
}