package bunlisugo.repository;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.*;

import bunlisugo.server.model.User;
import bunlisugo.server.repository.DBManager;
import bunlisugo.server.repository.GameDAO;
import bunlisugo.server.repository.UserDAO;
import bunlisugo.server.util.PasswordUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTest {
    private static final UserDAO userDAO = new UserDAO();
    private static final GameDAO gameDAO = new GameDAO();

    private String testUsername;
    private static final String TEST_PASSWORD = "password123";
    private int testUserId;

    @BeforeEach
    void setup() throws Exception {

        testUsername = "testuser_" + UUID.randomUUID();
        
        try {

            Connection conn = DBManager.getConnection();
            System.out.println("Connected to: " + conn.getMetaData().getURL());

            String hashed = PasswordUtil.hashPassword(TEST_PASSWORD);

            User testUser = new User(
                0,
                testUsername,
                hashed,
                0,
                java.time.LocalDateTime.now()
            );

            // 사용자 생성
            boolean created = userDAO.createUser(testUser);
            System.out.println("User creation: " + (created ? "Success" : "Failed"));
            assertTrue(created, "User creation should succeed");

            // 생성된 사용자 ID 가져오기
            User retrieved = userDAO.getUserByUsername(testUsername);
            assertNotNull(retrieved, "Retrieved user should not be null");
            testUserId = retrieved.getUserId();
            assertTrue(testUserId > 0, "User ID should be assigned");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void testUserRetrieval() throws Exception {
        User retrieved = userDAO.getUserByUsername(testUsername);
        assertNotNull(retrieved, "Retrieved user should not be null");
        assertEquals(testUsername, retrieved.getUsername(), "Usernames should match");
        System.out.println("Retrieved user id: " + retrieved.getUserId());
    }

    @Test
    @Order(2)
    void testGameSessionCreation() throws Exception {
        int sessionId = gameDAO.createGameSession(testUserId, testUserId); // self-match 예시
        assertTrue(sessionId > 0, "Game session should be created");
        System.out.println("Created game session id: " + sessionId);
    }

    @Test
    @Order(3)
    void testRankingUpdate() throws Exception {
        boolean updated = userDAO.updateRankingScore(testUserId, 100);
        assertTrue(updated, "Ranking update should succeed");
    }

}
