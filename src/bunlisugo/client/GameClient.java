package bunlisugo.client;

import java.io.*;
import java.net.Socket;
import java.util.*;

import bunlisugo.client.command.Command;
import bunlisugo.client.command.CommandRegistry;
import bunlisugo.client.controller.GameController;
import bunlisugo.client.model.*;
import bunlisugo.client.view.*;
import bunlisugo.client.view.game.*;

public class GameClient {
    private static GameClient instance;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private User currentUser;
    private int lastScore = 0;
    public static final int RANKING_LIMIT = 10;

    private SignView signView;
    private LoginView loginView;
    private HomeView homeView;
    private MatchingView matchingView;
    private GameView gameView;
    private RankingView rankingView;

    private GameScorePanel gameScorePanel;
    private TimePanel timePanel;
    private CountdownPanel countdownPanel;
    private TrashBoxPanel trashBoxPanel;

    private List<String> rankList = new ArrayList<>();
    private GameController gameController;
    private GameState gameState;

    private GameClient() {
        // 연결은 나중에 connect(ip)로
    }

    public static GameClient getInstance() {
        if (instance == null) {
            instance = new GameClient();
        }
        return instance;
    }

    public void connect(String ip) {
        try {
            socket = new Socket(ip, 3328);
            System.out.println("Connected to server on IP: " + ip);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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
            throw new RuntimeException("서버 연결 실패 (IP: " + ip + ") : " + e.getMessage());
        }
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

        Command command = CommandRegistry.getCommand(cmd);
        if (command != null) {
            command.execute(parts, this);
        } else {
            System.out.println("Unknown command: " + cmd);
        }
    }

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    public int getLastScore() { return lastScore; }
    public void setLastScore(int lastScore) { this.lastScore = lastScore; }

    public String getNickname() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    public SignView getSignView() {
        return signView;
    }

    public void setSignView(SignView signView) {
        this.signView = signView;
    }

    public LoginView getLoginView() { return loginView; }
    public void setLoginView(LoginView loginView) { this.loginView = loginView; }

    public HomeView getHomeView() { return homeView; }
    public void setHomeView(HomeView homeView) { this.homeView = homeView; }

    public MatchingView getMatchingView() { return matchingView; }
    public void setMatchingView(MatchingView matchingView) { this.matchingView = matchingView; }

    public GameView getGameView() { return gameView; }
    public void setGameView(GameView gameView) { this.gameView = gameView; }

    public RankingView getRankingView() { return rankingView; }
    public void setRankingView(RankingView rankingView) { this.rankingView = rankingView; }

    public GameScorePanel getGameScorePanel() { return gameScorePanel; }
    public void setGameScorePanel(GameScorePanel panel) { this.gameScorePanel = panel; }

    public TimePanel getTimePanel() { return timePanel; }
    public void setTimePanel(TimePanel panel) { this.timePanel = panel; }

    public CountdownPanel getCountdownPanel() { return countdownPanel; }
    public void setCountdownPanel(CountdownPanel panel) { this.countdownPanel = panel; }

    public TrashBoxPanel getTrashBoxPanel() { return trashBoxPanel; }
    public void setTrashBoxPanel(TrashBoxPanel panel) { this.trashBoxPanel = panel; }

    public GameController getGameController() { return gameController; }
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        if (this.gameState != null && this.gameController != null) {
            this.gameController.setGameState(this.gameState);
        }
    }

    public GameState getGameState() { return gameState; }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        if (this.gameController != null && this.gameState != null) {
            this.gameController.setGameState(this.gameState);
        }
    }
}
