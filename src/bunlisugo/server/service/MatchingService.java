package bunlisugo.server.service;

import bunlisugo.server.model.GameRoom;
import bunlisugo.server.model.User;

import java.util.LinkedList;
import java.util.Queue;

public class MatchingService {

    private final Queue<User> waitingUsers = new LinkedList<>();

    // 1. 유저를 매칭 대기열에 넣기
    public void enqueue(User user) {
        waitingUsers.add(user);
    }

    // 2. 두 명 이상 있으면 방을 만들어서 반환
    public GameRoom match() {
        if (waitingUsers.size() < 2) {
            return null; // 아직 매칭 불가
        }

        User p1 = waitingUsers.poll();
        User p2 = waitingUsers.poll();

        // TODO: GameRoom 생성 로직은 나중에 GameRoom 설계하면서 채우기
        GameRoom room = new GameRoom(p1, p2);
        return room;
    }
    
    // 3. 매칭 취소 하고싶으
    public void cancel(User user) {
        waitingUsers.remove(user);
    }

    public int getWaitingCount() {
        return waitingUsers.size();
    }
}
