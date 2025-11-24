package Client.View;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginView {

private JFrame frame;
private JTextField NameField;
private JTextField PasswordField;


	public LoginView() {
        frame = new JFrame("Login View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        LoginButton.addActionListener(e -> {
            // 로그인 버튼 클릭 시 동작
            // 예: 로그인 처리 및 홈 화면으로 전환
            frame.dispose(); // 현재 로그인 뷰 닫기
            new HomeView(); // 홈 뷰 열기 (HomeView 클래스가 있다고 가정)
        });
		frame.getContentPane().add(LoginButton);
		
		
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
}