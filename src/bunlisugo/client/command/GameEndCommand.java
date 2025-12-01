package bunlisugo.client.command;

import bunlisugo.client.GameClient;

public class GameEndCommand implements Command {
    @Override
    public void execute(String[] parts, GameClient client) {
        // 서버가 GAME_END는 알림용 → 클라이언트에서는 아무 처리 안함
    }
}
