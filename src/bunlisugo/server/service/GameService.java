package bunlisugo.server.service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

import bunlisugo.server.dao.GameDAO;
import bunlisugo.server.dto.TrashDTO;
import bunlisugo.server.entity.GameResult;
import bunlisugo.server.entity.GameSession;
import bunlisugo.server.entity.TrashType;

public class GameService {
    // 쓰레기 생성 및 관리 로직
    private final GameSession session;// 60초 게임
    private final GameDAO gameDAO = new GameDAO();

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
    public void generateTrash(List<TrashType> types) {
        trashManager.generateTrash(types);
    }

    // 랜덤 쓰레기 생성
    // public void generateTrash() {
    //     if (activeTrashes.size() < MAX_TRASH) {
    //         Trash newTrash = TrashData.randomTrash();
    //         activeTrashes.add(newTrash);
    //     }
    // }

    // 쓰레기 수집 처리
    public boolean collectTrash(int playerId, String trashName) {
        boolean collected = trashManager.collectTrash(playerId, trashName);
        if (collected) {
            scoreManager.addScore(playerId, 5);
        }
        return collected;
    }

    public List<TrashDTO> getActiveTrashes() {
        return trashManager.getActiveTrashes();
    }

    //플레이어별 점수 (주기적 호출 필요)
    public int getScore(int playerId) {
        return scoreManager.getScore(playerId);
    }

    //남은 시간 (주기적 호출 필요)
    public int getRemainingTime() {
        return (int) gameTimer.getRemainingTime() / 1000; // 초 단위 반환
    }

    // 승자 결정
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

    // 게임 결과 조회
    public List<GameResult> getGameResults() {
        List<GameResult> results = new ArrayList<>();
        results.add(new GameResult(session.getSessionId(), session.getPlayer1Id(), 
                                    scoreManager.getScore(session.getPlayer1Id())));
        results.add(new GameResult(session.getSessionId(), session.getPlayer2Id(), 
                                    scoreManager.getScore(session.getPlayer2Id())));
        return results;
    }
    
    // 게임 종료
    public void endGame() throws SQLException {
        // 승자 결정
        Integer winnerId = determineWinner();

        // 게임 종료 정보 업데이트
        gameDAO.endGameSession(session.getSessionId(), winnerId);

        // 게임 결과 DB 저장
        List<GameResult> results = getGameResults();
        for (GameResult result : results) {
            gameDAO.saveGameRecord(result);
        }

    }

}
