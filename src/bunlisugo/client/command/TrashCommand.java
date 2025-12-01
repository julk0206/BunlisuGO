package bunlisugo.client.command;

import bunlisugo.client.GameClient;

public class TrashCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        // 서버 포맷: TRASH|name|category|imagePath|x|y
        if (parts.length < 6 || client.getGameController() == null) return;

        String trashName = parts[1];
        String category = parts[2];
        String imagePath = parts[3];
        int x = Integer.parseInt(parts[4]);
        int y = Integer.parseInt(parts[5]);

        javax.swing.SwingUtilities.invokeLater(() -> {
            client.getGameController().spawnTrash(trashName, category, imagePath, x, y);
        });
    }
}
