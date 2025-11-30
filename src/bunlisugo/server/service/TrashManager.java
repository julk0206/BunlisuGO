package bunlisugo.server.service;

import java.util.*;

import bunlisugo.server.entity.TrashType;
import bunlisugo.server.dao.TrashTypeDAO;
import bunlisugo.server.dto.TrashDTO;

public class TrashManager {

    // 쓰레기 객체 관리
    private final List<TrashDTO> activeTrashes = new ArrayList<>();
    private final Random random = new Random();
    private TrashTypeDAO trashTypeDAO = new TrashTypeDAO();
    private final int MAX_TRASH = 20;

    // 게임 맵 크기 1200*750임 
    private final int maxX;
    private final int maxY;

    private final List<TrashType> trashTypes;

    public TrashManager(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.trashTypes = trashTypeDAO.getTrashTypes();
    }

    // 랜덤 쓰레기 생성
    public TrashDTO generateTrash(int maxX, int maxY) {
            TrashType selectedType = trashTypes.get(random.nextInt(trashTypes.size())); //랜덤 타입 선택


            // 랜덤 위치 설정
            int x = new Random().nextInt(maxX + 1);
            int y = new Random().nextInt(maxY + 1);

            TrashDTO randomTrash = new TrashDTO(selectedType, x, y, false); //쓰레기 객체 생성

            return randomTrash;
    }

    public List<TrashDTO> getActiveTrashes() {
        return activeTrashes;
    }


}
