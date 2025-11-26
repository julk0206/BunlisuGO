package bunlisugo.server.model;

public class Trash {
    private int trashId;
    private String name;
    private TrashType type;
    private String imagePath;
    private int x;
    private int y;
    private boolean isCollected;
    private int sessionId;

    //DB 저장/조회용 생성자
    public Trash(int trashId, String name, TrashType type, String imagePath, int x, int y, boolean isCollected, int sessionId) {
        this.trashId = trashId;
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
        this.x = x;
        this.y = y;
        this.isCollected = isCollected;
        this.sessionId = sessionId;
    }

    // 게임 내 생성용 생성자
    public Trash(String name, TrashType type, String imagePath, int x, int y, boolean isCollected, int sessionId) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
        this.x = x;
        this.y = y;
        this.isCollected = isCollected;
        this.sessionId = sessionId;
    }

    public int getTrashId() {
        return this.trashId;
    }

    public void setTrashId(int trashId) {
        this.trashId = trashId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrashType getType() {
        return this.type;
    }

    public void setType(TrashType type) {
        this.type = type;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isIsCollected() {
        return this.isCollected;
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    public int getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

}