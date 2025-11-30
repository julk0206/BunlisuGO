package bunlisugo.server.dto;

import bunlisugo.server.entity.TrashType;

public class TrashDTO {
    private String name;
    private String category;
    private String imagePath;
    private int x;
    private int y;
    
        public TrashDTO(TrashType type, int x, int y, boolean isCollected) {
        this.name = type.getName();
        this.category = type.getCategory();
        this.imagePath = type.getImagePath();
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}