package bunlisugo.server.service;

import java.sql.SQLException;

import bunlisugo.server.dao.UserDAO;
import bunlisugo.server.entity.User;
import bunlisugo.server.util.PasswordUtil;

public class SignupService {

	private final UserDAO userDAO = new UserDAO();

	public boolean signup(String username, String pw) {
		System.out.println("Signup called with username = " + username);
		validateInput(username, pw);
		checkDuplicate(username);
		String hashedPw = hashPassword(pw); //해시는 필드로 두면 안 됨
		User user = buildUser(username, hashedPw);
		System.out.println("User 객체 생성 완료");
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
		 User existing = userDAO.getUserByUsername(username);
		 if (existing != null) {
		     throw new IllegalArgumentException("Username already exists");
		 }
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
		System.out.println("Saving user: " + user.getUsername());
	    return userDAO.createUser(user);
	}

}