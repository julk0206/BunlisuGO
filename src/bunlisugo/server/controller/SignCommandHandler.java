// bunlisugo.server.controller.SignupCommandHandler.java
package bunlisugo.server.controller;

import java.util.logging.Logger;

import bunlisugo.server.service.SignupService;

public class SignCommandHandler implements ClientCommandHandler {

    private static final Logger logger = Logger.getLogger(SignCommandHandler.class.getName());
    private final SignupService signupService;

    public SignCommandHandler(SignupService signupService) {
        this.signupService = signupService;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {
        if (parts.length < 3) {
            session.send("SIGNUP_FAIL|BAD_FORMAT");
            return;
        }

        String username = parts[1];
        String pw = parts[2];

        logger.info("[SIGNUP TRY] " + username);

        try {
            boolean result = signupService.signup(username, pw);
            if (!result) {
                session.send("SIGNUP_FAIL|USERNAME_EXISTS");
                logger.info("[SIGNUP FAIL] username exists: " + username);
                return;
            }
        } catch (Exception e) {
            session.send("SIGNUP_FAIL|SERVER_ERROR");
            logger.warning("[SIGNUP ERROR] " + e.getMessage());
            return;
        }

        session.send("SIGNUP_OK|" + username);
        logger.info("[SIGNUP OK] " + username);
    }
}
