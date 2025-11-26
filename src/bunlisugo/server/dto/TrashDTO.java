package bunlisugo.server.dto;

import bunlisugo.server.model.Trash;

public class TrashDTO {
    public String name;
    public String type;
    public String imagePath;
    public int x;
    public int y;

    public TrashDTO(Trash t) {
        this.name = t.getName();
        this.type = t.getType().name();
        this.imagePath = t.getImagePath();
        this.x = t.getX();
        this.y = t.getY();
    }
}
