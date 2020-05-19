package client.vo;

public class FriendList {
    private String id;
    private String states = "";

    public FriendList(){}
    public FriendList(String id, String states){
        this.id = id;
        this.states =states;
    }
    public FriendList(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }
}
