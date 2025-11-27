package bunlisugo.client.view.game;

import java.awt.Color;
import java.awt.Rectangle;

import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TrashBoxPanel extends JPanel {
    private JPanel[] boxes = new JPanel[4];

    //좀 나눠야 되나..? (생성자 메소드를)
    //model에다가 저 box 만들고나서, TrashBoxPanel에다가 가져와야 되나? 
    public TrashBoxPanel() {
        setLayout(null);
        setBounds(120,484,756,217);
        makeTrashBox();
    }

    private void makeTrashBox(){

        int boxWidth = 189;
		int boxHeight = 217;
		//int startX = 80;
		int gap = 0;

        ImageIcon trashboximage = null;
        java.net.URL imgUrl = getClass().getResource("/images/교동이.png");
        if (imgUrl != null) {
            trashboximage = new ImageIcon(imgUrl);
        } else {
            System.out.println("이미지 파일을 찾을 수 없습니다: /images/교동이.png");
        }

        for (int i =0; i<4; i ++){
            JPanel box = new JPanel();
            box.setBounds(i*(boxWidth+gap), 0 , boxWidth, boxHeight); 
            //이렇게 하면 부모패널(trashbox)기준으로 위치잡히는 거 맞아?
            box.setLayout(null); //이 순서 맞아? 
            
            if (trashboximage != null) {
                JLabel trashBoxImageLabel = new JLabel(trashboximage);
                trashBoxImageLabel.setBounds(0, 0, boxWidth, boxHeight);
                box.add(trashBoxImageLabel);
            } else {
                // 이미지가 없으면 빨간색 칠해서 "위치"라도 확인하기
                box.setBackground(Color.RED); 
                JLabel errorLabel = new JLabel("이미지 없음");
                errorLabel.setBounds(0,0,100,20);
                box.add(errorLabel);
            }

            boxes[i] = box;
            add(box);
        }  
    }

    public Rectangle getTrashBoxBounds(){
        return this.getBounds();
    }


}