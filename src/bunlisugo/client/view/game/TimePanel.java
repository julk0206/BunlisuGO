package bunlisugo.client.view.game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class TimePanel extends JPanel {

    private JLabel timeLabel;

    public TimePanel() {
        setBackground(Color.MAGENTA);
        timeLabel = new JLabel("시간: 00:00");
        add(timeLabel);
    }

    public void updateTime(int remainingSeconds) {
        int remainingTime = remainingSeconds;  // 내부 값 갱신
        int m = remainingTime / 60;
        int s = remainingTime % 60;
        timeLabel.setText(String.format("시간: %02d:%02d", m, s));
    }


}