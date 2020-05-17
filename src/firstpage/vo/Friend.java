package firstpage.vo;

import java.io.Serializable;

public class Friend implements Serializable {
    private String mid;
    private String fid;
    private String states;


    public Friend(){}

    public Friend(String mid, String fid){
        this.mid = mid;
        this.fid = fid;
    }
    public Friend(String mid, String fid, String states){
        this.mid = mid;
        this.fid = fid;
        this.states = states;
    }


    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
