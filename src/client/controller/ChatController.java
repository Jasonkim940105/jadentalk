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



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

//todo : 동시에 여러 메세지를 보내게 하려면..

    @FXML
    private  Label lbFriendId;
    @FXML
    private TextArea taChatMain;
    @FXML
    private TextField taSendbox;

    public static String mid = null;
    public static String fid = null;
    private Socket socket = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbFriendId.setText(fid);
        taChatMain.setEditable(false);
        try {
            socket = new Socket("localhost", 11111);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            ChatThread chatThread = new ChatThread(ois, taChatMain);
            //생성되면서 동시에 기존 글을 뿌려준다.
            Data data = new Data(Protocol.CHAT_START, mid, fid); //
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

        Data data = new Data(Protocol.MESSAGE_SEND, message);  //  서버로 메세지 전송
        try {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        taSendbox.setText("");
        taChatMain.appendText("["+mid+"]"+ " " + message.getContents()+"\n"); // 내 채팅화면에 append

    } // 메세지 보내기

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


}
