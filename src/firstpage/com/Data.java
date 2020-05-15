package firstpage.com;

import firstpage.vo.AddFriend;
import firstpage.vo.Friend;
import firstpage.vo.User;

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
    private ArrayList<String> requestList;

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
    public Data(int protocol, ArrayList<String>requestList ){
        this.protocol =protocol;
        this.requestList = requestList;
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

    public ArrayList<String> getRequestList() {
        return requestList;
    }

    public void setRequestList(ArrayList<String> requestList) {
        this.requestList = requestList;
    }
}
