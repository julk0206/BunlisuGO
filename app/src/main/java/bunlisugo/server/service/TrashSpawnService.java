package bunlisugo.server.service;

import java.util.Random;

import bunlisugo.server.model.Trash;
import bunlisugo.server.model.TrashData;

public class TrashSpawnService {

    // 랜덤 쓰레기 생성
    public Trash spawnTrash(int maxX, int maxY) {
            Trash trash = TrashData.randomTrashTemplate();

            // 랜덤 위치 설정
            int randomX = new Random().nextInt(maxX + 1);
            int randomY = new Random().nextInt(maxY + 1);
            trash.setX(randomX);
            trash.setY(randomY);

            return trash;
    }
}
