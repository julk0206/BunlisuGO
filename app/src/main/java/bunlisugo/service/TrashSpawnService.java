package bunlisugo.service;

import java.util.*;

import bunlisugo.model.Trash;
import bunlisugo.model.TrashData;

public class TrashSpawnService {
    // 쓰레기 생성 및 관리 로직

    private final List<Trash> activeTrashes = new ArrayList<>();
    private final int maxTrashCount = 20;

    public void generateTrash() {
        if (activeTrashes.size() < maxTrashCount) {
            Trash newTrash = TrashData.randomTrash();
            activeTrashes.add(newTrash);
        }
    }
    
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

    public void update() {
        generateTrash();
        removeCollected();
    }
}

