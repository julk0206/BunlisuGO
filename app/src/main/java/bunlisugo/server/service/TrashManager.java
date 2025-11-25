package bunlisugo.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bunlisugo.server.model.Trash;

public class TrashManager {

    // 쓰레기 객체 관리
    private final List<Trash> activeTrashes = new ArrayList<>();
    private final TrashSpawnService TrashSpawnService = new TrashSpawnService();
    private final int MAX_TRASH = 20;

    // 게임 맵 크기
    private final int maxX;
    private final int maxY;

    public TrashManager(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    // 쓰레기 생성
    public void generateTrash() {
        if (activeTrashes.size() < MAX_TRASH) {
            Trash newTrash = TrashSpawnService.spawnTrash(maxX, maxY);
            activeTrashes.add(newTrash);
        }
    }

    // 쓰레기 수집 처리
    public boolean collectTrash(int playerId, String trashName) {
        for (Trash trash : activeTrashes) {
            if (trash.getName().equals(trashName) && !trash.isIsCollected()) {
                trash.setIsCollected(true);

                return true;
            }
        }
        return false;
    }

    // 분류된 쓰레기 제거 (필요 없을듯?)
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

}


