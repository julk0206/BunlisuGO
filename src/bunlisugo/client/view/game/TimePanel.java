package bunlisugo.client.view.game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import bunlisugo.client.controller.GameController;

public class TimePanel extends JPanel {

    private JLabel timeLabel;
    private Timer timer;
    private int remainingSeconds;  // 남은 시간(초)

    private GameController controller;

    public void setGameController(GameController controller) {
        this.controller = controller;
    }

    public TimePanel() {
        setBackground(Color.MAGENTA);
        timeLabel = new JLabel("시간: 00:00");
        add(timeLabel);
    }

    // 게임 시작 시 컨트롤러에서 호출
    public void startTimer(int durationSeconds) {
        this.remainingSeconds = durationSeconds;

        // 혹시 이전 타이머 돌고 있으면 정지
        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingSeconds <= 0) {
                    timer.stop();
                    timeLabel.setText("시간: 00:00");

                    // 게임 종료 알림
                    if (controller != null) {
                        controller.onTimeOver();
                    }
                    return;
                }

                remainingSeconds--;
                int m = remainingSeconds / 60;
                int s = remainingSeconds % 60;
                timeLabel.setText(String.format("시간: %02d:%02d", m, s));
            }
        });

        timer.start();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}
