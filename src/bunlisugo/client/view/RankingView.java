package bunlisugo.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import bunlisugo.client.GameClient;

public class RankingView {

private JFrame frame;
private GameClient client;
private String username;
private int finalScore;
    
	public RankingView(GameClient client, String username, int finalScore) {
    this.client = client;
    this.username = username;
    this.finalScore = finalScore;

    frame = new JFrame("Ranking View");
    frame.setBounds(100,100, 1200, 750);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initialize();
    frame.setVisible(true);
}


    private void initialize() {
        // Initialization
        frame.getContentPane().setLayout(null); 

        //돌아가기 버튼
        JButton backButton = new JButton("돌아가기");
        backButton.setBounds(38, 35, 158, 64);
        backButton.addActionListener(e -> {
            frame.dispose();
            new HomeView(client);
        });
        frame.getContentPane().add(backButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(391, 130, 401, 517);
		frame.getContentPane().add(scrollPane);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(387, 35, 401, 69);
		frame.getContentPane().add(lblNewLabel);

        
    }
}