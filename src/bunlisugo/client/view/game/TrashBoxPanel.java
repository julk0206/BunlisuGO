package bunlisugo.client.view.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bunlisugo.client.model.TrashType;

public class TrashBoxPanel extends JPanel {

    private JPanel[] boxes = new JPanel[4];

    // 0~3번 통의 타입 (게임 판정용)
    private TrashType[] boxTypes = {
        TrashType.GENERAL,   // 0
        TrashType.PLASTIC,   // 1
        TrashType.GLASSCAN,  // 2
        TrashType.PAPER      // 3
    };

    // 화면에 보여줄 이름
    private String[] names = { "일반쓰레기", "플라스틱", "캔/유리", "종이" };

    public TrashBoxPanel() {
        setLayout(null);
        // GameView에서 써왔던 위치 그대로
        setBounds(120, 484, 756, 217);
        makeTrashBox();
    }

    private void makeTrashBox() {

        int boxWidth  = 189;
        int boxHeight = 217;

        ImageIcon trashboxImage = null;
        java.net.URL imgUrl = getClass().getResource("/images/trashbox.png");
        if (imgUrl != null) {
            trashboxImage = new ImageIcon(imgUrl);
        } else {
            System.out.println("이미지 파일을 찾을 수 없습니다: /images/trashbox.png");
        }

        for (int i = 0; i < 4; i++) {

            JPanel box = new JPanel();
            box.setLayout(null);
            box.setBounds(i * boxWidth, 0, boxWidth, boxHeight);

            // 1) 이름 라벨 (맨 위, 아주 튀게)
            JLabel nameLabel = new JLabel(names[i], JLabel.CENTER);
            nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            nameLabel.setForeground(Color.BLACK);
            nameLabel.setOpaque(true);
            nameLabel.setBackground(Color.YELLOW);   // 확실하게 보이게
            nameLabel.setBounds(0, 0, boxWidth, 25); // 맨 위 25px

            box.add(nameLabel);

            // 2) 그 아래에 쓰레기통 이미지
            if (trashboxImage != null) {
                JLabel imgLabel = new JLabel(trashboxImage);
                // 라벨 바로 아래부터 그림
                imgLabel.setBounds(0, 25, boxWidth, boxHeight - 25);
                box.add(imgLabel);
            } else {
                box.setBackground(Color.GRAY);
            }

            boxes[i] = box;
            add(box);
        }
    }

    // GameController에서 판정할 때 사용
    public JPanel[] getBoxes() {
        return boxes;
    }

    public TrashType getBoxType(int index) {
        return boxTypes[index];
    }

    public Rectangle getTrashBoxBounds() {
        return this.getBounds();
    }
}
