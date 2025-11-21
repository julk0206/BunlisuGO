package bunlisugo.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("192.168.219.105", 3328)) {
            System.out.println("Connected to server..");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            while (true) {
                // 1) 메시지 보냄
                System.out.print("SEND: ");
                String msg = sc.nextLine();

                if (msg == null || msg.isBlank()) {
                    continue; // 빈 줄이면 그냥 다시 보냄
                }

                // 2) 서버로 보냄
                out.println(msg);

                // 3) 서버 응답 한 줄 받기 (응답이 올 때까지 여기서 기다림)
                String resp = in.readLine();
                if (resp == null) {
                    System.out.println("RECV: <SERVER CLOSED>");
                    break;
                }

                // 4) 받은 응답 출력
                System.out.println("RECV: " + resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
