package bunlisugo.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bunlisugo.client.controller.GameController;
import bunlisugo.client.model.User;
import bunlisugo.client.view.HomeView;
import bunlisugo.client.view.LoginView;
import bunlisugo.client.view.MatchingView;
import bunlisugo.client.view.RankingView;
import bunlisugo.client.view.game.GameScorePanel;
import bunlisugo.client.view.game.TimePanel;

public class GameClient {

    private static GameClient instance;        // 싱글톤
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private User currentUser;

    public static final int RANKING_LIMIT = 10;
    
    // 화면 참조
    private LoginView loginView;
    private HomeView homeView;
    private MatchingView matchingView;
    private RankingView rankingView;

    private GameScorePanel gameScorePanel;
    private TimePanel timePanel;
    private List<String> rankList = new ArrayList<>();
    private GameController gameController = new GameController(null);
    
    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }
    
    public void setHomeView(HomeView homeView) { 
        this.homeView = homeView;
    }
    
    public void setMatchingView(MatchingView matchingView) {  
        this.matchingView = matchingView;
    }

    public void setRankingView(RankingView rankingView) {
        this.rankingView = rankingView;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setGameScorePanel(GameScorePanel panel) {
        this.gameScorePanel = panel;
    }

    public void setTimePanel(TimePanel panel) {
        this.timePanel = panel;
    }

    private GameClient() {
        try {
            socket = new Socket("10.240.34.57", 3328); //서버 컴퓨터의 
            System.out.println("Connected to server..");

            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 서버 메시지를 "계속" 읽는 수신 스레드
            new Thread(() -> {
                try {
                    String resp;
                    while ((resp = in.readLine()) != null) {
                        System.out.println("RECV: " + resp);
                        handleServerMessage(resp);   // ⭐ 여기 이름 수정!
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 싱글톤
    public static GameClient getInstance() {
        if (instance == null) {
            instance = new GameClient();
        }
        return instance;
    }

    public void send(String message) {
        if (out != null) {
            out.println(message);
            out.flush();
        }
    }
    
    // 서버 응답 처리
    private void handleServerMessage(String line) {
        String[] parts = line.split("\\|");
        String cmd = parts[0];

        switch (cmd) {
            case "LOGIN_OK":
                if (loginView != null) {
                    String username = parts.length > 1 ? parts[1] : "";
                    currentUser = new User();
                    currentUser.setUsername(username);
                    loginView.onLoginSuccess(username);
                }
                break;

            case "LOGIN_FAIL":
                if (loginView != null) {
                    String reason = (parts.length > 1) ? parts[1] : "알 수 없는 오류";
                    loginView.onLoginFail(reason);
                }
                break;
            case "MATCH_WAITING":
                int waiting = Integer.parseInt(parts[1]);
                matchingView.onMatchWaiting(waiting);
                break;

            case "MATCH_FOUND":
                String opponentName = parts[1]; // 서버에서 전달받은 상대 이름
                matchingView.onMatchFound(opponentName);
                break;

            case "SCORE_UPDATE":
                String playerId = parts[1];
                int score = Integer.parseInt(parts[2]);
                gameScorePanel.updateOpponentScore(score); // TODO: 구현 필요
                break;

            case "TRASH":
                String name = parts[1];
                String category = parts[2];
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                // TODO:Trash 업데이트.생성 메서드 게임 화면에 추가
                break;
            
            case "TIME_UPDATE":
                int time = Integer.parseInt(parts[1]);
                timePanel.updateTime(time); // TODO: 구현 필요 - 게임룸에다가 쓰세요(타이머)
                break;

                // 안 써도 될듯 GameStart
            case "GAME_START":
                // TODO: 게임 시작 메서드 게임 화면에 추가
                break;
            
            case "GAME_END":
                // TODO: 게임 업데이트.생성 메서드 게임 화면에 추가 - 최종 점수 날리기
                break;
            
            case "COUNTDOWN":
                int count = Integer.parseInt(parts[1]);
                // TODO: 게임룸에다가 쓰세요(타이머) (3~1까지 1초 간격으로 보냄 -> 화면 중앙에 countdown panel 추가해서 표시하다가 1까지 표시 후 없애기)
                break;
            
            case "WINNER":
                // TODO: 최종 결과 및 승자 표시 (승자 ID 전달하니 본인 ID와 같으면 승리 표시하는 메서드 구현)
                String winnerId = parts[1];

                gameController.showResult(winnerId); 
                break;
            
            case "RANK":
                // RANK|ERROR
                if (parts.length == 2 && parts[1].equals("ERROR")) {
                    System.out.println("랭킹 조회 실패");
                    break;
                }

                // RANK|username|score
                String username = parts[1];
                String rankingScore = parts[2];

                rankingView.addRanking(username, rankingScore);

                break;
        }
    }

    // 테스트용 main (안 써도 됨)
    public static void main(String[] args) {
        GameClient client = GameClient.getInstance();
    }
}