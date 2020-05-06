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
                btnCase(getData());
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
        String data = str[1];  //필요한 데이터
        switch (Integer.parseInt(str[0])) {
            case 1 : // 로그인 버튼
                String arr[] = data.split(":");
                // arr[0] = id , arr[1] = pw
                if (DB.getUsers().size() != 0) {
                    for (int i = 0; i < DB.getUsers().size(); i++) {
                        if (arr[0].equals(DB.getUsers().get(i).getId())) {
                            if (arr[1].equals(DB.getUsers().get(i).getPw())) {
                                loginOk();
                            }
                        } else {
                            loginNo();
                        }
                    }
                } else {
                    loginNo();
                }
                break;

            case 2: // 조인 버튼
                String brr[] = data.split(":");
                User user = new User(brr[0], brr[1], brr[2]);
                DB.addUser(user);
                System.out.println("완료");
                break;
        }
    }


    private void loginOk() throws IOException{
        bw.write("okay" + "\n");
        bw.flush();
    }

    private void loginNo() throws IOException{
        bw.write("no" + "\n");
        bw.flush();
    }


} // class



