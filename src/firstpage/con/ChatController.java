package firstpage.con;

import firstpage.com.ChatStart;
import firstpage.com.Data;
import firstpage.com.Protocol;
import firstpage.vo.Message;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbFriendId.setText(fid);
        taChatMain.setEditable(false);
        try {
            socket = new Socket("localhost", 11111);
            oos = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnSendClick(){
        String contents = taSendbox.getText();
        Message message = new Message(mid, fid, contents);
        Data data = new Data(Protocol.MESSAGE_SEND, message);
        try {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        taSendbox.setText("");
        taChatMain.appendText(message.getContents()+"\n");

    }


}
