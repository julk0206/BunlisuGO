package bunlisugo.client.model;
//Trash는 server/model 에 있어야할것같은데?
public class Trash {
    private String name;
    private TrashType type;
    private String imagePath;
    private int x;
    private int y;
    private boolean isCollected;

    public Trash(String name, TrashType type, String imagePath) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
    }

    public Trash(String name, TrashType type, String imagePath, int x, int y, boolean isCollected) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
        this.x = x;
        this.y = y;
        this.isCollected = isCollected;
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

}