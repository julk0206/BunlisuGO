package bunlisugo.server.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import bunlisugo.server.dao.UserDAO;
import bunlisugo.server.model.*;
import bunlisugo.server.repository.*;
import bunlisugo.server.util.*;

public class LoginService {

    private final UserDAO userDAO = new UserDAO();
    //로그인 된 유저를 담는 set
    private final Set<String> loggedInUsers =  Collections.synchronizedSet(new HashSet<>());

    public boolean login(String username, String pw) throws Exception {

    	// 0. 이미 로그인 된 유저인지
    	if(loggedInUsers.contains(username)) {
    		throw new IllegalStateException("[LOGIN FAIL] SAME_ID_EXISTS");
    	}
    	
        // 1. DB에서 유저 정보 조회
        User user = userDAO.getUserByUsername(username); 
        // SQLException 발생하면 그냥 위로 던짐 → 서버에서 처리

        if (user == null) {
            return false;   // 아이디 없음
        }

        // 2. 비밀번호 검증
        boolean ok = PasswordUtil.verifyPassword(pw, user.getPasswordHash());
        if (!ok) {
            return false;   // 비밀번호 틀림
        }
        
        // 3. 로그인 성공
        loggedInUsers.add(username);
        return true; 
    }

    public void logout(String username) {
        // 나중에 SessionManager 나오면 구현
    }
}
