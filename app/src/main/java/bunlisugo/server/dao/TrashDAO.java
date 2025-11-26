package bunlisugo.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bunlisugo.server.model.Trash;
import bunlisugo.server.repository.DBManager;



public class TrashDAO {

    //DB에서 랜덤으로 쓰레기 10개 가져오기  
   public List<Trash> findRandom(int count) throws SQLException {
        String sql = "SELECT name, type, image_path " +
                     "FROM trashes ORDER BY RAND() LIMIT ?";
        List<Trash> list = new ArrayList<>();
        Trash trash =null;
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, count);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    String image_path = rs.getString("image_path");
                    trash = new Trash(name, type, image_path);
                    System.out.println(image_path);

                    list.add(trash);        
                }
            }
        }
        return list;
    }
}