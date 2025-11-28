package bunlisugo.client.controller;

import java.awt.Rectangle;
import java.awt.Image;
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

    private int score = 0;
    private int trashNumber = 10;

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

    // 게임 시작할 때 GameView에서 호출
    public void startGame() {
        if (timePanel != null) {
            timePanel.startTimer(60); // 60초 게임
        }

        // 생성할 쓰레기 수를 필드에서 정해줌
        for (int i = 0; i < trashNumber; i++) {
            spawnRandomTrash();
        }
    }
    
    //이미지 경로 가져옴
    private String getRandomImagePath(TrashType type) {
        String[] candidates = null;

        switch (type) {
            case PLASTIC -> {
                candidates = new String[] {
                    "/images/trash/plastic/delivery_clean.png",
                    "/images/trash/plastic/PlasticBottle.png",
                    "/images/trash/plastic/PlasticCup.png"
                };
            }
            case GLASSCAN -> {
                candidates = new String[] {
                    "/images/trash/glasscan/aluminumcan.png",
                    "/images/trash/glasscan/beer.png",
                    "/images/trash/glasscan/soju.png"
                };
            }
            case PAPER -> {
                candidates = new String[] {
                    "/images/trash/paper/newspaper.png",
                    "/images/trash/paper/postit.png"
                };
            }
            case GENERAL -> {
                candidates = new String[] {
                    "/images/trash/general/brokenglass.png",
                    "/images/trash/general/ceramic.png",
                    "/images/trash/general/delivery_dirty.png",
                    "/images/trash/general/fruitnet.png",
                    "/images/trash/general/receipt.png",
                    "/images/trash/general/toothpaste.png"
                };
            }
           
        }

        return candidates[random.nextInt(candidates.length)];
    }


    //쓰레기 하나 랜덤으로 생성해서 프레임에 추가
    private void spawnRandomTrash() {
        if (frame == null || trashBoxPanel == null) return;

        // 랜덤 타입
        TrashType[] types = TrashType.values();
        TrashType type = types[random.nextInt(types.length)];

        // 아이콘 경로
        String imagePath = getRandomImagePath(type);

        java.net.URL imgUrl = getClass().getResource(imagePath);
        if (imgUrl == null) {
            System.out.println("이미지 못 찾음: " + imagePath);
            return;
        }

        // 🔥 원본 아이콘
        ImageIcon originalIcon = new ImageIcon(imgUrl);

        // 아이콘 크기 지정
        int width = 120;
        int height = 120;

        // 아이콘 크기를 120 * 120으로 맞춤
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledImage);

        JButton trashBtn = new JButton(icon);
        trashBtn.setBorderPainted(false);
        trashBtn.setContentAreaFilled(false);
        trashBtn.setFocusPainted(false);

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


    //드래그해서 놓았을 때 점수 판정
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

    //어떤 통 위에 놓였는지 + 타입 맞는지 체크
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
