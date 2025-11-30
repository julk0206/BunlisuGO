// bunlisugo.server.controller.MatchCommandHandler.java
package bunlisugo.server.controller;

import java.util.List;
import java.util.logging.Logger;
import bunlisugo.server.entity.GameRoom;
import bunlisugo.server.service.GameService;
import bunlisugo.server.service.MatchingService;

public class MatchCommandHandler implements ClientCommandHandler {

    private static final Logger logger = Logger.getLogger(MatchCommandHandler.class.getName());

    private final MatchingService matchingService;
    private final List<GameClientHandler> clients; // 브로드캐스트용
    private final GameService gameService;

    public MatchCommandHandler(MatchingService matchingService, List<GameClientHandler> sessionList, GameService gameService) {
        this.matchingService = matchingService;
        this.clients = sessionList;
        this.gameService = gameService;
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
                matchingService.enqueue(session);
                int waiting = matchingService.getWaitingCount();
                session.send("MATCH_WAITING|" + waiting);
                logger.info("[MATCH JOIN] " + session.getPlayerId() + " waiting: " + waiting);

                GameRoom room = matchingService.match();
                if (room != null) {
                    notifyMatchedPlayers(room);
                    startCountdown(room);
                    logger.info("[MATCH FOUND] room created");
                }

            } else if ("CANCEL".equalsIgnoreCase(action)) {
                matchingService.cancel(session);
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
        String p1 = room.getPlayer1Id();
        String p2 = room.getPlayer2Id();

        if (p1 == null || p2 == null) {
            logger.warning("[MATCH FOUND] room has null player");
            return;
        }

        for (GameClientHandler handler : clients) {
            if (!handler.isLoggedIn() || handler.getPlayerId() == null) continue;

            if (handler.getPlayerId().equals(p1)) {
                // p1에게 p2 이름을 상대방 이름으로 전달
                handler.send("MATCH_FOUND|" + p2);
                logger.info("[MATCH FOUND SENT] to " + handler.getPlayerId() + " opponent: " + p2);
            } else if (handler.getPlayerId().equals(p2)) {
                // p2에게 p1 이름 전달
                handler.send("MATCH_FOUND|" + p1);
                logger.info("[MATCH FOUND SENT] to " + handler.getPlayerId() + " opponent: " + p1);
            }
        }
    }

    private void startCountdown(GameRoom room) {
        String p1 = room.getPlayer1Id();
        String p2 = room.getPlayer2Id();

        new Thread(() -> {
            try {
                Thread.sleep(1000); // 1초 뒤 시작

                for (int i = 3; i >= 1; i--) {
                    for (GameClientHandler client : clients) {
                        if (client.isLoggedIn() &&
                            (client.getPlayerId().equals(p1) || client.getPlayerId().equals(p2))) {
                            client.send("COUNTDOWN|" + i);
                        }
                    }
                    Thread.sleep(1000); // 1초 간격
                }

                gameService.startGameLoop(room);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
