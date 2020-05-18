package server;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ArrayList<ServerThread>clientList = new ArrayList<ServerThread>();
    private HashMap<String, ObjectOutputStream> clientMap = new HashMap<String, ObjectOutputStream>(); // key : id , value : 출력스트림


    public Server(){
        try{

            serverSocket = new ServerSocket(11111);
            while (true){
                System.out.println("Server Start");
                socket = serverSocket.accept();
                String ipAddr = socket.getInetAddress().getHostAddress();
                System.out.println(ipAddr + " : Connected ");
                ServerThread serverThread = new ServerThread(clientList, clientMap,socket);
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
