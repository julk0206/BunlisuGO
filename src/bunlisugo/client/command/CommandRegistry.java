package bunlisugo.client.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private static final Map<String, Command> commands = new HashMap<>();

    static {
    	commands.put("SIGNUP_OK", new SignupOkCommand());
    	commands.put("SIGNUP_FAIL", new SignupFailCommand());
    	commands.put("LOGIN_OK", new LoginOkCommand());
        commands.put("LOGIN_FAIL", new LoginFailCommand());
        commands.put("MATCH_WAITING", new MatchWaitingCommand());
        commands.put("MATCH_FOUND", new MatchFoundCommand());
        commands.put("COUNTDOWN", new CountdownCommand());
        commands.put("SCORE_UPDATE", new ScoreUpdateCommand());
        commands.put("TRASH", new TrashCommand());
        commands.put("TIME_UPDATE", new TimeUpdateCommand());
        commands.put("WINNER", new WinnerCommand());
        commands.put("GAME_END", new GameEndCommand());
        commands.put("RANKING_RES", new RankingResponseCommand());
    }

    public static Command getCommand(String cmd) {
        return commands.get(cmd);
    }
}