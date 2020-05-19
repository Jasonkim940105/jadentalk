package client.com;

import client.vo.AddFriend;
import client.vo.Friend;
import client.vo.Message;
import client.vo.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    private String id;
    private String pw;
    private String nick;
    private String email;
    private String state;
    private int protocol;
    private User user;
    private Friend friend;
    private AddFriend addFriend;
    private ArrayList<String> list;
    private String mId;
    private String fId;
    private Message message;
    private String myState;


    public Data(int protocol, AddFriend addFriend){
        this.protocol = protocol;
        this.addFriend = addFriend;
    }
    public Data(int protocol) {
        this.protocol =protocol;
    }
    public Data(int protocol, String id) {
        this.protocol = protocol;
        this.id =id;
    }
    public Data(int protocol, User user) {
        this.protocol = protocol;
        this.user = user;
    }
    public Data(int protocol, Friend friend){
        this.protocol = protocol;
        this.friend = friend;
    }
    public Data(int protocol, String id, String pw, String nick, String email, String state) {
        this.protocol = protocol;
        this.id = id;
        this.pw = pw;
        this.nick = nick;
        this.email = email;
        this.state = state;
    }
    public Data(int protocol, ArrayList<String>list ){
        this.protocol =protocol;
        this.list = list;
    }
    public Data(int protocol, String mId, String fId){
        this.protocol = protocol;
        this.mId = mId;
        this.fId = fId;

    }
    public Data(int protocol, Message message){
        this.protocol = protocol;
        this.message = message;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public AddFriend getAddFriend() {
        return addFriend;
    }

    public void setAddFriend(AddFriend addFriend) {
        this.addFriend = addFriend;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


}
