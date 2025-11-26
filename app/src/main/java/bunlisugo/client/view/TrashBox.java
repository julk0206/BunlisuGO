package bunlisugo.client.view;


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TrashBox{
	
	private JPanel[] boxes = new JPanel[4];
	private String[] names = {"일반쓰레기", "플라스틱", "캔/유리", "종이"};
	private String TrashBoxImage = "images/trashbox.png";
	
	public TrashBox() {
		
		makeTrashBox();//맨 마지막줄에 하는게 맞나 아님 다른 게 맞나?
		/*
		boxes[0].setName("일반쓰레기");
		boxes[1].setName("플라스틱");
		boxes[2].setName("캔/유리");
		boxes[3].setName("종이");
		boxes[0].setBackground(Color.RED);
		boxes[1].setBackground(Color.orange);
		boxes[2].setBackground(Color.yellow);
		boxes[3].setBackground(Color.PINK);
		 */
		
			}

	public void makeTrashBox() {
		
		int boxWidth = 165;
		int boxHeight = 185;
		int startX = 200;
		int gap = 100;
		
		
		
		
		for (int i =0; i<4; i ++){
			JPanel box = new JPanel();
			box.setBounds(startX + i*(boxWidth+gap), 600 , boxWidth, boxHeight);
			box.setLayout(null);
			box.setName(names[i]);
			JLabel nameLabel = new JLabel(names[i]);
			nameLabel.setBounds(0, 10 , boxWidth, boxHeight);
			box.add(nameLabel);
			
			ImageIcon trashboximage = new ImageIcon(TrashBoxImage);
			JLabel TrashBoxImageLabel = new JLabel(trashboximage);
			TrashBoxImageLabel.setBounds(0, 0 , boxWidth, boxHeight);
			box.add(TrashBoxImageLabel);
			
			boxes[i] = box;
		}
	}
	

	public JPanel[] getBoxes() {
		return boxes;
	}
}

