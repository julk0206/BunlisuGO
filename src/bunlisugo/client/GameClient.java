package bunlisugo.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bunlisugo.client.command.Command;
import bunlisugo.client.command.CommandRegistry;

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
    private int lastScore = 0;

    public static final int RANKING_LIMIT = 10;

    private LoginView loginView;
    private HomeView homeView;
    private MatchingView matchingView;
    private RankingView rankingView;

    private GameScorePanel gameScorePanel;
    private TimePanel timePanel;

    private List<String> rankList = new ArrayList<>();

    private GameController gameController;
    private GameState gameState;

    public LoginView getLoginView() { return loginView; }
    public MatchingView getMatchingView() { return matchingView; }
    public GameScorePanel getGameScorePanel() { return gameScorePanel; }
    public TimePanel getTimePanel() { return timePanel; }
    public RankingView getRankingView() { return rankingView; }
    public void setCurrentUser(User user) { this.currentUser = user; }

    public void setLoginView(LoginView loginView) { this.loginView = loginView; }
    public void setHomeView(HomeView homeView) { this.homeView = homeView; }
    public void setMatchingView(MatchingView matchingView) { this.matchingView = matchingView; }
    public void setRankingView(RankingView rankingView) { this.rankingView = rankingView; }

    public User getCurrentUser() { return currentUser; }
    public int getLastScore() { return lastScore; }
    public void setLastScore(int lastScore) { this.lastScore = lastScore; }

    public String getNickname() {
        return currentUser != null ? currentUser.getUsername() : null;
    }

    public void setGameScorePanel(GameScorePanel panel) { this.gameScorePanel = panel; }
    public void setTimePanel(TimePanel panel) { this.timePanel = panel; }

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

    private GameClient() {
        try {
            socket = new Socket("10.240.152.133", 3328);
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
        if (instance == null) instance = new GameClient();
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

        Command command = CommandRegistry.getCommand(cmd);
        if (command != null) {
            command.execute(parts, this);
        } else {
            System.out.println("Unknown command: " + cmd);
        }
    }
}
