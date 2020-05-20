package server;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Server extends Application {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ArrayList<ServerThread>clientList = new ArrayList<ServerThread>();
    private HashMap<String, ObjectOutputStream> clientMap = new HashMap<String, ObjectOutputStream>(); // key : id , value : 출력스트림


    @Override
    public void start(Stage stage) throws Exception {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("fxml/Server.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("server");
            stage.show();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
/*

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
*/
/*public static void main(String[] args) {
        new Server();
    }*/
}
