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

            // 1) classpath 내에서 먼저 시도
            InputStream in = DBManager.class.getClassLoader().getResourceAsStream("db.properties");

            // 2) 못 찾으면 프로젝트 루트에서 직접 읽기
            if (in == null) {
                in = new FileInputStream("db.properties");
            }

            props.load(in);
            in.close();

            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");

            System.out.println("[DBManager] DB 설정 로드 완료");

        } catch (Exception e) {
            System.err.println("[DBManager] DB 설정 파일 로드 실패");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
