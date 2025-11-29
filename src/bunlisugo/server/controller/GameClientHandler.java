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

import bunlisugo.server.entity.User;
import bunlisugo.server.service.GameSessionInstance;
import bunlisugo.server.service.LoginService;
import bunlisugo.server.service.MatchingService;
import bunlisugo.server.service.RankingService;
import bunlisugo.server.service.ResultService;
import bunlisugo.server.service.SignupService;

public class GameClientHandler extends Thread {

    private static final Logger logger = Logger.getLogger(GameClientHandler.class.getName());

    private final LoginService loginService  = new LoginService();
    private final SignupService signupService = new SignupService();
    private static final MatchingService matchingService = new MatchingService();
    private static final ResultService resultService = new ResultService(); 
    private static final RankingService rankingService = new RankingService();
    private GameSessionInstance gameSessionInstance;
    
    // 서버에 붙어 있는 모든 클라이언트 핸들러 목록
    static final List<GameClientHandler> handlers = new CopyOnWriteArrayList<>();

    private final Socket socket;
    private Scanner in;
    private PrintWriter out;

    // 세션 상태
    private boolean loggedIn = false;
    private String playerId;
    private User currentUser;

    // 명령별 핸들러 맵
    private final Map<String, ClientCommandHandler> commandHandlers = new HashMap<>();

    public GameClientHandler(Socket socket) {
        this.socket = socket;
        handlers.add(this); // 새로 만들어진 핸들러(자기 자신)를 리스트에 등록

        // 명령 핸들러 등록
        commandHandlers.put("LOGIN",  new LoginCommandHandler(loginService));
        commandHandlers.put("SIGNUP", new SignCommandHandler(signupService));
        commandHandlers.put("MATCH",  new MatchCommandHandler(matchingService, handlers));
        commandHandlers.put("GAME_RESULT", new ResultCommandHandler(resultService));
        commandHandlers.put("RANKING_REQ", new RankingCommandHandler(rankingService));
    }

    @Override
    public void run() {
        try {
            in  = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                handleMessage(line);
            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
        } finally {
            handlers.remove(this);

            if (playerId != null) {
                loginService.logout(playerId);
                logger.info("[LOGOUT] " + playerId);
            }
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void handleMessage(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 0) {
            send("?");
            return;
        }

        String cmd = parts[0];

        // 로그인 전에 허용되는 명령 제한
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

    // 세션 상태 & 전송

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
    
    public GameSessionInstance getGameSessionInstance() { 
        return gameSessionInstance;
    }
}
