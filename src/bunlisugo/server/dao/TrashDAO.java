package bunlisugo.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bunlisugo.server.model.Trash;

public class TrashDAO {

    // DB에서 랜덤으로 쓰레기 N개 가져오기
    public List<Trash> findRandom(int count) throws SQLException {
        String sql =
            "SELECT name, category, image_path " +
            "FROM trash_types " +
            "ORDER BY RAND() LIMIT ?";

        List<Trash> list = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, count);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String name       = rs.getString("name");
                    String category   = rs.getString("category");   // 예전 type 컬럼
                    String imagePath  = rs.getString("image_path");

                    Trash trash = new Trash(name, category, imagePath);
                    System.out.println(imagePath);

                    list.add(trash);
                }
            }
        }
        return list;
    }
}
