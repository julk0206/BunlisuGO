package bunlisugo.server.service;

import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

import bunlisugo.server.dao.GameDAO;
import bunlisugo.server.dao.TrashTypeDAO;
import bunlisugo.server.dto.TrashDTO;
import bunlisugo.server.entity.GameResult;
import bunlisugo.server.entity.GameSession;
import bunlisugo.server.entity.TrashType;

public class GameService {
    // 쓰레기 생성 및 관리 로직
    private final GameSession session;// 60초 게임
    private final GameSessionInstance instance;
    private final GameDAO gameDAO = new GameDAO();
    private TrashTypeDAO trashTypeDAO = new TrashTypeDAO();

    private final List<TrashType> allTrashTypes;

    private static final int COUNTDOWN_SECONDS = 3;
    private static final long GAME_LOOP_INTERVAL_MS = 1000; // 게임 루프 간격 (1초)
    private static final long TRASH_SPAWN_INTERVAL_MS = 2000; // 쓰레기 생성 간격 (2초)


    public GameService(GameSession session, int maxX, int maxY) {
        this.session = session;
        this.instance = new GameSessionInstance(maxX, maxY, session);
        this.allTrashTypes = trashTypeDAO.getTrashTypes(); //DB에서 쓰레기 타입 가져오기
    }

    public void startGame() {
        Thread gameThread = new Thread(new Runnable() {
        @Override
        public void run() {
            startGameLoop(); // 실제 게임 진행 메서드 호출
        }
    });
    gameThread.start();
    }

    public void startGameLoop() {
        
        // 카운트다운 시작
        for (int i = COUNTDOWN_SECONDS; i > 0; i--) {
            System.out.println("게임이 " + i + " 초 후에 시작됩니다...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 게임 시작
        System.out.println("게임 시작!");
        instance.getTimerManager().startTimer();

        // 게임 루프 시작
        long lastTrashSpawn = System.currentTimeMillis();
        while (!instance.getTimerManager().isFinished()) {
            
            // 쓰레기 2초마다 생성
            long now = System.currentTimeMillis();
            if(now - lastTrashSpawn >= TRASH_SPAWN_INTERVAL_MS) {
                instance.getTrashManager().generateTrash(allTrashTypes);
                lastTrashSpawn = now;
            }

            try {
                Thread.sleep(GAME_LOOP_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }        

        try {
            endGame();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 쓰레기 생성
    public void generateTrash(List<TrashType> types) {
        instance.getTrashManager().generateTrash(types);
    }

    // 쓰레기 수집 처리
    public boolean collectTrash(int playerId, String trashName) {
        boolean collected = instance.getTrashManager().collectTrash(playerId, trashName);
        if (collected) {
            instance.getScoreManager().addScore(playerId, 5);
        }
        return collected;
    }

    public List<TrashDTO> getActiveTrashes() {
        return instance.getTrashManager().getActiveTrashes();
    }

    //플레이어별 점수 (주기적 호출 필요)
    public int getScore(int playerId) {
        return instance.getScoreManager().getScore(playerId);
    }

    //남은 시간 (주기적 호출 필요)
    public int getRemainingTime() {
        return (int) instance.getTimerManager().getRemainingTime() / 1000; // 초 단위 반환
    }

    // 승자 결정
    public Integer determineWinner() {

        int player1Score = instance.getScoreManager().getScore(session.getPlayer1Id());
        int player2Score = instance.getScoreManager().getScore(session.getPlayer2Id());

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
                                    instance.getScoreManager().getScore(session.getPlayer1Id())));
        results.add(new GameResult(session.getSessionId(), session.getPlayer2Id(), 
                                    instance.getScoreManager().getScore(session.getPlayer2Id())));
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
