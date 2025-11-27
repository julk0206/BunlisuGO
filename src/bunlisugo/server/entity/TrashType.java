package bunlisugo.server.entity;

import bunlisugo.server.entity.TrashType;

public class TrashType {
    private int typeId;
    private String name;
    private String category;
    private String imagePath;

    public TrashType(int typeId, String name, String category, String imagePath) {
        this.typeId = typeId;
        this.name = name;
        this.category = category;
        this.imagePath = imagePath;
    }

    public TrashType(String name, String category, String imagePath) {
        this.name = name;
        this.category = category;
        this.imagePath = imagePath;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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

}