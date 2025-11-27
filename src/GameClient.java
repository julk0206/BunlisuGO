

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import bunlisugo.client.view.HomeView;
import bunlisugo.client.view.LoginView;

public class GameClient {

    private static GameClient instance;        // 싱글톤
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    // 로그인 화면 참조
    private LoginView loginView;
	private HomeView homeView;


    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }
    
    public void setHomeView(HomeView homeView) { 
        this.homeView = homeView;
    }


    private GameClient() {
        try {
            socket = new Socket("10.240.193.80", 3328);
            System.out.println("Connected to server..");

            out = new PrintWriter(socket.getOutputStream(), true); // 서버로 문자열을 보냄
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 서버가 보낸 메시지를 읽음

            // 서버 메시지를 "계속" 읽는 수신 스레드 생성
            new Thread(() -> {
                try {
                    String resp;
                    while ((resp = in.readLine()) != null) {
                        System.out.println("RECV: " + resp);
                        handleResponse(resp); // 응답 받아서 처리
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GameClient 객체를 싱글톤으로 만듦
    // 소켓 연결은 한 번만 열면 되고...여러 View가 모두 같은 GameClient를 공유하며 send()를 호출해야 함
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
    public void handleResponse(String resp) {
    	String[] parts = resp.split("\\|");
        String cmd = parts[0];
        
        if ("LOGIN_OK".equals(cmd)) {
            String username = parts.length > 1 ? parts[1] : "";
            if (loginView != null) {
                loginView.onLoginSuccess(username);
            }
        } else if ("LOGIN_FAIL".equals(cmd)) {
            String reason = parts.length > 1 ? parts[1] : "UNKNOWN";
            if (loginView != null) {
                loginView.onLoginFail(reason);
            }
        }
    }

    // 테스트용 main
    public static void main(String[] args) {
        GameClient client = GameClient.getInstance();
        
    }
}
