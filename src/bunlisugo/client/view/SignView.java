package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SignView {

private JFrame frame;
private JTextField signNameTextField;
private JTextField signPasswordTextField;
private JTextField checkPasswordTextField;
	
	public SignView() {
        frame = new JFrame("Sign View");
        frame.setBounds(100,100, 1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        // Initialization
        frame.getContentPane().setLayout(null); 

		JLabel signNameLabel = new JLabel("닉네임");
		signNameLabel.setBounds(223, 145, 176, 66);
		frame.getContentPane().add(signNameLabel);
		
		JLabel signPasswordLabel = new JLabel("비밀번호");
		signPasswordLabel.setBounds(223, 243, 176, 66);
		frame.getContentPane().add(signPasswordLabel);
		
		JLabel checkPasswordLabel = new JLabel("비밀번호 확인");
		checkPasswordLabel.setBounds(223, 345, 176, 66);
		frame.getContentPane().add(checkPasswordLabel); 


        
		//닉네임 입력
		signNameTextField = new JTextField();
		signNameTextField.setColumns(10);
		signNameTextField.setBounds(430, 243, 323, 66);
		frame.getContentPane().add(signNameTextField);
		
        //비밀번호 입력
		signPasswordTextField = new JTextField();
		signPasswordTextField.setColumns(10);
		signPasswordTextField.setBounds(430, 146, 323, 66);
		frame.getContentPane().add(signPasswordTextField);
		
		//비밀번호 확인 입력
		checkPasswordTextField = new JTextField();
		checkPasswordTextField.setColumns(10);
		checkPasswordTextField.setBounds(430, 345, 323, 66);
		frame.getContentPane().add(checkPasswordTextField);
		
		//회원가입 버튼
		JButton signButton = new JButton("회원가입완료");
		signButton.setBounds(481, 465, 221, 66);
		signButton.addActionListener(e -> {
			// 회원가입 버튼 클릭 시 동작
			// 예: 회원가입 처리 및 로그인 화면으로 전환
			frame.dispose(); // 현재 회원가입 뷰 닫기
			new LoginView(); // 로그인 뷰 열기 (LoginView 클래스가 있다고 가정)
		});	

		frame.getContentPane().add(signButton);




        //안내 라벨
		JLabel lblNewLabel = new JLabel("사용할 수 있는 아이디"); //근데 DB 랑 확인해야 돼서 아직 미완성
		lblNewLabel.setBounds(779, 164, 279, 29);
		frame.getContentPane().add(lblNewLabel);
		

        //비밀번호 일치 여부 
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(779, 358, 279, 29);
		frame.getContentPane().add(lblNewLabel_1);  

        
    }
}