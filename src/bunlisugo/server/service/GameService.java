package bunlisugo.server.service;

import java.sql.SQLException;
import java.util.*;

import bunlisugo.server.controller.GameClientHandler;
import bunlisugo.server.controller.SendCommandHandler;
import bunlisugo.server.dao.GameDAO;
import bunlisugo.server.dto.TrashDTO;
import bunlisugo.server.entity.GameRoom;
import bunlisugo.server.entity.Ranking;
import bunlisugo.server.entity.ScreenSize;


public class GameService {
    // 쓰레기 생성 및 관리 로직
    private final GameRoom room;// 60초 게임
    private final GameDAO gameDAO = new GameDAO();

    private ScoreManager scoreManager;
    private TrashManager trashManager;
    private final TimerManager timerManager = new TimerManager(60);
    private final ScreenSize screenSize;
    private SendCommandHandler sendHandler;

    private final List<GameClientHandler> clients;

    private static final long GAME_LOOP_INTERVAL_MS = 1000; // 게임 루프 간격 (1초)
    private static final long TRASH_SPAWN_INTERVAL_MS = 1500; // 쓰레기 생성 간격 (2초)

    public GameService(GameRoom room, ScreenSize screenSize, List<GameClientHandler> clients) {
        this.room = room;
        this.screenSize = screenSize;
        this.clients = clients;
        this.sendHandler = new SendCommandHandler(this, clients);
        this.scoreManager = new ScoreManager(room);
        this.trashManager = new TrashManager(screenSize.getMaxX(), screenSize.getMaxY()); // 쓰레기 관리 로직 생성 (maxX, maxY);
    }

    public void startGameLoop(GameRoom room) {

        // 게임 시작
        System.out.println("게임 시작!");
        timerManager.startTimer();

        // 게임 루프 시작
        long lastTrashSpawn = System.currentTimeMillis();
        while (!timerManager.isFinished()) {
            long now = System.currentTimeMillis();
            long lastTimeSent = 0;

            // 1초마다 남은 시간 전송
            if(now - lastTimeSent >= 1000) {
                sendHandler.broadcastTime();
                lastTimeSent = now;
            }

            // 쓰레기 2초마다 생성
            if(now - lastTrashSpawn >= TRASH_SPAWN_INTERVAL_MS) {
                TrashDTO trash = trashManager.generateTrash(screenSize.getMaxX(), screenSize.getMaxY());
                sendHandler.broadcastTrash(trash);
                lastTrashSpawn = now;
            }

            try {
                Thread.sleep(GAME_LOOP_INTERVAL_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }        
        sendHandler.broadcastGameEnd();
    }

    public List<TrashDTO> getActiveTrashes() {
        return trashManager.getActiveTrashes();
    }

    //플레이어별 점수
    public int getScore(String playerId) {
        return scoreManager.getScore(playerId);
    }

    public void addScore(String playerId, int score) {
        scoreManager.addScore(playerId, score);

        int newScore = scoreManager.getScore(playerId);

        for (GameClientHandler client : clients) {
            if (!client.getPlayerId().equals(playerId)) { // 자신이 아닌 상대방
                client.send("SCORE_UPDATE|" + playerId + "|" + newScore);
            }
        }
    }


    // 승자 결정
    public String determineWinner(GameRoom room) {

        int player1Score = scoreManager.getScore(room.getPlayer1Id());
        int player2Score = scoreManager.getScore(room.getPlayer2Id());

        if (player1Score > player2Score) {
            room.setWinnerId(room.getPlayer1Id());
            return room.getPlayer1Id();
        } else if (player2Score > player1Score) {
            room.setWinnerId(room.getPlayer2Id());
            return room.getPlayer2Id();
        } else {
            room.setWinnerId(null); // 무승부
            return null;
        }
    }

    // 게임 결과 조회
    public GameRoom getGameResults() {
        return room;
    }
    
    // 게임 종료
    public void endGame(GameRoom room, String wId) throws SQLException {
        // 승자 결정
        String winnerId = wId;
        String player1Id = room.getPlayer1Id();
        String player2Id = room.getPlayer2Id();

        // 게임 종료 정보 업데이트
        gameDAO.endGameRoom(room);
        

        // 랭킹 DB 업데이트
        if (winnerId == player1Id) {
            String loserId = player2Id;

            gameDAO.updateRankingScore(winnerId, +10);
            gameDAO.updateRankingScore(loserId, -5);

        } else if (winnerId == player2Id) {
            String loserId = player1Id;

            gameDAO.updateRankingScore(winnerId, +10);
            gameDAO.updateRankingScore(loserId, -5);

        }

        // 클라이언트에게 게임 종료 알림
        for (GameClientHandler client : clients) {
                client.send("WINNERs|" + winnerId);
        }

    }

    // 랭킹 조회
    public List<Ranking> getRankingList(int limit) throws SQLException {
        return gameDAO.getRankingList(limit);
    }

}