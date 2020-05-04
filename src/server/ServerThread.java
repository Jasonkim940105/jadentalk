package server;

import firstpage.DB;
import firstpage.User;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{
    Socket socket;
    BufferedWriter bw;
    BufferedReader br;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try{
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true){
                String joinData = br.readLine();
                String arr[] = joinData.split(":");
                User user = new User(arr[0],arr[1],arr[2]);
                DB.addUser(user);
                System.out.println("완료");
            }



        } catch (IOException ioe){
            System.out.println(ioe.getMessage());
        }

    }
}
