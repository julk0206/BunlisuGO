package bunlisugo.dao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import bunlisugo.server.model.User;
import bunlisugo.server.repository.DBManager;
import bunlisugo.server.repository.GameDAO;
import bunlisugo.server.repository.UserDAO;
import bunlisugo.server.util.PasswordUtil;

public class DAOTest {
    private static final UserDAO userDAO = new UserDAO();
    private static final GameDAO gameDAO = new GameDAO();

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "password123";
    private static int TEST_USER_ID = -1; // DB에 저장 후 할당될 ID

    public static void main(String[] args) {
        try {

            Connection conn = DBManager.getConnection();
            System.out.println("Connected to: " + conn.getMetaData().getURL());

            String hashed = PasswordUtil.hashPassword(TEST_PASSWORD);

            User testUser = new User(
                0,
                TEST_USERNAME,
                hashed,
                0,
                java.time.LocalDateTime.now()
            );

            // 사용자 생성 테스트
            boolean created = userDAO.createUser(testUser);
            System.out.println("User creation: " + (created ? "Success" : "Failed"));
            assertTrue(created);

            // 사용자 조회 테스트
            TEST_USER_ID = userDAO.getUserByUsername(TEST_USERNAME).getUserId();
            System.out.println("User retrieval: " + (TEST_USER_ID != -1 ? "Success" : "Failed"));

            // 게임 세션 생성 테스트
            int sessionCreated = gameDAO.createGameSession(TEST_USER_ID, TEST_USER_ID);
            System.out.println("Game session creation: " + (sessionCreated));
            assertTrue(sessionCreated > 0);

            // 랭킹 업데이트
            boolean rankingUpdated = userDAO.updateRankingScore(TEST_USER_ID, 100);
            System.out.println("Ranking update: " + (rankingUpdated ? "Success" : "Failed"));
            assertTrue(rankingUpdated);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testUserCreation() throws Exception {

        System.out.println("=== Start DAO Test ===");

        Connection conn = DBManager.getConnection();
        System.out.println("Connected to: " + conn.getMetaData().getURL());

        String hashed = PasswordUtil.hashPassword(TEST_PASSWORD);

        User testUser = new User(
            0,
            TEST_USERNAME,
            hashed,
            0,
            java.time.LocalDateTime.now()
        );

        boolean created = userDAO.createUser(testUser);
        System.out.println("User creation: " + created);
        assertTrue(created);

        User retrieved = userDAO.getUserByUsername(TEST_USERNAME);
        assertNotNull(retrieved);
        System.out.println("Retrieved user id: " + retrieved.getUserId());
    }

}
