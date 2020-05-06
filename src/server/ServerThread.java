package server;

import firstpage.DB;
import firstpage.User;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{
    Socket socket;
    BufferedWriter bw;
    BufferedReader br;

    public ServerThread(Socket socket) throws IOException{
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        while (true){
            try{
                String[]str = getData();
                btnCase(str);
            } catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    }
// 1 로그인 2 조인

    private String[] getData() throws IOException{
        String fromClient = br.readLine();
        String str[] = fromClient.split("@");
        return str;
    }

    private void btnCase(String[] str) throws IOException {
        switch (Integer.parseInt(str[0])) {

            case 1:
                String loginData = str[1];
                String arr[] = loginData.split(":");
                // arr[0] = id , arr[1] = pw
                if (DB.getUsers().size() != 0) {
                    for (int i = 0; i < DB.getUsers().size(); i++) {
                        if (arr[0].equals(DB.getUsers().get(i).getId())) {
                            if (arr[1].equals(DB.getUsers().get(i).getPw())) {
                                bw.write("okay" + "\n");
                                bw.flush();
                                System.out.println("okay flushed");
                            }
                        } else {
                            bw.write("no" + "\n");
                            bw.flush();
                            System.out.println("no flushed");
                        }
                    }
                } else {
                    bw.write("no" + "\n");
                    bw.flush();
                    System.out.println("no flushed");
                }
                break;
            case 2:
                String joinData = str[1];
                System.out.println(joinData);
                String brr[] = joinData.split(":");
                User user = new User(brr[0], brr[1], brr[2]);
                DB.addUser(user);
                System.out.println("완료");
                break;
        }
    }

    }



