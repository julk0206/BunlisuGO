package bunlisugo.client.view.game;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class CountdownPanel extends JPanel {

    private JLabel textLabel;

    public CountdownPanel() {
        // 전체 화면 덮도록 투명 배경
        setOpaque(false);
        setLayout(new GridBagLayout());  // 중앙 정렬
        
        textLabel = new JLabel("");
        textLabel.setFont(new Font("Arial", Font.BOLD, 120));
        textLabel.setForeground(Color.GREEN);
        add(textLabel);
    }

    public void updateCountdown(int sec) {
        if (sec > 0) {
            textLabel.setText(String.format("%d", sec));
            setVisible(true);
        } else {
            textLabel.setText("");
            setVisible(false);
        }
        revalidate(); // UI 갱신될 때마다 다시 그림
        repaint();
    }
}
