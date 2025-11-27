package bunlisugo.client.view;

import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AreaCheck {
	
	int score =0;
	private JPanel[] boxes;
	Map<String, Integer> answer;

	private JLabel scoreLabel;
	
	public void setBoxes(JPanel[] boxes) {
		this.boxes = boxes;
	}
	
	public void setanswer(Map<String, Integer> answer) {
		this.answer = answer;
	}
	
	public void setScoreLabel(JLabel label) {
		this.scoreLabel = label;
	}
	
	public void Check(JButton b) {
		int value;
		//Rectangle area = b.getBounds();
		
		int centerX = b.getX()+ b.getWidth()/2;
		int centerY = b.getY()+ b.getHeight()/2;
		
		value = -1;
		for(int i = 0; i<boxes.length; i ++) {
			if( boxes[i].getBounds().contains(centerX, centerY)) {
				value = i;
				b.setVisible(false);
				break;
			}
		}
		
		if(value ==-1) {
			return;
		}
		
		String trashName = b.getText();
		
		
		if(answer.containsKey(trashName)) {
			int correctAnswer = answer.get(trashName);
			
			if(value == correctAnswer) {
				score +=2;
			}else {
				score -=1;
			}
			
		}

		if(scoreLabel != null) {
			scoreLabel.setText("점수는" + score);
		}
		
		b.setVisible(false);
		
	}
	
	
	
	
	
}
