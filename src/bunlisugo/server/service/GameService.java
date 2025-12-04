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

    // 현재 진행 중인 방 (startGameLoop에서 설정)
    private GameRoom room;

    private final GameDAO gameDAO = new GameDAO();

    // 점수 / 쓰레기 / 타이머 관리
    private ScoreManager scoreManager;
    private TrashManager trashManager;
    private final TimerManager timerManager = new TimerManager(60);

    private final ScreenSize screenSize;
    private final List<GameClientHandler> clients;
    private final SendCommandHandler sendHandler;

    private static final long GAME_LOOP_INTERVAL_MS   = 1000; // 1초
    private static final long TRASH_SPAWN_INTERVAL_MS = 1500; // 1.5초

    // 모든 클라이언트 게임 시작 준비되었는지 확인
    private final Vector<String> readyPlayers = new Vector<>();

    public GameService(GameRoom room, ScreenSize screenSize, List<GameClientHandler> clients) {
        // room 은 아직 null일 수 있음 → startGameLoop 에서 진짜 방으로 설정
        this.room = room;
        this.screenSize = screenSize;
        this.clients = clients;
        this.sendHandler = new SendCommandHandler(this, clients, timerManager);
    }

 
    public void startGameLoop(GameRoom room) {
        this.room = room;
        this.scoreManager = new ScoreManager(room);
        this.trashManager = new TrashManager(screenSize.getMaxX(), screenSize.getMaxY());
        sendHandler.setRoom(room);

        new Thread(() -> {
            try {
                for (int i = 3; i >= 0; i--) {
                    sendHandler.broadcastCountdown(i);
                    Thread.sleep(1000);
                }

                // 카운트다운이 끝난 뒤에 타이머 시작 + 초기 TIME_UPDATE 전송
                System.out.println("게임 시작!");
                timerManager.startTimer();
                sendHandler.broadcastTime();

                long lastTrashSpawn = System.currentTimeMillis();
                long lastTimeSent = System.currentTimeMillis();

                while (!timerManager.isFinished()) {
                    long now = System.currentTimeMillis();

                    if (now - lastTimeSent >= 1000) {
                        sendHandler.broadcastTime();
                        lastTimeSent = now;
                    }

                    if (now - lastTrashSpawn >= TRASH_SPAWN_INTERVAL_MS) {
                        TrashDTO trash = trashManager.generateTrash(
                                screenSize.getMaxX(), screenSize.getMaxY());
                        sendHandler.broadcastTrash(trash);
                        lastTrashSpawn = now;
                    }

                    Thread.sleep(GAME_LOOP_INTERVAL_MS);
                }

                sendHandler.broadcastGameEnd();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void notifyGameReady(String playerId) {
        if (!readyPlayers.contains(playerId)) {
            readyPlayers.add(playerId);
        }
    }

    // 현재 필드에 있는 쓰레기 리스트 (필요하면 사용)
    public List<TrashDTO> getActiveTrashes() {
        return trashManager != null ? trashManager.getActiveTrashes()
                                    : Collections.emptyList();
    }

    public void registerRoom(GameRoom room) throws SQLException {
        // DB에 game_rooms INSERT
        int roomId = gameDAO.createGameRoom(
            room.getPlayer1Id(),
            room.getPlayer2Id()
        );

        if (roomId > 0) {
            room.setRoomId(roomId);
        } else {
            System.out.println("[registerRoom] roomId 생성 실패");
        }
    }


    public void updateScore(GameRoom room, String playerId, int score) {
        if (room == null || playerId == null) {
            return;
        }

        // 음수 방지
        if (score < 0) {
            score = 0;
        }

        String p1 = room.getPlayer1Id();
        String p2 = room.getPlayer2Id();

        // GameRoom 필드에 직접 반영
        if (p1 != null && p1.equals(playerId)) {
            room.setScore1(score);
        } else if (p2 != null && p2.equals(playerId)) {
            room.setScore2(score);
        }

        // ScoreManager도 같이 쓰고 싶다면 여기서 동기화
        if (scoreManager != null) {
            scoreManager.setScore(playerId, score);  // setScore 메서드가 없다면 ScoreManager 쪽에 하나 추가
        }
    }

    public void updateScore(String playerId, int score) {
        updateScore(this.room, playerId, score);
    }

    // 예전 인터페이스들(혹시 다른 곳에서 쓰고 있으면 그대로 유지)
    public int getScore(String playerId) {
        return (scoreManager != null) ? scoreManager.getScore(playerId) : 0;
    }

    public void addScore(String playerId, int delta) {
        if (scoreManager == null) return;

        scoreManager.addScore(playerId, delta);
        int newScore = scoreManager.getScore(playerId);

        for (GameClientHandler client : clients) {
            if (!client.getPlayerId().equals(playerId)) {
                client.send("SCORE_UPDATE|" + playerId + "|" + newScore);
            }
        }
    }

 
    public String determineWinner(GameRoom room) {
        int player1Score = room.getScore1();
        int player2Score = room.getScore2();

        if (player1Score > player2Score) {
            room.setWinnerId(room.getPlayer1Id());
            return room.getPlayer1Id();
        } else if (player2Score > player1Score) {
            room.setWinnerId(room.getPlayer2Id());
            return room.getPlayer2Id();
        } else {
            // 무승부
            room.setWinnerId(null);
            return null;
        }
    }

    public GameRoom getGameResults() {
        return room;
    }

  
    public void endGame(GameRoom room, String wId) throws SQLException {
        String winnerId  = wId;
        String player1Id = room.getPlayer1Id();
        String player2Id = room.getPlayer2Id();

        // 게임 기록 저장 (room_id, player1_id, score1, player2_id, score2, winner_id, started_at)
        gameDAO.endGameRoom(room);

        // 승자 / 패자 랭킹 점수 업데이트
        if (winnerId != null) {
            if (winnerId.equals(player1Id)) {
                String loserId = player2Id;
                gameDAO.updateRankingScore(winnerId, +10);
                gameDAO.updateRankingScore(loserId, -5);
            } else if (winnerId.equals(player2Id)) {
                String loserId = player1Id;
                gameDAO.updateRankingScore(winnerId, +10);
                gameDAO.updateRankingScore(loserId, -5);
            }
        }

        // 클라이언트에게 승자 브로드캐스트
        for (GameClientHandler client : clients) {
            client.send("WINNER|" + winnerId);
        }
    }

  
    public List<Ranking> getRankingList(int limit) throws SQLException {
        return gameDAO.getRankingList(limit);
    }
}