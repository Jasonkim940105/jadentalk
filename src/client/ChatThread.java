package client;

import client.com.Data;
import client.com.Protocol;
import client.vo.Message;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

public class ChatThread extends Thread {
    private ObjectInputStream ois;
    private TextArea taChatMain;
    private boolean isStop = false;


    public ChatThread(ObjectInputStream ois, TextArea taChatMain){
        this.ois = ois;
        this.taChatMain = taChatMain;
    }

    @Override
    public void run() {
        while (!isStop){
            try {
                Data data = (Data)ois.readObject();
                readCase(data);
            } catch (SocketException e){
                isStop = true;
                System.out.println("연결종료");
            } catch (IOException e) {
                isStop = true;
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
            case Protocol.MESSAGE_RECEIVE_REALTIME : {
                System.out.println(data.getMessage().getContents());
                String message = data.getMessage().getContents();
                taChatMain.appendText("["+ data.getMessage().getSend_id()+ "]"+ message+"\n");
                break;
            }
        }
    } // 분기
    private void showPreviousMessage(Data data) throws IOException{
        ArrayList<Message> messageList = new ArrayList<>();
        messageList = data.getList();
        Collections.sort(messageList);
        for(int i = 0 ; i < messageList.size(); i++){
            taChatMain.appendText("["+ messageList.get(i).getSend_id() +"] "  +messageList.get(i).getContents() +" :: " +  messageList.get(i).getTime()+"\n");
           /* if(ChatController.mid.equals(messageList.get(i).getSend_id())){ // 내가 보낸 메세지일 경우
                Text myText = new Text("["+ messageList.get(i).getSend_id() +"] "  +messageList.get(i).getContents() +" :: " +  messageList.get(i).getTime()+"\n");
                myText.setStyle("-fx-text-fill: #A3CCA2");
                taChatMain.appendText(myText.getText());
            } else {
                Text yourText = new Text("["+ messageList.get(i).getSend_id() +"] "  +messageList.get(i).getContents() +" :: " +  messageList.get(i).getTime()+"\n");
                yourText.setStyle("-fx-text-fill: #2e508d");
                taChatMain.appendText(yourText.getText());
            }*/
        }
    }  // 접속시 이전채팅 있을 시 보여줌
    private void previousMessageEmpty(Data data) throws IOException{ // 접속시 이전채팅 없을 시
        System.out.println("메세지 없음");
        return;
    }
}
