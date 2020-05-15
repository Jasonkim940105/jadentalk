package firstpage.vo;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String pw;
    private String nick;
    private String email;
    private String state;
    private String loginStatus;


    public User(){

    }

    public User(String id, String pw, String email){
        this.id = id;
        this.pw = pw;
        this.email = email;
    }

    public User(String id, String pw){
        this.id = id;
        this.pw = pw;
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

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}
