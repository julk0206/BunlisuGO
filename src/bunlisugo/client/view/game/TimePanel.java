package bunlisugo.client.view.game;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimePanel extends JPanel {
    private JLabel timeLabel;   

    //화면에 붙을 시간 패널 
    public TimePanel() {
        // TimePanel 초기화 코드 작성
        setBackground(Color.MAGENTA);
        timeLabel = new JLabel("시간: 00:00"); //이건 언제 붙는거지?
        add(timeLabel);
    }

    //시간 갱신하는 것. 
    public void updateTimeLabel(String newTime){
        timeLabel.setText("남은 시간: " + newTime);
    }


}