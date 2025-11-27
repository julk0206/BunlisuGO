package bunlisugo.server.model;

import bunlisugo.server.entity.GameSession;
import bunlisugo.server.entity.User;

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

	public User getPlayer1() {
		// TODO Auto-generated method stub
		return player1;
	}
	
	public User getPlayer2() {
		// TODO Auto-generated method stub
		return player2;
	}
}
