package client.vo;

import oracle.sql.DATE;

import java.io.Serializable;

public class AddFriend implements Serializable {
    private String sendId;
    private String receiveId;
    private DATE sendTime;
    private String readStatus;

    public AddFriend(){}

    public AddFriend(String sendId, String receiveId){
        this.sendId = sendId;
        this.receiveId = receiveId;
    }

    public AddFriend(String sendId, String receiveId, DATE sendTime, String readStatus) {
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.sendTime = sendTime;
        this.readStatus = readStatus;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public DATE getSendTime() {
        return sendTime;
    }

    public void setSendTime(DATE sendTime) {
        this.sendTime = sendTime;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}
