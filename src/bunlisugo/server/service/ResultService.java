package bunlisugo.server.service;

import java.sql.SQLException;
import java.util.logging.Logger;

import bunlisugo.server.controller.GameClientHandler;
import bunlisugo.server.dao.GameDAO;
import bunlisugo.server.entity.GameResult;
import bunlisugo.server.entity.GameSession;

public class ResultService {

    private static final Logger logger =
            Logger.getLogger(ResultService.class.getName());

    private final GameDAO gameDAO = new GameDAO();
    
    public ResultService() {
    }

    // 클라이언트 한 명이 GAME_RESULT|score 를 보냈을 때 호출
    public void submitResult(GameClientHandler sessionHandler, int score) {

        GameSessionInstance instance = GameSessionInstance.getInstance();
        if (instance == null) {
            sessionHandler.send("ERROR|NO_GAME_SESSION");
            logger.warning("[RESULT] no GameSessionInstance");
            return;
        }

        GameSession gameSession = instance.getSession();
        ScoreManager scoreManager = instance.getScoreManager();

        int playerId;
        boolean isP1;

        if (sessionHandler == instance.getPlayer1Handler()) {
            playerId = gameSession.getPlayer1Id();
            isP1 = true;
        } else if (sessionHandler == instance.getPlayer2Handler()) {
            playerId = gameSession.getPlayer2Id();
            isP1 = false;
        } else {
            sessionHandler.send("ERROR|NOT_IN_SESSION");
            logger.warning("[RESULT] handler not in session");
            return;
        }

        // 1) 점수 반영(큐에서 먼저 꺼낸 애가 항상 player1)
        scoreManager.addScore(isP1, score);
        logger.info("[RESULT] addScore isP1=" + isP1 + " score=" + score);

        // 2) 제출 플래그 세팅
        if (isP1) {
            instance.setP1Submitted(true);
        } else {
            instance.setP2Submitted(true);
        }

        // 3) 두 명 다 결과 안 냈으면 아직 기다림
        if (!(instance.isP1Submitted() && instance.isP2Submitted())) {
            logger.info("[RESULT] waiting other player");
            return;
        }

        // 4) 이미 RESULT 보낸 적 있으면 또 보내지 않기
        if (instance.isResultSent()) {
            logger.info("[RESULT] already sent");
            return;
        }
        instance.setResultSent(true);

        // 5) 최종 점수 계산
        int p1Score = scoreManager.getPlayer1Score();
        int p2Score = scoreManager.getPlayer2Score();

        GameClientHandler p1Session = instance.getPlayer1Handler();
        GameClientHandler p2Session = instance.getPlayer2Handler();

        // 5-1) DB에 게임 결과/랭킹 점수 반영
        int sessionId = gameSession.getSessionId();   // game_sessions.session_id
        int p1Id = gameSession.getPlayer1Id();        // users.user_id
        int p2Id = gameSession.getPlayer2Id();

        try {
            // game_results 테이블에 두 플레이어 기록 저장
            gameDAO.saveGameRecord(new GameResult(sessionId, p1Id, p1Score));
            gameDAO.saveGameRecord(new GameResult(sessionId, p2Id, p2Score));

            // users.ranking_score 누적 (원하면 주석 해제해서 사용)
            gameDAO.updateRankingScore(p1Id, p1Score);
            gameDAO.updateRankingScore(p2Id, p2Score);

            logger.info("[RESULT] game results saved to DB. sessionId=" + sessionId);

        } catch (SQLException e) {
            logger.warning("[RESULT] failed to save game result: " + e.getMessage());
            e.printStackTrace();
            // 저장 실패해도 게임 결과 전송은 계속 진행
        }

        // 6) RESULT 패킷 전송
        String packet = "RESULT|" + p1Session.getPlayerId() + "|" + p1Score
                       + "|" + p2Session.getPlayerId() + "|" + p2Score;

        logger.info("[RESULT SEND] " + packet);

        p1Session.send(packet);
        p2Session.send(packet);
    }
}
