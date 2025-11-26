package bunlisugo.server.service;

import java.time.Instant;
import java.util.*;

import bunlisugo.server.model.GameResult;
import bunlisugo.server.model.GameSession;
import bunlisugo.server.model.Trash;

public class GameService {
    // 쓰레기 생성 및 관리 로직
    private final GameSession session;// 60초 게임

    private final int maxX;
    private final int maxY;

    private final TrashManager trashManager;
    private final ScoreManager scoreManager;
    private final TimerManager gameTimer = new TimerManager(60); 

    public GameService(GameSession session, int maxX, int maxY) {
        this.session = session;
        this.maxX = maxX;
        this.maxY = maxY;
        this.trashManager = new TrashManager(maxX, maxY);
        this.scoreManager = new ScoreManager(session);
    }

    // 쓰레기 생성
    public void generateTrash() {
        trashManager.generateTrash();
    }

    // 쓰레기 수집 처리
    public boolean collectTrash(int playerId, String trashName) {
        boolean collected = trashManager.collectTrash(playerId, trashName);
        if (collected) {
            // 점수 부여 (예: 쓰레기 종류에 따라 다르게 설정 가능)
            scoreManager.addScore(playerId, 10); // 예시로 10점 부여
        }
        return collected;
    }

    public List<Trash> getActiveTrashes() {
        return trashManager.getActiveTrashes();
    }

    public int getScore(int playerId) {
        return scoreManager.getScore(playerId);
    }

    public int getRemainingTime() {
        return (int) gameTimer.getRemainingTime() / 1000; // 초 단위 반환
    }

    // 게임 종료 및 승자 결정
    public Integer determineWinner() {

        int player1Score = scoreManager.getScore(session.getPlayer1Id());
        int player2Score = scoreManager.getScore(session.getPlayer2Id());

        if (player1Score > player2Score) {
            session.setWinnerId(session.getPlayer1Id());
            return session.getPlayer1Id();
        } else if (player2Score > player1Score) {
            session.setWinnerId(session.getPlayer2Id());
            return session.getPlayer2Id();
        } else {
            session.setWinnerId(null); // 무승부
            return null;
        }
    }

    public List<GameResult> getGameResults() {
        List<GameResult> results = new ArrayList<>();
        results.add(new GameResult(session.getSessionId(), session.getPlayer1Id(), 
                                    scoreManager.getScore(session.getPlayer1Id())));
        results.add(new GameResult(session.getSessionId(), session.getPlayer2Id(), 
                                    scoreManager.getScore(session.getPlayer2Id())));
        return results;
    }
    
}
