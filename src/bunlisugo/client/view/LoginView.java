package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bunlisugo.client.controller.GameClient;

public class LoginView {

private JFrame frame;
private JTextField NameField;
private JTextField PasswordField;
private final GameClient client; // Main에서 넘겨준 GameClient를 보관하기 위해...


	public LoginView(GameClient client) {
		this.client = client; // 필드에 저장     
	    this.client.setLoginView(this); // 자기 자신 등록
	    
        frame = new JFrame("Login View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameClient.getInstance().setLoginView(this); // 자기 자신을 GameClient에 등록
        
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        // Initialization
        frame.getContentPane().setLayout(null); 

        JLabel 분리수GO = new JLabel("New label");
		분리수GO.setBounds(411, 91, 348, 120);
		frame.getContentPane().add(분리수GO);
		
        //아이디 라벨   
		JLabel NameMessageLabel = new JLabel("닉네임");
		NameMessageLabel.setBounds(223, 243, 176, 66);
		frame.getContentPane().add(NameMessageLabel);
		
        //비밀번호 라벨 
		JLabel PasswordMessageLabel = new JLabel("비밀번호");
		PasswordMessageLabel.setBounds(223, 345, 176, 66);
		frame.getContentPane().add(PasswordMessageLabel);
		
        //아이디 입력창
		NameField = new JTextField();
		NameField.setBounds(430, 243, 323, 66);
		frame.getContentPane().add(NameField);
		NameField.setColumns(10);
		
        //비밀번호 입력창
		PasswordField = new JTextField();
		PasswordField.setColumns(10);
		PasswordField.setBounds(430, 345, 323, 66);
		frame.getContentPane().add(PasswordField);
		
        //로그인 버튼
		JButton LoginButton = new JButton("로그인");
		LoginButton.setBounds(481, 465, 221, 66);
		frame.getContentPane().add(LoginButton);
		
        LoginButton.addActionListener(e -> {
            // 로그인 버튼 클릭 시 동작
        	// username과 비밀번호를 입력한 곳에서 텍스트를 뽑아 가져옴
        	 String username = NameField.getText();
        	 String pw = PasswordField.getText();
        	 
        	 // 서버 프로토콜 형식에 맞춰서 작성하여 서버에 로그인 요청 보내기
        	 client.send("LOGIN|" + username + "|" + pw); // 여기서도 받은 client 사용            // 여기서 화면 전환 X(서버가 LOGIN_OK를 줄 때 전환한다.)
        });
		
		
		
		//회원가입 버튼
		JButton SignButton = new JButton("계정이 없으신가요? 회원가입 하러가기");
		SignButton.setBounds(430, 549, 323, 45);
        SignButton.addActionListener(e -> {
            // 회원가입 버튼 클릭 시 동작
            // 예: 회원가입 화면으로 전환
            frame.dispose(); // 현재 로그인 뷰 닫기
            new SignView(); // 회원가입 뷰 열기 (SignView 클래스가 있다고 가정)
        });
		frame.getContentPane().add(SignButton);

		
        
    }
    
    public void onLoginSuccess(String username) {
        // 로그인 성공 시 화면 전환 및 성공했다고 팝업 띄움
        JOptionPane.showMessageDialog(frame, "로그인 성공: " + username);
        frame.dispose();
        new HomeView(); 
    }

    public void onLoginFail(String reason) {
        // 실패 메시지를 팝업으로 띄움
        JOptionPane.showMessageDialog(frame, "로그인 실패: " + reason);
    }
}