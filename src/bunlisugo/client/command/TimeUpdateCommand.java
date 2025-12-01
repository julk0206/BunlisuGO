package bunlisugo.client.command;

import bunlisugo.client.GameClient;

public class TimeUpdateCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        int time = Integer.parseInt(parts[1]);

        if (client.getTimePanel() != null) {
            client.getTimePanel().updateTime(time);
        }

        // 시간 0일 때 onTimeOver
        if (time == 0 && client.getGameController() != null) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                client.getGameController().onTimeOver();
            });
        }
    }
}
