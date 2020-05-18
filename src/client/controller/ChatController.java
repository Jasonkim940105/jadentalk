package client.controller;

import client.ChatThread;
import client.com.Data;
import client.com.Protocol;
import client.vo.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

public class ChatController implements Initializable {


    @FXML
    private  Label lbFriendId;
    @FXML
    private TextArea taChatMain;
    @FXML
    private TextField taSendbox;


    public static String mid = null;
    public static String fid = null; // 다른방법이 없을까?
    private Socket socket = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private BufferedReader br;
    String sendData;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbFriendId.setText(fid);
        taChatMain.setEditable(false);
        try {
            socket = new Socket("localhost", 11111);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            ChatThread chatThread = new ChatThread(ois, taChatMain);
            //먼저 생성되면서 동시에 기존 글을 긁어와야함 그치?
            Data data = new Data(Protocol.CHAT_START, mid, fid);
            oos.writeObject(data);
            chatThread.start();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnSendClick(){
        String contents = taSendbox.getText();
        Date utilDate = new Date(System.currentTimeMillis());
        Timestamp time = new Timestamp(utilDate.getTime());
        Message message = new Message(mid, fid, contents, time);
        System.out.println("보낸시간 : " + time);

        Data data = new Data(Protocol.MESSAGE_SEND, message);
        try {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        taSendbox.setText("");
        taChatMain.appendText(message.getContents()+"\n");

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


}
