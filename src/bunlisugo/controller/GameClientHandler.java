package bunlisugo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

import bunlisugo.service.LoginService;
import bunlisugo.service.SignupService;

public class GameClientHandler extends Thread {

    private static final Logger logger = Logger.getLogger(GameClientHandler.class.getName());

    private final LoginService loginService = new LoginService();
    private final SignupService signupService = new SignupService();
    
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
        out.println("LOGIN_OK|" + username);
        logger.info("[LOGIN OK] " + username);
    }
}
