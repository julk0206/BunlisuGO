package bunlisugo.client.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import bunlisugo.client.GameClient;
import bunlisugo.client.model.GameState;
import bunlisugo.client.model.TrashType;
import bunlisugo.client.view.ResultView;
import bunlisugo.client.view.game.GameScorePanel;
import bunlisugo.client.view.game.TimePanel;
import bunlisugo.client.view.game.TrashBoxPanel;

public class GameController {

    private TrashBoxPanel trashBoxPanel;
    private JFrame gameFrame;        

    // 필요하면 쓸 스폰 타이머
    private Timer spawnTimer;

    // 점수
    private int score = 0;
    private final int correct_score = 5;
    private final int wrong_score   = 2;

    private List<JButton> trashButtons = new ArrayList<>();
    private GameClient client;

    private GameScorePanel gameScorePanel;
    private GameState gameState;

    // 게임이 이미 끝났는지 여부(중복 종료 방지)
    private boolean gameEnded = false;

    // 결과 화면을 이미 띄웠는지 여부
    private boolean resultShown = false;

    // 외부에서 주입받을 playerId (로그인한 유저 이름)
    private String playerId;
    private TimePanel timePanel;

    public GameController() {}

    // client만 넘길 때
    public GameController(GameClient client) {
        this.client = client;
        if (client != null && client.getCurrentUser() != null) {
            this.playerId = client.getCurrentUser().getUsername();
        }
    }
    
    public GameController(GameClient client, String playerId) {
        this.client = client;
        this.playerId = playerId;
    }


    // client + gameFrame 까지 한 번에 넘기는 생성자
    public GameController(GameClient client, JFrame gameFrame) {
        this(client);
        this.gameFrame = gameFrame;
    }

    public void setTimePanel(TimePanel timePanel) {
        this.timePanel = timePanel;
    }

    public void setTrashBoxPanel(TrashBoxPanel trashBoxPanel) {
        this.trashBoxPanel = trashBoxPanel;
    }

    public void setGameFrame(JFrame frame) {
        this.gameFrame = frame;
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    public void setGameScorePanel(GameScorePanel panel) {
        this.gameScorePanel = panel;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public void startGame() {
        gameEnded = false;
        resultShown = false;
        score = 0;  // 내 점수 리셋

        // 모델도 같이 리셋
        if (gameState != null) {
            gameState.setMyScore(0);
            gameState.setOpponentScore(0);
        }

        // 화면 점수판도 리셋
        if (gameScorePanel != null) {
            gameScorePanel.updateMyScore(0);
            gameScorePanel.updateOpponentScore(0);
        }
    }

    // 60초 끝나면 GameClient에서 TIME_UPDATE=0을 받고 여기 onTimeOver 호출
    public void onTimeOver() {
        if (gameEnded) return; // 이미 끝났으면 무시

        if (spawnTimer != null && spawnTimer.isRunning()) {
            spawnTimer.stop();
        }

        gameOver();
    }

    // 서버에서 온 쓰레기 하나를 화면에 추가
    public void spawnTrash(String name, String category, String imagePath, int x, int y) {
        if (gameFrame == null || trashBoxPanel == null || gameEnded) return;

        // 쓰레기통 위에 겹치지 않게 Y 최소값 제한 (필요하면 수치 조정)
        int minY = 250;
        if (y < minY) {
            y = minY;
        }

        TrashType type = TrashType.valueOf(category.toUpperCase()); // 서버에서 보내준 category 활용

        java.net.URL imgUrl = getClass().getResource("/" + imagePath);
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
        gameFrame.getContentPane().add(trashBtn);
        gameFrame.repaint();
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
        if (gameEnded) return;  // 게임 끝났으면 판정 안 함

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

                if (gameState != null) {
                    gameState.setMyScore(score);
                }

                // 외부 주입된 playerId 사용 (없으면 client에서 한 번 더 가져오기)
                String pid = playerId;
                if (pid == null && client != null && client.getCurrentUser() != null) {
                    pid = client.getCurrentUser().getUsername();
                }

                if (client != null && pid != null) {
                    client.send("SCORE|" + pid + "|" + score);
                }

                if (gameScorePanel != null) {
                    gameScorePanel.updateMyScore(score);
                }

                btn.setVisible(false);
                trashButtons.remove(btn);
                if (gameFrame != null) {
                    gameFrame.getContentPane().remove(btn);
                    gameFrame.repaint();
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
        for (JButton btn : new ArrayList<>(trashButtons)) {
            btn.setEnabled(false);
        }

        if (client != null) {
            // 서버 ResultCommandHandler와 맞춘 명령 이름
            client.send("RESULT|" + score);
        }

        System.out.println("게임 종료! 최종 점수 = " + score);
    }

    public void showResult(String winnerId) {
        if (gameState == null) return;

        String myName   = gameState.getMyName();
        String oppName  = gameState.getOpponentName();
        int myScore     = gameState.getMyScore();
        int oppScore    = gameState.getOpponentScore();

        String resultText;
        if (winnerId == null || "null".equalsIgnoreCase(winnerId)) {
            resultText = "DRAW!";
        } else if (winnerId.equals(myName)) {
            resultText = "YOU WIN!";
        } else if (winnerId.equals(oppName)) {
            resultText = "YOU LOSE!";
        } else {
            resultText = winnerId;
        }

        System.out.println("[showResult] winner=" + winnerId +
                ", myName=" + myName +
                ", oppName=" + oppName +
                ", myScore=" + myScore +
                ", oppScore=" + oppScore);

        javax.swing.SwingUtilities.invokeLater(() -> {
            // 게임 창 닫기
            if (gameFrame != null) {
                gameFrame.dispose();
            }
            new ResultView(client, resultText, myScore, oppScore);
        });
    }

}
