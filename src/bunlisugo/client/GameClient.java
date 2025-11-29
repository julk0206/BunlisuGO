package bunlisugo.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bunlisugo.client.model.User;
import bunlisugo.client.view.HomeView;
import bunlisugo.client.view.LoginView;
import bunlisugo.client.view.MatchingView;
import bunlisugo.client.view.RankingView;

public class GameClient {

    private static GameClient instance;        // 싱글톤
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private User currentUser;
    private int lastScore = 0;
    public static final int RANKING_LIMIT = 10;
    
    // 화면 참조
    private LoginView loginView;
    private HomeView homeView;
    private MatchingView matchingView;
    private RankingView rankingView;

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
    
    public int getLastScore() {
        return lastScore;
    }

    public void setLastScore(int score) {
        this.lastScore = score;
    }
    
    public String getNickname() {
        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return null;
    }

    private GameClient() {
        try {
            socket = new Socket("192.168.219.105", 3328); //서버 컴퓨터의 
            System.out.println("Connected to server..");

            out = new PrintWriter(socket.getOutputStream(), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 서버 메시지를 "계속" 읽는 수신 스레드 (이 람다는 괜찮다고 했으니 그대로 둠)
            new Thread(() -> {
                try {
                    String resp;
                    while ((resp = in.readLine()) != null) {
                        System.out.println("RECV: " + resp);
                        handleServerMessage(resp);  
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
        } else {
            System.out.println("WARN: tried to send but output stream is null. msg=" + message);
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
                if (parts.length > 1) {
                    int waiting = Integer.parseInt(parts[1]);
                    if (matchingView != null) {
                        matchingView.onMatchWaiting(waiting);
                    }
                }
                break;

            case "MATCH_FOUND":
                if (matchingView != null) {
                    matchingView.onMatchFound();
                }
                break;
           
            case "RESULT":
                if (parts.length < 5) {
                    System.out.println("Invalid RESULT packet");
                    break;
                }

                String p1Name = parts[1];
                String p2Name = parts[3];

                int p1Score;
                int p2Score;

                try {
                    p1Score = Integer.parseInt(parts[2]);
                    p2Score = Integer.parseInt(parts[4]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid score format in RESULT");
                    break;
                }
                
                String myName = getNickname();
                if (myName == null) {
                    System.out.println("WARN: RESULT received but currentUser is null");
                    break;
                }

                // 내가 P1인지 P2인지 판단
                boolean iAmP1 = myName.equals(p1Name);

                int myScore    = iAmP1 ? p1Score : p2Score;
                int otherScore = iAmP1 ? p2Score : p1Score;

                setLastScore(myScore);  // 홈/랭킹에서 사용

                // 승패 텍스트
                String resultText;
                if (myScore > otherScore) {
                    resultText = "You WIN!";
                } else if (myScore < otherScore) {
                    resultText = "You LOSE!";
                } else {
                    resultText = "DRAW!";
                }

                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new bunlisugo.client.view.ResultView(
                            GameClient.this,
                            resultText,
                            myScore,
                            otherScore
                        );
                    }
                });

                break;
                
            case "RANKING_RES":
                List<String> items = new ArrayList<String>();

                for (int i = 1; i < parts.length; i++) {
                    items.add(parts[i]); // username,score
                }

                if (rankingView != null) {
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            rankingView.updateRanking(items);
                        }
                    });
                }
                break;

            default:
                System.out.println("Unknown command from server: " + cmd);
                break;
        }
    }
}
