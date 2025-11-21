package bunlisugo.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("10.240.71.28", 3328)) {
            System.out.println("서버에 연결됨!");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            //
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("[SERVER] " + line);
                    }
                } catch (Exception e) {
                    System.out.println("[SERVER]|CONNECTION TERMINATION");
                }
            }).start();

            // 사용자 입력 → 서버로 전송
            while (true) {
                System.out.print("입력: ");
                String msg = sc.nextLine();
                out.println(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
