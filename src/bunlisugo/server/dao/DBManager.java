package bunlisugo.server.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBManager {

    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            Properties props = new Properties();

            InputStream in = DBManager.class.getClassLoader().getResourceAsStream("db.properties");
            if (in == null) {
                in = new FileInputStream("db.properties");
            }

            props.load(in);
            in.close();

            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");

            System.out.println("[DBManager] DB 설정 로드 완료: " + url);

            try {
                Class.forName("org.mariadb.jdbc.Driver");
                System.out.println("[DBManager] MariaDB JDBC 드라이버 로드 성공");
            } catch (ClassNotFoundException e) {
                System.err.println("[DBManager] MariaDB 드라이버 클래스를 찾을 수 없습니다.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("[DBManager] DB 설정 파일 로드 실패");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
