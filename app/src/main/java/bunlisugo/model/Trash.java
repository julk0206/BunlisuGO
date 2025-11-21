package bunlisugo.model;

public class Trash {
    private String name;
    private TrashType type;
    private String imagePath;

    public Trash(String name, TrashType type, String imagePath) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
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

}
