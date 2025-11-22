package bunlisugo.server.model;

public class GameRoom {
    private int roomId;
    private User player1;
    private User player2;
    private GameSession session;
    
    public GameRoom() {
    	
    }
    
    public GameRoom(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
    }
}

