package bunlisugo.server.service;

import bunlisugo.server.entity.GameSession;

//게임 세션 하나의 상태 관리
public class GameSessionInstance { 
    private TrashManager trashManager;
	private ScoreManager scoreManager;
    private TimerManager timerManager;
	

    public GameSessionInstance(int maxX, int maxY, GameSession session) {
		this.trashManager = new TrashManager(maxX, maxY);
		this.scoreManager = new ScoreManager(session);
		this.timerManager = new TimerManager(60); // 60초 게임
	}


	public TrashManager getTrashManager() {
		return this.trashManager;
	}

	public void setTrashManager(TrashManager trashManager) {
		this.trashManager = trashManager;
	}

	public ScoreManager getScoreManager() {
		return this.scoreManager;
	}

	public void setScoreManager(ScoreManager scoreManager) {
		this.scoreManager = scoreManager;
	}

	public TimerManager getTimerManager() {
		return this.timerManager;
	}

	public void setTimerManager(TimerManager timerManager) {
		this.timerManager = timerManager;
	}

}

