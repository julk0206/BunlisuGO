package bunlisugo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

import bunlisugo.sevice.LoginService;

public class GameClientHandler extends Thread {

    private static final Logger logger = Logger.getLogger(GameClientHandler.class.getName());

    private final LoginService loginService = new LoginService();

    private final Socket socket;
    private Scanner in;
    private PrintWriter out;

    private boolean loggedIn = false;
    private String playerId;

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

        if (!"LOGIN".equals(cmd) && !loggedIn) {
            out.println("ERROR|NOT_LOGGED_IN");
            return;
        }

        switch (cmd) {
            case "LOGIN":
                handleLogin(parts);
                break;
            default:
                out.println("?");
                break;
        }
    }

    public void handleLogin(String[] parts) {
        if (parts.length < 3) {
            out.println("LOGIN_FAIL|BAD_FORMAT");
            return;
        }

        String id = parts[1];
        String pw = parts[2];

        logger.info("[LOGIN TRY] " + id);

        try {
            if (!loginService.login(id, pw)) {
                out.println("LOGIN_FAIL|BAD_CREDENTIALS");
                logger.info("[LOGIN FAIL] " + id);
                return;
            }
        } catch (IllegalStateException e) {
            out.println("LOGIN_FAIL|SAME_ID_EXISTS");
            logger.info("[LOGIN FAIL] same id: " + id);
            return;
        } catch (Exception e) {
            out.println("LOGIN_FAIL|SERVER_ERROR");
            logger.warning("[LOGIN ERROR] " + e.getMessage());
            return;
        }

        loggedIn = true;
        playerId = id;
        out.println("LOGIN_OK|" + id);
        logger.info("[LOGIN OK] " + id);
    }
}
