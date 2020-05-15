package firstpage.vo;

import java.io.Serializable;

public class Friend implements Serializable {
    private String mid;
    private String fid;

    public Friend(){}

    public Friend(String mid, String fid){
        this.mid = mid;
        this.fid = fid;
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
