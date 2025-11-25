package bunlisugo.server.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

import bunlisugo.server.model.GameRoom;
import bunlisugo.server.model.User;
import bunlisugo.server.service.LoginService;
import bunlisugo.server.service.MatchingService;
import bunlisugo.server.service.SignupService;

public class GameClientHandler extends Thread {

    private static final Logger logger = Logger.getLogger(GameClientHandler.class.getName());

    private final LoginService loginService = new LoginService();
    private final SignupService signupService = new SignupService();
    private static final MatchingService matchingService = new MatchingService();

    private final Socket socket;
    private Scanner in;
    private PrintWriter out;

    private boolean loggedIn = false;
    private String playerId;
    // ★ 매칭 큐에 넣을 유저 정보 (지금은 username만 사용)
    private User currentUser;

    public GameClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in  = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String line = in.nextLine().trim();
                if (line.isEmpty()) {
                    continue; // 빈 줄 무시
                }
                handleMessage(line);
            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
        } finally {
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
            out.println("?");
            return;
        }

        String cmd = parts[0];

        if (!"LOGIN".equals(cmd) && !"SIGNUP".equals(cmd) && !loggedIn) {
            out.println("ERROR|NOT_LOGGED_IN");
            return;
        }

        switch (cmd) {
            case "LOGIN":
                handleLogin(parts);
                break;
            case "SIGNUP":
                handleSignup(parts);
                break;
            case "MATCH":                      
                handleMatching(parts);
                break;
            default:
                out.println("?");
                break;
        }
    }

    public void handleSignup(String[] parts) {
        if (parts.length < 3) {
            out.println("SIGNUP_FAIL|BAD_FORMAT");
            return;
        }

        String username = parts[1];
        String pw = parts[2];

        logger.info("[SIGNUP TRY] " + username);

        try {
            boolean result = signupService.signup(username, pw);
            if (!result) {
                out.println("SIGNUP_FAIL|USERNAME_EXISTS");
                logger.info("[SIGNUP FAIL] username exists: " + username);
                return;
            }
        } catch (Exception e) {
            out.println("SIGNUP_FAIL|SERVER_ERROR");
            logger.warning("[SIGNUP ERROR] " + e.getMessage());
            return;
        }

        out.println("SIGNUP_OK|" + username);
        logger.info("[SIGNUP OK] " + username);
    }

    public void handleLogin(String[] parts) {
        if (parts.length < 3) {
            out.println("LOGIN_FAIL|BAD_FORMAT");
            return;
        }

        String username = parts[1];
        String pw = parts[2];

        logger.info("[LOGIN TRY] " + username);

        try {
            if (!loginService.login(username, pw)) {
                out.println("LOGIN_FAIL|BAD_CREDENTIALS");
                logger.info("[LOGIN FAIL] " + username);
                return;
            }
        } catch (IllegalStateException e) {
            out.println("LOGIN_FAIL|SAME_USERNAME_EXISTS");
            logger.info("[LOGIN FAIL] same username: " + username);
            return;
        } catch (Exception e) {
            out.println("LOGIN_FAIL|SERVER_ERROR");
            logger.warning("[LOGIN ERROR] " + e.getMessage());
            return;
        }

        loggedIn = true;
        playerId = username;

        // ★ 매칭용 User 객체 생성 (필요한 최소 정보만)
        currentUser = new User();
        currentUser.setUsername(username);

        out.println("LOGIN_OK|" + username);
        logger.info("[LOGIN OK] " + username);
    }

    public void handleMatching(String[] parts) {
        // 프로토콜: MATCH|JOIN  또는 MATCH|CANCEL
        if (parts.length < 2) {
            out.println("MATCH_FAIL|BAD_FORMAT");
            logger.warning("[MATCH FAIL] bad format from " + playerId);
            return;
        }

        if (!loggedIn) {
            out.println("MATCH_FAIL|NOT_LOGGED_IN");
            logger.info("[MATCH FAIL] not logged in user");
            return;
        }

        String action = parts[1];

        try {
            if ("JOIN".equalsIgnoreCase(action)) {
                // 1. 대기열에 추가  ★ 인스턴스로 호출
                matchingService.enqueue(currentUser);
                int waiting = matchingService.getWaitingCount();
                out.println("MATCH_WAITING|" + waiting);
                logger.info("[MATCH JOIN] " + playerId + " waiting: " + waiting);

                // 2. 두 명 이상이면 매칭 성사
                GameRoom room = matchingService.match();
                if (room != null) {
                    // 지금은 방 정보까지 쓰진 않고, 단순히 "매칭 완료" 신호만 보냄
                    out.println("MATCH_FOUND");
                    logger.info("[MATCH FOUND] room created");
                }

            } else if ("CANCEL".equalsIgnoreCase(action)) {
                matchingService.cancel(currentUser);
                int waiting = matchingService.getWaitingCount();
                out.println("MATCH_WAITING|" + waiting);
                logger.info("[MATCH CANCEL] " + playerId + " waiting: " + waiting);

            } else {
                out.println("MATCH_FAIL|UNKNOWN_ACTION");
                logger.warning("[MATCH FAIL] unknown action: " + action);
            }

        } catch (Exception e) {
            out.println("MATCH_FAIL|SERVER_ERROR");
            logger.warning("[MATCH ERROR] " + e.getMessage());
        }
    }
}
