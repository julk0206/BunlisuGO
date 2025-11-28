package bunlisugo.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import bunlisugo.client.view.HomeView;
import bunlisugo.client.view.LoginView;
import bunlisugo.client.view.MatchingView;

public class GameClient {

    private static GameClient instance;        // 싱글톤
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    // 화면 참조
    private LoginView loginView;
    private HomeView homeView;
    private MatchingView matchingView;
    
    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }
    
    public void setHomeView(HomeView homeView) { 
        this.homeView = homeView;
    }
    
    public void setMatchingView(MatchingView matchingView) {  
        this.matchingView = matchingView;
    }

    private GameClient() {
        try {
            socket = new Socket("10.240.63.25", 3328);
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
                matchingView.onMatchFound();
                break;

        
        }
    }

    // 테스트용 main (안 써도 됨)
    public static void main(String[] args) {
        GameClient client = GameClient.getInstance();
    }
}
