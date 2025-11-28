package bunlisugo.client.controller;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bunlisugo.client.model.TrashType;
import bunlisugo.client.view.game.TimePanel;
import bunlisugo.client.view.game.TrashBoxPanel;

public class GameController {

    private TimePanel timePanel;
    private TrashBoxPanel trashBoxPanel;
    private JFrame frame;

    private javax.swing.Timer spawnTimer;
    private int score = 0;

    private Random random = new Random();
    private List<JButton> trashButtons = new ArrayList<>();

    public void setTimePanel(TimePanel timePanel) {
        this.timePanel = timePanel;
    }

    public void setTrashBoxPanel(TrashBoxPanel trashBoxPanel) {
        this.trashBoxPanel = trashBoxPanel;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /** 게임 시작할 때 GameView에서 호출 */
    public void startGame() {
        if (timePanel != null) {
            timePanel.startTimer(60); // 60초 게임
        }

        // 1.5초마다 쓰레기 하나 생성
        spawnTimer = new javax.swing.Timer(1500, e -> spawnRandomTrash());
        spawnTimer.start();
    }

    /** 쓰레기 하나 랜덤으로 생성해서 프레임에 추가 */
    private void spawnRandomTrash() {
        if (frame == null || trashBoxPanel == null) return;

        // 랜덤 타입
        TrashType[] types = TrashType.values();
        TrashType type = types[random.nextInt(types.length)];

        // 아이콘 경로 (네 이미지 구조에 맞게 수정)
        String imagePath = switch (type) {
            case PAPER -> "/images/trash/paper/paper1.png";
            case PLASTIC -> "/images/trash/plastic/plastic1.png";
            case GLASSCAN -> "/images/trash/glasscan/can1.png";
            case GENERAL -> "/images/trash/general/general1.png";
        };

        java.net.URL imgUrl = getClass().getResource(imagePath);
        if (imgUrl == null) {
            System.out.println("이미지 못 찾음: " + imagePath);
            return;
        }

        ImageIcon icon = new ImageIcon(imgUrl);
        JButton trashBtn = new JButton(icon);
        trashBtn.setBorderPainted(false);
        trashBtn.setContentAreaFilled(false);

        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        // 프레임 안에서 랜덤 위치 (윗부분)
        int maxX = frame.getWidth() - width - 50;
        int maxY = 300; // 쓰레기통 위쪽까지만
        int x = 50 + random.nextInt(Math.max(maxX, 50));
        int y = 100 + random.nextInt(Math.max(maxY, 50));

        trashBtn.setBounds(x, y, width, height);

        // 드래그 & 드롭
        addDragAndDrop(trashBtn, type);

        trashButtons.add(trashBtn);
        frame.getContentPane().add(trashBtn);
        frame.repaint();
    }

    /** 드래그해서 놓았을 때 점수 판정 */
    private void addDragAndDrop(JButton btn, TrashType type) {
        MouseAdapter ma = new MouseAdapter() {
            Point initialClick;

            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = btn.getLocation().x;
                int thisY = btn.getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                btn.setLocation(X, Y);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                checkDrop(btn, type);
            }
        };

        btn.addMouseListener(ma);
        btn.addMouseMotionListener(ma);
    }

    /** 어떤 통 위에 놓였는지 + 타입 맞는지 체크 */
    private void checkDrop(JButton btn, TrashType type) {
        JPanel[] boxes = trashBoxPanel.getBoxes();

        Rectangle btnBounds = btn.getBounds();

        for (int i = 0; i < boxes.length; i++) {
            Rectangle boxBounds = SwingUtilities.convertRectangle(
                    btn.getParent(), btnBounds, trashBoxPanel);

            Rectangle target = boxes[i].getBounds();

            if (boxBounds.intersects(target)) {
                // 이 박스의 정답 타입
                TrashType boxType = trashBoxPanel.getBoxType(i);
                if (boxType == type) {
                    score += 10;
                    System.out.println("정답! 현재 점수: " + score);
                } else {
                    System.out.println("오답! 현재 점수: " + score);
                }
                btn.setVisible(false);
                trashButtons.remove(btn);
                frame.getContentPane().remove(btn);
                frame.repaint();
                return;
            }
        }
        // 아무 박스에도 안 떨어졌으면 그냥 놔두기
    }
}
