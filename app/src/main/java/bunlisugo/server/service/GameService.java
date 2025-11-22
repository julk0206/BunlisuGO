package bunlisugo.server.service;

import java.util.*;

import bunlisugo.server.model.Trash;
import bunlisugo.server.model.TrashData;

public class GameService {
    // 쓰레기 생성 및 관리 로직

    private final List<Trash> activeTrashes = new ArrayList<>();
    private final int MAX_TRASH = 20;

    // 랜덤 쓰레기 생성
    public void generateTrash() {
        if (activeTrashes.size() < MAX_TRASH) {
            Trash newTrash = TrashData.randomTrash();
            activeTrashes.add(newTrash);
        }
    }

    // 쓰레기 수집 처리
    public boolean collectedTrash(String trashName) {
        for (Trash trash : activeTrashes) {
            if (trash.getName().equals(trashName) && !trash.isIsCollected()) {
                trash.setIsCollected(true);
                return true;
            }
        }
        return false;
    }
    
    // 분류된 쓰레기 제거
    public void removeCollected() {
        Iterator<Trash> iterator = activeTrashes.iterator();
        while (iterator.hasNext()) {
            Trash trash = iterator.next();
            if (trash.isIsCollected()) {
                iterator.remove();
            }
        }
    }
    
    public List<Trash> getActiveTrashes() {
        return activeTrashes;
    }

    // 게임 루프에서 주기적 호출하기
    public void update() {
        generateTrash();
        removeCollected();
    }
}

