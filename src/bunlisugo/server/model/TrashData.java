package bunlisugo.server.model;

import java.util.*;

public class TrashData {
    public static final Map<TrashType, List<Trash>> trashByType = new HashMap<>();
    private static final Random random = new Random();
    static {
        trashByType.put(TrashType.PAPER, Arrays.asList(
            new Trash("Newspaper", TrashType.PAPER, "/images/trash/paper/newspaper.png"),
            new Trash("PostIt", TrashType.PAPER, "/images/trash/paper/postit.png")
        ));
        trashByType.put(TrashType.PLASTIC, Arrays.asList(
            new Trash("Delivery Clean", TrashType.PLASTIC, "/images/trash/plastic/delivery_clean.png"),
            new Trash("Toothpaste", TrashType.PLASTIC, "/images/trash/plastic/toothpaste.png")
        ));
        trashByType.put(TrashType.GLASSCAN, Arrays.asList(
            new Trash("Beer", TrashType.GLASSCAN, "/images/trash/glasscan/beer.png"),
            new Trash("Soju", TrashType.GLASSCAN, "/images/trash/glasscan/soju.png"),
            new Trash("Aluminum Can", TrashType.GLASSCAN, "/images/trash/glasscan/aluminum_can.png")
        ));
        trashByType.put(TrashType.GENERAL, Arrays.asList(
            new Trash("Receipt", TrashType.GENERAL, "/images/trash/general/receipt.png"),
            new Trash("Delivery Dirty", TrashType.GENERAL, "/images/trash/general/delivery_dirty.png"),
            new Trash("Ceramic", TrashType.GENERAL, "/images/trash/general/cermaic.png"),
            new Trash("Broken Glass", TrashType.GENERAL, "/images/trash/general/broken_glass.png"),
            new Trash("Fruit Net", TrashType.GENERAL, "/images/trash/general/fruit_net.png")
        ));
    } 

    //랜덤 Trash 생성
    public static Trash randomTrash() {
        TrashType[] types = TrashType.values();
        TrashType randomType = types[random.nextInt(types.length)];

        List<Trash> trashes = trashByType.get(randomType);
        Trash trash = trashes.get(random.nextInt(trashes.size()));

        int x = random.nextInt(800);
        int y = random.nextInt(600);
        
        return new Trash (
            trash.getName(),
            trash.getType(),
            trash.getImagePath(),
            x,
            y,
            false
        );
    }
    
}