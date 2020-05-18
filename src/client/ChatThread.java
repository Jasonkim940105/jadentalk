package client;

import client.com.Data;
import client.com.Protocol;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ChatThread extends Thread {
    private ObjectInputStream ois;
    private TextArea taChatMain;

    public ChatThread(ObjectInputStream ois, TextArea taChatMain){
        this.ois = ois;
        this.taChatMain = taChatMain;
    }

    @Override
    public void run() {
        try {
            Data data = (Data)ois.readObject();
            readCase(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //들어온 데이터 짤라줄 메소드
    private void readCase(Data data) throws IOException{
        int protocol = data.getProtocol();
        switch (protocol){
            case Protocol.PREVIOUS_MESSAGE_EXIST : {
                // 화면에 뿌려주기는 메소드
                showPreviousMessage(data);
                break;
            }
            case Protocol.PRRVIOUS_MESSAGE_EMPTY : {
                previousMessageEmpty(data);
                break;
            }

            case Protocol.TEST : {
                System.out.println(" *** " + data.getMessage());
                break;
            }
        }
    }

    private void showPreviousMessage(Data data) throws IOException{
        ArrayList<String> messageList = new ArrayList<>();
        messageList = data.getList();
        for(int i = 0 ; i < messageList.size(); i++){
            taChatMain.appendText(messageList.get(i)+ "\n");
        }
    }
    private void previousMessageEmpty(Data data) throws IOException{
        System.out.println("메세지 없음");
        return;
    }
}
