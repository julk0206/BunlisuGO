package bunlisugo.server.service;

import bunlisugo.server.controller.GameClientHandler;
import bunlisugo.server.entity.GameRoom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MatchingService {

    private final Queue<GameClientHandler> waitingClients = new LinkedList<>();
    // 방ID -> 해당 방에 속한 클라이언트 리스트
    private final Map<Integer, List<GameClientHandler>> clientsMap = new HashMap<>();


    // 1. 유저를 매칭 대기열에 넣기
    public void enqueue(GameClientHandler player) {
        waitingClients.add(player);
    }

    // 2. 두 명 이상 있으면 방을 만들어서 반환
    public GameRoom match() {
        if (waitingClients.size() < 2) {
            return null; // 아직 매칭 불가
        }

        GameClientHandler p1 = waitingClients.poll();
        GameClientHandler p2 = waitingClients.poll();

        GameRoom room = new GameRoom(p1.getPlayerId(), p2.getPlayerId());

        clientsMap.put(room.getRoomId(), Arrays.asList(p1, p2));


        return room;
    }
    
    // 3. 매칭 취소 하고싶으
    public void cancel(GameClientHandler player) {
        waitingClients.remove(player);
    }

    public int getWaitingCount() {
        return waitingClients.size();
    }

    // 방 ID로 클라이언트 리스트 가져오기
    public List<GameClientHandler> getClientsInRoom(int roomId) {
        return clientsMap.get(roomId);
    }
    
}