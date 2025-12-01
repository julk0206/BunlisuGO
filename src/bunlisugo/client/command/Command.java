package bunlisugo.client.command;

import bunlisugo.client.GameClient;

public interface Command {
    void execute(String[] parts, GameClient client);
}
