package bunlisugo.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import bunlisugo.server.controller.GameClientHandler;
import bunlisugo.server.service.LoginService;

//GameServer.java
public class GameServer {
 public static final int PORT = 3328;
 private static final Logger logger = Logger.getLogger(GameServer.class.getName());

 public static void main(String[] args) {
     ServerSocket serverSocket = null;

     LoginService sharedLoginService = new LoginService();

     try {
         String hostAddress = InetAddress.getLocalHost().getHostAddress();

         serverSocket = new ServerSocket();
         serverSocket.bind(new InetSocketAddress(hostAddress, PORT));

         logger.info("Server started on " + hostAddress + " : " + PORT);
         logger.info("Waiting for clients...");

         while (true) {
             Socket socket = serverSocket.accept();
             logger.info("Client connected: " + socket.getRemoteSocketAddress());

             GameClientHandler handler = new GameClientHandler(socket, sharedLoginService);
             handler.start();
         }

     } catch (IOException e) {
         e.printStackTrace();
     } finally {
         if (serverSocket != null) {
             try { serverSocket.close(); } catch (IOException ignored) {}
         }
     }
 }
}

