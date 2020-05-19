package client.vo;

import oracle.sql.DATE;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable ,Comparable<Message>{
    private String send_id;
    private String receive_id;
    private DATE send_time;
    private String contents;
    private String readStatus;
    private Timestamp time;

    @Override
    public int compareTo(Message message) {
        return this.time.compareTo(message.time);
    }

    public Message(){}

    public Message(String send_id, String receive_id, String contents, Timestamp time){
        this.send_id = send_id;
        this.receive_id = receive_id;
        this.contents= contents;
        this.readStatus = "x";
        this.time = time;
    }
    public Message(String send_id, String receive_id, Timestamp timestamp){
        this.send_id = send_id;
        this.receive_id = receive_id;
        this.time = timestamp;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getReceive_id() {
        return receive_id;
    }

    public void setReceive_id(String receive_id) {
        this.receive_id = receive_id;
    }

    public DATE getSend_time() {
        return send_time;
    }

    public void setSend_time(DATE send_time) {
        this.send_time = send_time;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
