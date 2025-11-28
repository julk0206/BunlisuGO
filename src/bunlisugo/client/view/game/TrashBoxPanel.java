package bunlisugo.client.view.game;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bunlisugo.client.model.TrashType;

public class TrashBoxPanel extends JPanel {

    private JPanel[] boxes = new JPanel[4];
    private TrashType[] boxTypes = {
        TrashType.GENERAL,   // 0번 박스
        TrashType.GLASSCAN,  // 1번 박스
        TrashType.PAPER,     // 2번 박스
        TrashType.PLASTIC    // 3번 박스
    };

    public TrashBoxPanel() {
        setLayout(null);
        setBounds(120, 484, 756, 217);
        makeTrashBox();
    }

    private void makeTrashBox() {
        int boxWidth = 189;
        int boxHeight = 217;
        int gap = 0;

        ImageIcon trashboximage = null;
        java.net.URL imgUrl = getClass().getResource("/images/trashbox.png");
        if (imgUrl != null) {
            trashboximage = new ImageIcon(imgUrl);
        } else {
            System.out.println("이미지 파일을 찾을 수 없습니다: /images/trashbox.png");
        }

        for (int i = 0; i < 4; i++) {
            JPanel box = new JPanel();
            box.setBounds(i * (boxWidth + gap), 0, boxWidth, boxHeight);
            box.setLayout(null);

            if (trashboximage != null) {
                JLabel trashBoxImageLabel = new JLabel(trashboximage);
                trashBoxImageLabel.setBounds(0, 0, boxWidth, boxHeight);
                box.add(trashBoxImageLabel);
            } else {
                box.setBackground(Color.RED);
                JLabel errorLabel = new JLabel("이미지 없음");
                errorLabel.setBounds(0, 0, 100, 20);
                box.add(errorLabel);
            }

            boxes[i] = box;
            add(box);
        }
    }

    public Rectangle getTrashBoxBounds() {
        return this.getBounds();
    }

    // 🔥 GameController에서 판정할 때 씀
    public JPanel[] getBoxes() {
        return boxes;
    }

    public TrashType getBoxType(int index) {
        return boxTypes[index];
    }

    
}
