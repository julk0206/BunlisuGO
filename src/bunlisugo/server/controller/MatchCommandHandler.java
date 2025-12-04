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

    public MatchCommandHandler(MatchingService matchingService,
                               List<GameClientHandler> sessionList,
                               GameService gameService) {
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

                // 매칭 큐에 넣기
                matchingService.enqueue(session);
                int waiting = matchingService.getWaitingCount();
                session.send("MATCH_WAITING|" + waiting);
                logger.info("[MATCH JOIN] " + session.getPlayerId() + " waiting: " + waiting);

                // 매칭 시도
                GameRoom room = matchingService.match();
                if (room != null) {
                    try {
                        gameService.registerRoom(room);   // GameService에 우리가 추가한 메서드
                    } catch (Exception e) {
                        logger.warning("[MATCH FOUND] registerRoom 실패: " + e.getMessage());
                        e.printStackTrace();
                    }

                    // 클라이언트들에게 MATCH_FOUND 보내고 방 연결
                    notifyMatchedPlayers(room);

                    // 카운트다운 + 게임 루프 시작
                    gameService.startGameLoop(room);

                    logger.info("[MATCH FOUND] room created (roomId=" + room.getRoomId() + ")");
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
            e.printStackTrace();
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

            String playerId = handler.getPlayerId();

            // 현재 방 세팅
            if (playerId.equals(p1) || playerId.equals(p2)) {
                handler.setCurrentRoom(room);
            }

            if (playerId.equals(p1)) {
                // p1에게 p2 이름을 상대 이름으로 전달
                handler.send("MATCH_FOUND|" + p2);
                logger.info("[MATCH FOUND SENT] to " + playerId + " opponent: " + p2);
            } else if (playerId.equals(p2)) {
                // p2에게 p1 이름 전달
                handler.send("MATCH_FOUND|" + p1);
                logger.info("[MATCH FOUND SENT] to " + playerId + " opponent: " + p1);
            }
        }
    }
}