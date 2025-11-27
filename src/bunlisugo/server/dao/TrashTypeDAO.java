package bunlisugo.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import bunlisugo.server.entity.TrashType;

public class TrashTypeDAO {

    public List<TrashType> getTrashTypes() {
        List<TrashType> typeList = new ArrayList<>();

        String sql = "SELECT * FROM trash_types";

        try(Connection conn = DBManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TrashType trashType = new TrashType(
                        rs.getInt("type_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getString("image_path")
                    );
                    typeList.add(trashType);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return typeList;
    }

}