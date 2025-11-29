// BunlisuGo/src/bunlisugo/server/controller/MatchCommandHandler.java
package bunlisugo.server.controller;

import java.util.List;
import java.util.logging.Logger;

import bunlisugo.server.entity.User;
import bunlisugo.server.entity.GameSession;
import bunlisugo.server.model.GameRoom;
import bunlisugo.server.service.GameSessionInstance;
import bunlisugo.server.service.MatchingService;

public class MatchCommandHandler implements ClientCommandHandler {

    private static final Logger logger = Logger.getLogger(MatchCommandHandler.class.getName());

    private final MatchingService matchingService;
    private final List<GameClientHandler> sessionList;

    public MatchCommandHandler(MatchingService matchingService, List<GameClientHandler> sessionList) {
        this.matchingService = matchingService;
        this.sessionList = sessionList;
    }

    @Override
    public void handle(String[] parts, GameClientHandler session) {
    	 // 1) 이번에 매칭 요청한 유저를 대기열에 넣고
        User user = session.getCurrentUser();
        matchingService.enqueue(user);

        // 2) 매칭 시도 (인자 없는 match() 사용)
        GameRoom room = matchingService.match();

        // 3) 아직 짝이 안 맞으면 대기 메시지
        if (room == null) {
            session.send("MATCH_WAITING|" + matchingService.getWaitingCount());
            logger.info("[MATCH JOIN] " + session.getPlayerId() +
                        " waiting: " + matchingService.getWaitingCount());
            return;
        }

        // 4) 매칭 성사!
        notifyMatchedPlayers(room);
        logger.info("[MATCH FOUND] room created");
    }

    private void notifyMatchedPlayers(GameRoom room) {
        User p1 = room.getPlayer1();
        User p2 = room.getPlayer2();
        if (p1 == null || p2 == null) {
            logger.warning("[MATCH FOUND] room has null player");
            return;
        }

        String u1 = p1.getUsername();
        String u2 = p2.getUsername();

        GameClientHandler p1Handler = null;
        GameClientHandler p2Handler = null;

        // 1) 두 유저의 핸들러 찾으면서 MATCH_FOUND 전송
        for (GameClientHandler handler : sessionList) {
            if (!handler.isLoggedIn() || handler.getPlayerId() == null) continue;

            if (handler.getPlayerId().equals(u1)) {
                p1Handler = handler;
                handler.send("MATCH_FOUND");
                logger.info("[MATCH FOUND SENT] to " + handler.getPlayerId());
            } else if (handler.getPlayerId().equals(u2)) {
                p2Handler = handler;
                handler.send("MATCH_FOUND");
                logger.info("[MATCH FOUND SENT] to " + handler.getPlayerId());
            }
        }

        if (p1Handler == null || p2Handler == null) {
            logger.warning("[MATCH FOUND] handlers not found for players " + u1 + ", " + u2);
            return;
        }

        // 2) GameSession + GameSessionInstance 생성
        GameSession session = new GameSession(p1.getUserId(), p2.getUserId());
        GameSessionInstance instance = new GameSessionInstance(10, 10, session); // maxX,maxY는 적당히

        // 3) 인스턴스에 핸들러 등록
        instance.setPlayerHandlers(p1Handler, p2Handler);

        logger.info("[MATCH FOUND] GameSessionInstance created");
    }
}
