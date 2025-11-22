package bunlisugo.server.service;

import java.sql.SQLException;

import bunlisugo.server.model.User;
import bunlisugo.server.repository.UserDAO;
import bunlisugo.server.util.PasswordUtil;

public class SignupService {

	private final UserDAO userDAO = new UserDAO();

	public boolean signup(String username, String pw) {
		validateInput(username, pw);
		hashPassword(pw);
		
		String hashedPw = hashPassword(pw); //해시는 필드로 두면 안 됨
		User user = buildUser(username, hashedPw);
		
		return saveUser(user);
		

		// 5. userDAO.createUser(user) 호출
		
	}

	private void validateInput(String username, String pw) {
		// 1. 입력 검증(null, 빈 문자열 등)
		if (username == null || pw == null) {
			throw new IllegalArgumentException("Username or password is null");
		}
				
		if (username.trim().isEmpty() || pw.trim().isEmpty()) {
			throw new IllegalArgumentException("Username or password is empty");
		}
		
	}
	
	private void checkDuplicate(String username) {
		// 2. 아이디 중복 체크(이미 db에 있는지)
    }
	

	private String hashPassword(String pw) {
		// 주어진 비밀번호를 bcrypt로 해싱하여 반환
		return PasswordUtil.hashPassword(pw);
	}
	
	private User buildUser(String username, String hashedPw) {
        User user = new User(username, hashedPw);
        return user;
    }
	
	private boolean saveUser(User user) {
	    try {
	        return userDAO.createUser(user);
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	        return false;
	    }
	}

}
