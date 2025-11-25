package bunlisugo.server.model;

import java.util.*;

public class TrashData {
    public static final Map<TrashType, List<Trash>> trashByType = new HashMap<>();
    private static final Random random = new Random();

    static {
        trashByType.put(TrashType.PAPER, Arrays.asList(
            new Trash("Newspaper", TrashType.PAPER, "/images/trash/paper/newspaper.png", 0, 0, false, 0),
            new Trash("PostIt", TrashType.PAPER, "/images/trash/paper/postit.png", 0, 0, false, 0)
        ));
        trashByType.put(TrashType.PLASTIC, Arrays.asList(
            new Trash("Delivery Clean", TrashType.PLASTIC, "/images/trash/plastic/delivery_clean.png", 0, 0, false, 0),
            new Trash("Toothpaste", TrashType.PLASTIC, "/images/trash/plastic/toothpaste.png", 0, 0, false, 0)
        ));
        trashByType.put(TrashType.GLASSCAN, Arrays.asList(
            new Trash("Beer", TrashType.GLASSCAN, "/images/trash/glasscan/beer.png", 0, 0, false, 0),
            new Trash("Soju", TrashType.GLASSCAN, "/images/trash/glasscan/soju.png", 0, 0, false, 0),
            new Trash("Aluminum Can", TrashType.GLASSCAN, "/images/trash/glasscan/aluminum_can.png", 0, 0, false, 0)
        ));
        trashByType.put(TrashType.GENERAL, Arrays.asList(
            new Trash("Receipt", TrashType.GENERAL, "/images/trash/general/receipt.png", 0, 0, false, 0),
            new Trash("Delivery Dirty", TrashType.GENERAL, "/images/trash/general/delivery_dirty.png", 0, 0, false, 0),
            new Trash("Ceramic", TrashType.GENERAL, "/images/trash/general/cermaic.png", 0, 0, false, 0),
            new Trash("Broken Glass", TrashType.GENERAL, "/images/trash/general/broken_glass.png", 0, 0, false, 0),
            new Trash("Fruit Net", TrashType.GENERAL, "/images/trash/general/fruit_net.png", 0, 0, false, 0)
        ));
    } 

    //랜덤 Trash 생성 (좌표, sessionID 아직 없음)
    public static Trash randomTrashTemplate() {
        // TrashType 중에서 랜덤 선택
        TrashType[] types = TrashType.values(); // enum의 모든 값 가져오기
        int typeIndex = random.nextInt(types.length); // 0~ types.Length-1 사이 난수
        TrashType randomType = types[typeIndex];

        // randomType에 해당하는 타입 리스트 가져오기
        List<Trash> trashes = trashByType.get(randomType);

        // 리스트에서 랜덤 Trash 선택
        int trashIndex = random.nextInt(trashes.size()); // 0 ~ trashes.size()-1 사이 난수
        Trash selectedTrash = trashes.get(trashIndex);
        
        return new Trash (
            selectedTrash.getTrashId(),
            selectedTrash.getName(),
            selectedTrash.getType(),
            selectedTrash.getImagePath(),
            0,
            0,
            false,
            selectedTrash.getSessionId()
        );
    }
    
}
