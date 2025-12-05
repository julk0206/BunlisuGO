package bunlisugo.server.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import bunlisugo.server.entity.GameRoom;
import bunlisugo.server.entity.User;
import bunlisugo.server.entity.ScreenSize;
import bunlisugo.server.service.GameService;
import bunlisugo.server.service.LoginService;
import bunlisugo.server.service.MatchingService;
import bunlisugo.server.service.SignupService;

public class GameClientHandler extends Thread {

    private static final Logger logger = Logger.getLogger(GameClientHandler.class.getName());

    private final LoginService loginService;
    private final SignupService signupService = new SignupService();
    private static final MatchingService matchingService = new MatchingService();

    private static final ScreenSize DEFAULT_SCREEN_SIZE = new ScreenSize();
    static {
        DEFAULT_SCREEN_SIZE.setMaxX(1200);
        DEFAULT_SCREEN_SIZE.setMaxY(750);
    }

    private GameService gameSerivce = new GameService(null, DEFAULT_SCREEN_SIZE, handlers);

    static final List<GameClientHandler> handlers = new CopyOnWriteArrayList<>();

    private final Socket socket;
    private Scanner in;
    private PrintWriter out;

    private boolean loggedIn = false;
    private String playerId;
    private User currentUser;
    private GameRoom currentRoom;

    private final Map<String, ClientCommandHandler> commandHandlers = new HashMap<>();

    public GameClientHandler(Socket socket, LoginService loginService) {
        this.socket = socket;
        this.loginService = loginService;
        handlers.add(this);

        // 명령 핸들러 등록
        commandHandlers.put("LOGIN", new LoginCommandHandler(loginService));
        commandHandlers.put("SIGNUP", new SignCommandHandler(signupService));
        commandHandlers.put("MATCH", new MatchCommandHandler(matchingService, handlers, gameSerivce));
        commandHandlers.put("SCORE", new ScoreCommandHandler(gameSerivce, handlers));
        commandHandlers.put("RANKING_REQ", new RankCommandHandler(gameSerivce));
        commandHandlers.put("RESULT", new ResultCommandHandler(gameSerivce));
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.isEmpty()) continue;

                handleMessage(line);
            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
        } finally {
            handlers.remove(this);

            if (playerId != null) {
                loginService.logout(playerId); // 로그아웃 처리
                logger.info("[LOGOUT] " + playerId);
            }

            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void handleMessage(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 0) {
            send("?");
            return;
        }

        String cmd = parts[0];

        if (!"LOGIN".equals(cmd) && !"SIGNUP".equals(cmd) && !loggedIn) {
            send("ERROR|NOT_LOGGED_IN");
            return;
        }

        ClientCommandHandler handler = commandHandlers.get(cmd);
        if (handler == null) {
            send("?");
            return;
        }

        handler.handle(parts, this);
    }

    public void send(String msg) {
        out.println(msg);
        out.flush();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public GameRoom getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(GameRoom room) {
        this.currentRoom = room;
    }
}
