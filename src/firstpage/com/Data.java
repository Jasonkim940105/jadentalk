package firstpage.com;

import firstpage.vo.User;

import java.io.Serializable;

public class Data implements Serializable {
    private String id;
    private String pw;
    private String nick;
    private String email;
    private String state;
    private int protocol;
    private User user;

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

    public Data(int protocol, String id, String pw, String nick, String email, String state) {
        this.protocol = protocol;
        this.id = id;
        this.pw = pw;
        this.nick = nick;
        this.email = email;
        this.state = state;
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
}
