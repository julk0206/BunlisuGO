package bunlisugo.sevice;

import bunlisugo.model.User;
import bunlisugo.repository.UserDAO;
import bunlisugo.util.PasswordUtil;

public class LoginService {

    private final UserDAO userDAO = new UserDAO();

    public boolean login(String username, String password) throws Exception {

        // 1. DB에서 유저 정보 조회
        User user = userDAO.getUserByUsername(username); 
        // SQLException 발생하면 그냥 위로 던짐 → 서버에서 처리

        if (user == null) {
            return false;   // 아이디 없음
        }

        // 2. 비밀번호 검증
        boolean ok = PasswordUtil.verifyPassword(password, user.getPasswordHash());
        if (!ok) {
            return false;   // 비밀번호 틀림
        }

        // 3. (추후) 세션/중복 로그인 체크

        return true;  // 로그인 성공
    }

    public void logout(String username) {
        // 나중에 SessionManager 나오면 구현
    }
}
