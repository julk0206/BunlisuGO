package bunlisugo.server.controller;

import java.util.List;

import bunlisugo.server.dto.TrashDTO;
import bunlisugo.server.service.GameService;
import bunlisugo.server.service.TimerManager;

public class SendCommandHandler {
    private final GameService gameService;
    private final List<GameClientHandler> clients;
    private TimerManager timerManager;

    public SendCommandHandler(GameService gameService, List<GameClientHandler> clients, TimerManager timerManager) {
        this.gameService = gameService;
        this.clients = clients;
        this.timerManager = timerManager;
    }

    public void broadcastTime() {
        long remainingSec = timerManager.getRemainingTime() / 1000;

        for (GameClientHandler client : clients) {
            client.send("TIME_UPDATE|" + remainingSec);
        }
    }

    public void broadcastGameStart() {
        for (GameClientHandler client : clients) {
            client.send("GAME_START|");
        }
    }

    public void broadcastGameEnd() {
        for (GameClientHandler client : clients) {
            client.send("GAME_END|");
        }
    }

    public void broadcastTrash(TrashDTO trash) {
        String msg = String.format("TRASH|%s|%s|%s|%d|%d",
                trash.getName(),
                trash.getCategory(),
                trash.getImagePath(),
                trash.getX(),
                trash.getY());

        for (GameClientHandler client : clients) {
            client.send(msg);
        }
    }

}