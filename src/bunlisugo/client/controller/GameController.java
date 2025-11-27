package bunlisugo.client.controller;

import java.util.List;
import java.sql.SQLException;

import bunlisugo.server.dao.TrashDAO;
import bunlisugo.server.model.Trash;

public class GameController {
    private TrashDAO trashDAO;

    public GameController() {
        this.trashDAO = new TrashDAO();
    }   
    public List<Trash> getRandomTrashes(int count) throws SQLException {
        return trashDAO.findRandom(count);
    }   

}