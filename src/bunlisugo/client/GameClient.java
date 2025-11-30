package bunlisugo.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bunlisugo.client.controller.GameController;
import bunlisugo.client.model.User;
import bunlisugo.client.model.GameState;
import bunlisugo.client.view.HomeView;
import bunlisugo.client.view.LoginView;
import bunlisugo.client.view.MatchingView;
import bunlisugo.client.view.RankingView;
import bunlisugo.client.view.game.GameScorePanel;
import bunlisugo.client.view.game.TimePanel;

public class GameClient {

    private static GameClient instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private User currentUser;

    // 랭킹에서 쓸 마지막 점수
    private int lastScore = 0;

    public static final int RANKING_LIMIT = 10;

    // 화면 참조
    private LoginView loginView;
    private HomeView homeView;
    private MatchingView matchingView;
    private RankingView rankingView;

    private GameScorePanel gameScorePanel;
    private TimePanel timePanel;
    private List<String> rankList = new ArrayList<>();

    // ★ GameController는 여기서 new 하지 않고, HomeView에서 주입
    private GameController gameController;

    // 게임 중에만 사용하는 상태
    private GameState gameState;

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    public void setMatchingView(MatchingView matchingView) {
        this.matchingView = matchingView;
    }

    public void setRankingView(RankingView rankingView) {
        this.rankingView = rankingView;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public int getLastScore() {
        return lastScore;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }

    public String getNickname() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    public void setGameScorePanel(GameScorePanel panel) {
        this.gameScorePanel = panel;
    }

    public void setTimePanel(TimePanel panel) {
        this.timePanel = panel;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        if (this.gameState != null && this.gameController != null) {
            this.gameController.setGameState(this.gameState);
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        if (this.gameController != null && this.gameState != null) {
            this.gameController.setGameState(this.gameState);
        }
    }

    private GameClient() {
        try {
            socket = new Socket("10.240.61.209", 3328);
            System.out.println("Connected to server..");

            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(() -> {
                try {
                    String resp;
                    while ((resp = in.readLine()) != null) {
                        System.out.println("RECV: " + resp);
                        handleServerMessage(resp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GameClient getInstance() {
        if (instance == null) {
            instance = new GameClient();
        }
        return instance;
    }

    public void send(String message) {
        if (out != null) {
            out.println(message);
            out.flush();
        }
    }

    private void handleServerMessage(String line) {
        String[] parts = line.split("\\|");
        String cmd = parts[0];

        switch (cmd) {

            case "LOGIN_OK":
                if (loginView != null) {
                    String username = parts.length > 1 ? parts[1] : "";
                    currentUser = new User();
                    currentUser.setUsername(username);
                    loginView.onLoginSuccess(username);
                }
                break;

            case "LOGIN_FAIL":
                if (loginView != null) {
                    String reason = (parts.length > 1) ? parts[1] : "알 수 없는 오류";
                    loginView.onLoginFail(reason);
                }
                break;

            case "MATCH_WAITING":
                int waiting = Integer.parseInt(parts[1]);
                if (matchingView != null) {
                    matchingView.onMatchWaiting(waiting);
                }
                break;

            case "MATCH_FOUND":
                String opponentName = parts[1];

                if (currentUser != null) {
                    GameState state = new GameState(currentUser.getUsername(), opponentName);
                    setGameState(state);
                }

                if (matchingView != null) {
                    matchingView.onMatchFound(opponentName);
                }
                break;

            case "SCORE_UPDATE":
                String playerId = parts[1];
                int score = Integer.parseInt(parts[2]);

                String myName = (currentUser != null) ? currentUser.getUsername() : null;

                // 점수판 업데이트
                if (gameScorePanel != null && myName != null) {
                    if (myName.equals(playerId)) {
                        // 내 점수
                        gameScorePanel.updateMyScore(score);
                    } else {
                        // 상대 점수
                        gameScorePanel.updateOpponentScore(score);
                    }
                }

                // GameState 업데이트 (결과뷰에서 사용)
                if (gameState != null && myName != null) {
                    if (myName.equals(playerId)) {
                        gameState.setMyScore(score);
                    } else {
                        gameState.setOpponentScore(score);
                    }
                }
                break;

            case "TRASH":
                // 서버 포맷: TRASH|name|category|imagePath|x|y
                if (parts.length >= 6 && gameController != null) {
                    String trashName  = parts[1];
                    String category   = parts[2];
                    String imagePath  = parts[3];
                    int x             = Integer.parseInt(parts[4]);
                    int y             = Integer.parseInt(parts[5]);

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        gameController.spawnTrash(trashName, category, imagePath, x, y);
                    });
                }
                break;

            case "TIME_UPDATE":
                int time = Integer.parseInt(parts[1]);
                if (timePanel != null) {
                    timePanel.updateTime(time);
                }

                // 시간 0이 되었을 때 한 번만 onTimeOver 호출
                if (time == 0 && gameController != null) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        gameController.onTimeOver();
                    });
                }
                break;

            case "WINNER":
                String winnerId = parts[1];
                if (gameController != null) {
                    gameController.showResult(winnerId);
                }
                break;

            case "GAME_END":
                // 서버에서 게임 종료 알림용으로만 사용.
                // TIME_UPDATE 0 에서 이미 onTimeOver, showResult 처리하므로 여기서는 아무 것도 안 함.
                break;

            case "RANKING_RES":
                List<String> items = new ArrayList<>();

                for (int i = 1; i < parts.length && i <= RANKING_LIMIT; i++) {
                    items.add(parts[i]);
                }

                if (rankingView != null) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        rankingView.updateRanking(items);
                    });
                }
                break;
        }
    }
}
