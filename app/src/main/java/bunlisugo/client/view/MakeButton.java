package bunlisugo.client.view;

import java.util.HashMap;
import java.util.Map;

//지워도 되는 거 같음
public class MakeButton {
	 Map<String, Integer> answer = new HashMap<>();
	 public  String[] TrashImage = {
			 "images/PlasticBottle.png", 
			 "images/PlasticCup.png", 
			 "images/bottle.png", 
			 "images/OMR.png"
	 };
	 
	 public MakeButton() {
			answer.put("페트병", 1);
			answer.put("테이크아웃잔", 1);
			answer.put("유리병", 2);
			answer.put("OMR 종이", 0);
	 }

	public void addTrash(String text, int CorrectBox) {
		//btn.setBounds((int) (100*(Math.random())), (int) (100*(Math.random())), 100, 200);
		answer.put(text, CorrectBox);
	
	}
	public void setTrashImage(String[] text) {
		this.TrashImage = text;
	}
	
	
	
	
}

