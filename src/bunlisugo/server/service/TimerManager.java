package bunlisugo.server.service;

import java.time.Instant;

public class TimerManager {

    private final long gameDuration; 
    private Instant startTime;

    public TimerManager(long seconds) {
        this.gameDuration = seconds * 1000; // milliseconds
        this.startTime = null;
    }

    public void startTimer() {
        // 게임 시작 시간 기록
        this.startTime = Instant.now();
    }

    // 남은 시간 조회
    public long getRemainingTime() {
        long passed = Instant.now().toEpochMilli() - startTime.toEpochMilli();
        long remaining = gameDuration - passed;
        return Math.max(remaining, 0);
    }

    public boolean isFinished() {
        return getRemainingTime() <= 0;
    }

}