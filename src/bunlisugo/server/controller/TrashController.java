package bunlisugo.server.controller;

import java.util.*;

import bunlisugo.server.dao.TrashTypeDAO;
import bunlisugo.server.entity.TrashType;

public class TrashController {

    private final TrashTypeDAO trashDAO = new TrashTypeDAO();

    public List<TrashType> getInitialTrashTypes() {
        return trashDAO.getTrashTypes();
    }

}