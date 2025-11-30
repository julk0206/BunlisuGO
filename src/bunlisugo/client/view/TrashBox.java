package bunlisugo.client.view;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TrashBox {

    private JPanel[] boxes = new JPanel[4];
    private String[] names = {"일반쓰레기", "플라스틱", "캔/유리", "종이"};
    private final String trashBoxImagePath = "/images/trashbox.png";

    public TrashBox() {
        makeTrashBox();
    }

    public void makeTrashBox() {

        int boxWidth = 165;
        int boxHeight = 185;
        int startX = 200;
        int gap = 100;

        ImageIcon trashboxImage = null;
        java.net.URL imgUrl = getClass().getResource(trashBoxImagePath);
        if (imgUrl != null) {
            trashboxImage = new ImageIcon(imgUrl);
        } else {
            System.out.println("이미지 파일을 찾을 수 없습니다: " + trashBoxImagePath);
        }

        for (int i = 0; i < 4; i++) {
            JPanel box = new JPanel();
            box.setLayout(null);
            box.setBounds(startX + i * (boxWidth + gap), 600, boxWidth, boxHeight);

            // 이미지
            if (trashboxImage != null) {
                JLabel imageLabel = new JLabel(trashboxImage);
                imageLabel.setBounds(0, 0, boxWidth, boxHeight);
                box.add(imageLabel);
            }

            // 이름 라벨
            JLabel nameLabel = new JLabel(names[i], JLabel.CENTER);
            nameLabel.setBounds(0, boxHeight - 30, boxWidth, 30);
            box.add(nameLabel);

            box.setName(names[i]);
            boxes[i] = box;
        }
    }

    public JPanel[] getBoxes() {
        return boxes;
    }
}
