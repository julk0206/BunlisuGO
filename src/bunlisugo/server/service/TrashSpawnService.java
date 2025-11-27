package bunlisugo.server.service;

import java.util.*;

import bunlisugo.server.dto.TrashDTO;
import bunlisugo.server.entity.TrashType;

public class TrashSpawnService {

    private final Random random = new Random();

    // 랜덤 쓰레기 생성
    public TrashDTO spawnTrash(List<TrashType> types, int maxX, int maxY) {
            TrashType selectedType = types.get(random.nextInt(types.size())); //랜덤 타입 선택

            // 랜덤 위치 설정
            int x = new Random().nextInt(maxX + 1);
            int y = new Random().nextInt(maxY + 1);

            TrashDTO randomTrash = new TrashDTO(selectedType, x, y, false); //쓰레기 객체 생성

            return randomTrash;
    }
}