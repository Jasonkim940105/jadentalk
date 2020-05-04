package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;

    public server(){
        try{
            serverSocket = new ServerSocket(11111);
            while (true){
                System.out.println("Server Start");
                socket = serverSocket.accept();
                String ipAddr = socket.getInetAddress().getHostAddress();
                System.out.println(ipAddr + " : Connected ");
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            }
        } catch (IOException ioe){
            ioe.printStackTrace();

        }
    }

    public static void main(String[] args) {
        new server();
    }
}
