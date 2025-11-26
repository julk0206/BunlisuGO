package bunlisugo.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import bunlisugo.server.repository.*;
import bunlisugo.server.socket.*;
public class GameServer {
	public static final int PORT = 3328; //나중에 알아서 수정
	private static final Logger logger = Logger.getLogger(GameServer.class.getName());

	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		// DB 연결 초기화
		try {
			DBManager.getConnection();
			logger.info("DB 연결 성공");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "DB 연결 초기화 실패", e);
			return;
		}

		try {
			//내 서버 ip 가져오기
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(hostAddress, PORT)); //서버가 어느 주소/포트에서 듣고 있을지 정하는 코드
			
			logger.info("Server started on " + hostAddress + " : " + PORT);
            logger.info("Waiting for clients...");
			
			while(true) {
				//클라이언트가 들어올 때 까지 기다림
				Socket socket = serverSocket.accept();
				logger.info("Client connected: " + socket.getRemoteSocketAddress());
				
				//1클라 1스레드(여러 명의 플레이어가 동시에 게임을 플레이할 수 있음)
				GameClientHandler handler = new GameClientHandler(socket);
                handler.start();
			}
		}catch (IOException e){
			e.printStackTrace();
			logger.log(Level.SEVERE, "Failed to start server");
		}finally {
			//일단 끝나면 서버 문 닫기
			if (serverSocket != null) {
		        try { 
		        	serverSocket.close(); 
		        	} catch (IOException ignored) {
		        		
		        	}
		    }
		}
	
	}
	
}
