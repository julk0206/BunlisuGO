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
import javax.swing.Timer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import bunlisugo.client.GameClient;
import bunlisugo.client.model.GameState;
import bunlisugo.client.model.TrashType;
import bunlisugo.client.view.ResultView;
import bunlisugo.client.view.game.GameScorePanel;
import bunlisugo.client.view.game.TimePanel;
import bunlisugo.client.view.game.TrashBoxPanel;

public class GameController {

    private TimePanel timePanel;
    private TrashBoxPanel trashBoxPanel;
    private JFrame frame;

    // 1.5초 간격 쓰레기 스폰용 타이머
    private Timer spawnTimer;
    private final int spawnIntervalMs = 1500;   // 1.5초
    private final int maxTrashCount   = 40;     // 60초 동안 1.5초마다 → 40개
    private int spawnedCount = 0;

    // 점수
    private int score = 0;
    private final int correct_score = 5;
    private final int wrong_score   = 2;

    private Random random = new Random();
    private List<JButton> trashButtons = new ArrayList<JButton>();
    private GameClient client;

    private GameScorePanel gameScorePanel;
    private GameState gameState;

    // 게임이 이미 끝났는지 여부(중복 종료 방지)
    private boolean gameEnded = false;

    public GameController(GameClient client) {
        this.client = client;
    }

    public GameController() {}

    public void setTimePanel(TimePanel timePanel) {
        this.timePanel = timePanel;
    }

    public void setTrashBoxPanel(TrashBoxPanel trashBoxPanel) {
        this.trashBoxPanel = trashBoxPanel;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    public void setGameScorePanel(GameScorePanel panel) {
        this.gameScorePanel = panel;
    }

    public void startGame() {
        gameEnded = false;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    // 60초 끝나면 TimePanel에서 호출
    public void onTimeOver() {
        if (gameEnded) return; // 이미 끝났으면 무시

        if (spawnTimer != null && spawnTimer.isRunning()) {
            spawnTimer.stop();
        }

        gameOver();
    }


    // 쓰레기 하나 랜덤으로 생성해서 프레임에 추가
    private void spawnTrash(String name, String category,String imagePath, int x, int y) {
        if (frame == null || trashBoxPanel == null || gameEnded) return;

        TrashType type = TrashType.valueOf(category.toUpperCase()); // 서버에서 보내준 category 활용

        java.net.URL imgUrl = getClass().getResource(imagePath);
        if (imgUrl == null) {
            System.out.println("이미지 못 찾음: " + imagePath);
            return;
        }

        // 원본 아이콘 -> 120x120으로 스케일링
        ImageIcon originalIcon = new ImageIcon(imgUrl);
        int width  = 120;
        int height = 120;
        java.awt.Image scaled = originalIcon.getImage()
                .getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaled);

        JButton trashBtn = new JButton(icon);
        trashBtn.setBorderPainted(false);
        trashBtn.setContentAreaFilled(false);

        trashBtn.setBounds(x, y, width, height);

        // 드래그 & 드롭
        addDragAndDrop(trashBtn, type);

        trashButtons.add(trashBtn);
        frame.getContentPane().add(trashBtn);
        frame.repaint();
    }

    // 드래그해서 놓았을 때 점수 판정
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

    // 어떤 통 위에 놓였는지 + 타입 맞는지 체크
    private void checkDrop(JButton btn, TrashType type) {
        if (gameEnded) return;  // ★ 게임 끝났으면 판정 안 함

        JPanel[] boxes = trashBoxPanel.getBoxes();
        if (boxes == null) return;

        Rectangle btnBounds = btn.getBounds();

        for (int i = 0; i < boxes.length; i++) {
            Rectangle boxBounds = SwingUtilities.convertRectangle(
                    btn.getParent(), btnBounds, trashBoxPanel);

            Rectangle target = boxes[i].getBounds();

            if (boxBounds.intersects(target)) {
                TrashType boxType = trashBoxPanel.getBoxType(i);
                boolean correct = (boxType == type);

                if (correct) {
                    score += correct_score;
                } else {
                    score -= wrong_score;
                    if (score < 0) {
                        score = 0;
                    }
                }

                System.out.println(
                    "판정 → trash type=" + type +
                    ", trashbox type=" + boxType +
                    ", " + (correct ? "정답" : "오답") +
                    ", 현재 점수=" + score
                );

                client.send("SCORE|" + score);
                gameScorePanel.updateMyScore(score);

                btn.setVisible(false);
                trashButtons.remove(btn);
                frame.getContentPane().remove(btn);
                frame.repaint();

                // 더 이상 쓰레기가 없고, 스폰도 안 돌고 있으면 종료
                if (trashButtons.isEmpty() &&
                    (spawnTimer == null || !spawnTimer.isRunning())) {
                    gameOver();
                }
                return;
            }
        }
        // 아무 박스에도 안 떨어졌으면 그냥 놔두기
    }

    // 게임 오버
    public void gameOver() {
        if (gameEnded) return;  // 중복 호출 방지
        gameEnded = true;

        if (spawnTimer != null && spawnTimer.isRunning()) {
            spawnTimer.stop();
        }

        // 남아있는 쓰레기 버튼 전부 비활성화
        for (JButton btn : new ArrayList<JButton>(trashButtons)) {
            btn.setEnabled(false);
        }

        if (client != null) {
            client.send("GAME_RESULT|" + score);
        }

        System.out.println("게임 종료! 최종 점수 = " + score);
    }

    public void showResult(String winnerId) {
        // 모델에서 점수와 이름 가져오기
        String myName = gameState.getMyName();
        String opponentName = gameState.getOpponentName();
        int myScore = gameState.getMyScore();
        int opponentScore = gameState.getOpponentScore();

        // 승패 텍스트 결정
        String resultText;
        if (winnerId.equals(myName)) {
            resultText = "YOU WIN!";
        } else if (winnerId.equals(opponentName)) {
            resultText = "YOU LOSE!";
        } else {
            resultText = "DRAW!";
        }

        // Swing UI 업데이트 (ResultView 생성)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new ResultView(client, resultText, myScore, opponentScore);
        });
    }

}
