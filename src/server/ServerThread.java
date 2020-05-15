package server;

import firstpage.com.Data;
import firstpage.com.Protocol;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;


public class ServerThread extends Thread {
    Socket socket;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    ArrayList<ServerThread> clientList;

    public ServerThread(ArrayList<ServerThread>clientList, Socket socket) throws IOException {
        this.clientList = clientList;
        this.socket = socket;
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        conn = JdbcUtil.getConnection();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Data data = (Data)ois.readObject();
                btnCase(data);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void btnCase(Data data) throws  IOException{
        int protocol = data.getProtocol();
        switch (protocol){
            case Protocol.LOGIN: {
                loginClick(data);
                break;
            }
            case Protocol.LOGIN_SUCCESS:{
                loginSuccess(data);
                break;
            }
            case Protocol.LOGOUT: {
                logOut(data);
                break;
            }
            case Protocol.JOIN: {
                joinClick(data);
                break;
            }
            case Protocol.ID_CHECK: {
                checkIdClick(data);
                break;
            }
            case Protocol.ADD_ID: {
                addFriend(data);
                break;
            }
            case Protocol.FRIEND_REQUEST_TO_ME: {
                //Listview 띄어줄 정보 전달메소드
                requestToMe(data);
                break;
            }
        }

    }
    private void checkIdClick(Data data) throws IOException{
        String sql = "SELECT ID FROM usertable WHERE ID = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getId());
            rs = pstmt.executeQuery();
            if(rs != null && !rs.isBeforeFirst()){ // DB에 데이터가 없는경우
                data.setProtocol(Protocol.ID_UNUSING);
                oos.writeObject(data);
            } else {
                data.setProtocol(Protocol.ID_USINGNOW);
                oos.writeObject(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pstmt);
        }

    } // 아이디 중복체크
    private void joinClick(Data data) throws IOException{
        try {
            pstmt = conn.prepareStatement("INSERT INTO usertable(ID, PW, EMAIL, LOGINSTATUS) values (?, ?, ?, ?)");
            pstmt.setString(1, data.getUser().getId());
            pstmt.setString(2, data.getUser().getPw());
            pstmt.setString(3, data.getUser().getEmail());
            pstmt.setString(4, "x");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // 중복아이디 접근시, 처리
            e.printStackTrace();
        }finally {
            JdbcUtil.close(pstmt);
        }

    } // 회원가입 버튼 클릭
    private void loginClick(Data data) throws  IOException{
        String id = data.getUser().getId();
        String pw = data.getUser().getPw();
        try{
            String sql =  "SELECT ID, PW FROM usertable WHERE ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getUser().getId());
            rs = pstmt.executeQuery();
            if(rs != null && ! rs.isBeforeFirst()){
                loginNo();
            } else {
                while (rs != null && rs.next()){
                    String myP = rs.getString("PW");
                    if(pw.equals(myP)){
                        loginOk(data);
                    } else
                        loginNo();
                }
            }
        } catch (SQLException sqle ){
            sqle.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pstmt);
        }
    } // 로그인 버튼 클릭
    private void loginOk(Data data) throws IOException{
        data.setProtocol(Protocol.LOGIN_OK);
        oos.writeObject(data);
    } // 로그인 가능
    private void logOut(Data data) throws  IOException{
        String sql = "UPDATE usertable set loginstatus = ? where id = ?";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "x");
            pstmt.setString(2, data.getUser().getId());
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
        }
        oos.writeObject(data);
    } // 로그아웃 기능
    private void loginNo() throws  IOException{
        Data data = new Data(Protocol.LOGIN_NO);
        oos.writeObject(data);
    } // 로그인 불가능
    private void loginSuccess(Data data) throws IOException{
        String sql = "UPDATE usertable set loginstatus = ? where id = ?";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getUser().getLoginStatus());
            pstmt.setString(2, data.getUser().getId());
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
        }
        oos.writeObject(data);
    } // 로그인 가능시, 클라이언트에게 데이터 던져줌
    private void addFriend(Data data) throws IOException{
        String sql = "SELECT ID FROM usertable WHERE ID = ?";  //친구추가할 아이디가 DB에 있는지확인
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getAddFriend().getReceiveId());
            rs = pstmt.executeQuery();
            if(rs != null && !rs.isBeforeFirst()){ // usertable에 fid 가 없는경우
                data.setProtocol(Protocol.FRIEND_ID_NOT_EXIST);
                oos.writeObject(data);
            } else{
                //  fid 가 있으면, addfriendtb 에추가
                    String sql2 = "INSERT INTO ADDFRIENDTB(sendid, receiveid) values(?,?)";
                    pstmt = conn.prepareStatement(sql2);
                    pstmt.setString(1, data.getAddFriend().getSendId());
                    pstmt.setString(2, data.getAddFriend().getReceiveId());
                    pstmt.executeUpdate();
                    data.setProtocol(Protocol.FRIEND_REQUEST_SUCCESS);
                    oos.writeObject(data);
            }
        } catch (SQLException e) {
            data.setProtocol(Protocol.FRIEND_REQUEST_FAIL);
            oos.writeObject(data);
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pstmt);
        }

    } // 친구추가 요청
    private void requestToMe(Data data) throws IOException{
        String sql = "SELECT sendid FROM addfriendtb WHERE receiveid = ? ";
        ArrayList<String> requestList = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getId());
            rs = pstmt.executeQuery();
            while (rs.next()){
                requestList.add(rs.getString(1));
            }
            if(requestList.size() != 0){ // 나에게 친구요청이 하나라도 온 경우
                data.setProtocol(Protocol.FRIEND_LIST_SHOW);
                data.setRequestList(requestList);
                oos.writeObject(data);
            } else {
                data.setProtocol(Protocol.FRIEND_LIST_EMPTY);
                oos.writeObject(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
        }

    } // 나에게 온 친구요청



} // class



