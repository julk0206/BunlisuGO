package bunlisugo.server.service;

import java.util.*;

import bunlisugo.server.entity.TrashType;
import bunlisugo.server.dao.TrashTypeDAO;
import bunlisugo.server.dto.TrashDTO;

public class TrashManager {

    // 쓰레기 객체 관리
    private final List<TrashDTO> activeTrashes = new ArrayList<>();
    private final TrashSpawnService TrashSpawnService = new TrashSpawnService();
    private final int MAX_TRASH = 20;
    private TrashJudgeService trashJudgeService = new TrashJudgeService();

    // 게임 맵 크기 1200*750임 
    private final int maxX;
    private final int maxY;

    public TrashManager(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    // 쓰레기 생성
    public void generateTrash(List<TrashType> types) {
        if (activeTrashes.size() < MAX_TRASH) {
            TrashDTO newTrash = TrashSpawnService.spawnTrash(types, maxX, maxY);
            activeTrashes.add(newTrash);
        }

    }

    // 쓰레기 수집 처리
   /* public boolean collectTrash(int playerId, String trashName) {
        for (TrashDTO trash : activeTrashes) {
            if (trash.getName().equals(trashName) && !trash.isIsCollected()) {
                trash.setIsCollected(true);
                trash.setCollectorId(playerId);

                return true;
            }
        }
        return false;
    } */

    //살짝 수정함 
    public boolean collectTrash(int playerId, String trashName) {
        for (TrashDTO trash : activeTrashes) {
            if (trash.getName().equals(trashName) && !trash.isIsCollected()) { //아직 안 된 애들 중에서 

                boolean isInsideTrashBox =trashJudgeService.judgeTrashCollected(trash.getX(), trash.getY()) ; //박스패널 전체에 들어갔는지. 
                
                if(isInsideTrashBox){ //박스 안에 들어감
                
                    trash.setIsCollected(true);
                    trash.setCollectorId(playerId);

                return true; //이러면 collectTrash가 true반환 
                }
            }}
            return false;
        }
    




    // 분류된 쓰레기 제거 (필요 없을듯?)
    public void removeCollected() {
        Iterator<TrashDTO> iterator = activeTrashes.iterator();
        while (iterator.hasNext()) {
            TrashDTO trash = iterator.next();
            if (trash.isIsCollected()) {
                iterator.remove();
            }
        }
    }

    public List<TrashDTO> getActiveTrashes() {
        return activeTrashes;
    }


}

