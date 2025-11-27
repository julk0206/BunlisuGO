package bunlisugo.client.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.TextArea;

public class Window {

	private JFrame frame =new JFrame();
	private JPanel[] boxes = new JPanel[4];
	//private String[] TrashImage;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		}

	public Window() {
		initialize();
		
		MakeButton m = new MakeButton();
		//Set<String> trashes = (Set<String>) m.answer.keySet();
		AreaCheck ac = new AreaCheck();
		ac.setBoxes(boxes);
		ac.setanswer(m.answer);
		//String[] TrashImage = m.setTrashImage(TrashImage);
		
		
		JLabel scoreLabel = new JLabel("점수: 0");
		scoreLabel.setBounds(50,50,200,50);
		frame.getContentPane().add(scoreLabel);
		ac.setScoreLabel(scoreLabel);
		Button btnFactory = new Button();
		
		for(String text: m.answer.keySet()) {
			//button btn = new button();
			for(String path: m.TrashImage) {
				JButton b = btnFactory.getbutton(text, ac, path);
			
			frame.getContentPane().add(b);
			frame.getContentPane().setComponentZOrder(b, 0);
			break;
			}
			
		}
		}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame.setBounds(100, 100, 1281, 843);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		TrashBox makebox = new TrashBox();
		boxes = makebox.getBoxes();
		
		
		for(int i = 0; i<boxes.length; i++) {
			if(boxes[i] != null) {
				frame.getContentPane().add(boxes[i]);
				
			}
			else {System.out.println("null입니다잉");
			
		}
		
	}
}
}
