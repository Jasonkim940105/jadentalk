package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ArrayList<ServerThread>clientList = new ArrayList<ServerThread>();




    public Server(){
        try{
            serverSocket = new ServerSocket(11111);
            while (true){
                System.out.println("Server Start");
                socket = serverSocket.accept();
                String ipAddr = socket.getInetAddress().getHostAddress();
                System.out.println(ipAddr + " : Connected ");
                ServerThread serverThread = new ServerThread(clientList, socket);
                serverThread.start();
                clientList.add(serverThread);
                System.out.println(clientList.size());

            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
