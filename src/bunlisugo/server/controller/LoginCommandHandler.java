// bunlisugo.server.controller.LoginCommandHandler.java
package bunlisugo.server.controller;

import java.util.logging.Logger;

import bunlisugo.server.entity.ScreenSize;
import bunlisugo.server.entity.User;
import bunlisugo.server.service.LoginService;

public class LoginCommandHandler implements ClientCommandHandler {

    private static final Logger logger = Logger.getLogger(LoginCommandHandler.class.getName());
    private final LoginService loginService;

    public LoginCommandHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {
        if (parts.length < 5) {
            session.send("LOGIN_FAIL|BAD_FORMAT");
            return;
        }

        String username = parts[1];
        String pw = parts[2];
        int maxX = Integer.parseInt(parts[3]);  
        int maxY = Integer.parseInt(parts[4]);

        ScreenSize size = new ScreenSize();
        size.setMaxX(maxX);
        size.setMaxY(maxY);

        logger.info("[LOGIN TRY] " + username);

        try {
            if (!loginService.login(username, pw, maxX, maxY)) {
                session.send("LOGIN_FAIL|BAD_CREDENTIALS");
                logger.info("[LOGIN FAIL] " + username);
                return;
            }
        } catch (IllegalStateException e) {
            session.send("LOGIN_FAIL|SAME_USERNAME_EXISTS");
            logger.info("[LOGIN FAIL] same username: " + username);
            return;
        } catch (Exception e) {
            session.send("LOGIN_FAIL|SERVER_ERROR");
            logger.warning("[LOGIN ERROR] " + e.getMessage());
            return;
        }

        // 세션 상태 업데이트
        session.setLoggedIn(true);
        session.setPlayerId(username);

        User user = new User();
        user.setUsername(username);
        session.setCurrentUser(user);

        session.send("LOGIN_OK|" + username);
        logger.info("[LOGIN OK] " + username);
    }
}