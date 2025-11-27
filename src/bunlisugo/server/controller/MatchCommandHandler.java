// bunlisugo.server.controller.MatchCommandHandler.java
package bunlisugo.server.controller;

import java.util.List;
import java.util.logging.Logger;

import bunlisugo.server.entity.User;
import bunlisugo.server.model.GameRoom;
import bunlisugo.server.service.MatchingService;

public class MatchCommandHandler implements ClientCommandHandler {

    private static final Logger logger = Logger.getLogger(MatchCommandHandler.class.getName());

    private final MatchingService matchingService;
    private final List<GameClientHandler> sessionList; // 브로드캐스트용

    public MatchCommandHandler(MatchingService matchingService, List<GameClientHandler> sessionList) {
        this.matchingService = matchingService;
        this.sessionList = sessionList;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {
        if (parts.length < 2) {
            session.send("MATCH_FAIL|BAD_FORMAT");
            logger.warning("[MATCH FAIL] bad format from " + session.getPlayerId());
            return;
        }

        if (!session.isLoggedIn()) {
            session.send("MATCH_FAIL|NOT_LOGGED_IN");
            logger.info("[MATCH FAIL] not logged in user");
            return;
        }

        String action = parts[1];

        try {
            if ("JOIN".equalsIgnoreCase(action)) {
                matchingService.enqueue(session.getCurrentUser());
                int waiting = matchingService.getWaitingCount();
                session.send("MATCH_WAITING|" + waiting);
                logger.info("[MATCH JOIN] " + session.getPlayerId() + " waiting: " + waiting);

                GameRoom room = matchingService.match();
                if (room != null) {
                    notifyMatchedPlayers(room);
                    logger.info("[MATCH FOUND] room created");
                }

            } else if ("CANCEL".equalsIgnoreCase(action)) {
                matchingService.cancel(session.getCurrentUser());
                int waiting = matchingService.getWaitingCount();
                session.send("MATCH_WAITING|" + waiting);
                logger.info("[MATCH CANCEL] " + session.getPlayerId() + " waiting: " + waiting);

            } else {
                session.send("MATCH_FAIL|UNKNOWN_ACTION");
                logger.warning("[MATCH FAIL] unknown action: " + action);
            }

        } catch (Exception e) {
            session.send("MATCH_FAIL|SERVER_ERROR");
            logger.warning("[MATCH ERROR] " + e.getMessage());
        }
    }

    // 방에 있는 두 플레이어에게 MATCH_FOUND 뿌리기
    private void notifyMatchedPlayers(GameRoom room) {
        User p1 = room.getPlayer1();
        User p2 = room.getPlayer2();

        if (p1 == null || p2 == null) {
            logger.warning("[MATCH FOUND] room has null player");
            return;
        }

        String u1 = p1.getUsername();
        String u2 = p2.getUsername();

        for (GameClientHandler handler : sessionList) {
            if (!handler.isLoggedIn() || handler.getPlayerId() == null) continue;

            if (handler.getPlayerId().equals(u1) || handler.getPlayerId().equals(u2)) {
                handler.send("MATCH_FOUND");
                logger.info("[MATCH FOUND SENT] to " + handler.getPlayerId());
            }
        }
    }
}
