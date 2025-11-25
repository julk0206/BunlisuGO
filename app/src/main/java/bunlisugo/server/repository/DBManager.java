package bunlisugo.server.repository;

import java.sql.*;
import java.sql.DriverManager;


public class DBManager {

    private static final String url = "jdbc:mariadb://localhost:3306/trashgame?serverTimezone=UTC";
    private static final String username = "stdUser";
    private static final String password = "wkvmtlf2";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


}
