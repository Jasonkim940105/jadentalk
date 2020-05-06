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
        while (true){
            try{
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                while (true){
                    String fromClient = br.readLine();
                    String str[] = fromClient.split("@");
                    if(str[0].equals("login")){
                        String loginData = str[1];
                        String arr[] = loginData.split(":");
                        // arr[0] = id , arr[1] = pw
                        if(DB.getUsers().size() != 0){
                            for(int i = 0 ; i < DB.getUsers().size(); i++){
                                if(arr[0].equals(DB.getUsers().get(i).getId())){
                                    if(arr[1].equals(DB.getUsers().get(i).getPw())){
                                        bw.write("okay"+"\n");
                                        bw.flush();
                                        System.out.println("okay flushed");
                                    }
                                } else{
                                    bw.write("no"+"\n");
                                    bw.flush();
                                    System.out.println("no flushed");
                                }
                            }
                        } else {
                            bw.write("no"+"\n");
                            bw.flush();
                            System.out.println("no flushed");
                        }
                    } else if(str[0].equals("join")){
                        String joinData = str[1];
                        System.out.println(joinData);
                        String arr[] = joinData.split(":");
                        User user = new User(arr[0],arr[1],arr[2]);
                        DB.addUser(user);
                        System.out.println("완료");
                    }
                }



            } catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }

    }
}
