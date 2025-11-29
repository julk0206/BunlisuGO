// bunlisugo.server.controller.ResultCommandHandler.java
package bunlisugo.server.controller;

import java.util.logging.Logger;
import bunlisugo.server.service.ResultService;

public class ResultCommandHandler implements ClientCommandHandler {

    private static final Logger logger =
            Logger.getLogger(ResultCommandHandler.class.getName());

    private final ResultService resultService;

    public ResultCommandHandler(ResultService resultService) {
        this.resultService = resultService;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {
        // 기대 패킷: GAME_RESULT|점수
        if (parts.length < 2) {
            session.send("ERROR|BAD_GAME_RESULT_FORMAT");
            return;
        }

        int score;
        try {
            score = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            session.send("ERROR|BAD_GAME_RESULT_SCORE");
            return;
        }

        logger.info("[GAME_RESULT RECV] " + session.getPlayerId() + " score=" + score);

        // 진짜 비즈니스 로직은 서비스에서
        resultService.submitResult(session, score);
    }
}
