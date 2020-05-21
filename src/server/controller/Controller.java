package server.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import server.ServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller  {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ArrayList<ServerThread> clientList = new ArrayList<ServerThread>();
    private HashMap<String, ObjectOutputStream> clientMap = new HashMap<String, ObjectOutputStream>();
    private boolean isStop = false;

    Task task = new Task() {
        @Override
        protected Object call() throws Exception {
            serverStart();
            return null;
        }
    };

    @FXML
    public void btnServerStart(){
        Thread th = new Thread(task);
        th.start();
    }
    @FXML
    public void btnServerStop(){
        try {
            if(socket != null){
                socket.close();
            }
            if(serverSocket != null){
                serverSocket.close();
            }
            isStop = true;
            Platform.exit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serverStart(){
        try{
            serverSocket = new ServerSocket(11111);
            while (!isStop){
                System.out.println("Server Start");
                socket = serverSocket.accept();
                String ipAddr = socket.getInetAddress().getHostAddress();
                System.out.println(ipAddr + " : Connected ");
                ServerThread serverThread = new ServerThread(clientList,clientMap,socket);
                serverThread.start();
                clientList.add(serverThread);
                System.out.println(clientList.size());
            }
        } catch (IOException ioe){
            ioe.printStackTrace();

        }
    }

}
