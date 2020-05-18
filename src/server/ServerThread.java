package server;

import client.com.Data;
import client.com.Protocol;


import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class ServerThread extends Thread {
    Socket socket;
    String user_id;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    boolean isStop = false;

    ArrayList<ServerThread> clientList;
    HashMap<String, ObjectOutputStream> clientMap;


    public ServerThread(ArrayList<ServerThread>clientList, HashMap<String, ObjectOutputStream> clientMap, Socket socket) throws IOException {
        this.clientList = clientList;
        this.clientMap = clientMap;
        this.socket = socket;
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        conn = JdbcUtil.getConnection();
    }

    @Override
    public void run() {
        while (!isStop) {
            try {
                Data data = (Data)ois.readObject();
                btnCase(data);
            } catch (IOException e) { // 연결 끊기면
                clientList.remove(this);
                isStop = true;
                System.out.println(socket.getInetAddress() + " 종료되었습니다");
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
            case Protocol.FRIEND_REQUEST_ACCEPT : {
                // 작업처리
                acceptFriend(data);
                break;
            }
            case Protocol.FRIEND_REQUEST_REFUSE : {
                refuseFriend(data);
                // 거절 작업처리
                break;
            }
            case Protocol.FRIEND_LIST_SHOW : {
                // friendlist 뿌려주는 작업 해야한다.
                showFriendList(data);
                break;
            }
            case Protocol.MESSAGE_SEND :{
                // db에 올려야함
                sendMessage(data);
                break;
            }
            case Protocol.CHAT_START : {
                System.out.println("채팅시작");
                System.out.println("내 아이디 : "  + data.getmId());
                System.out.println("친구 아이디 : "  + data.getfId());
                user_id = data.getmId();
                synchronized (clientMap){
                    clientMap.put(user_id, oos);
                }
                System.out.println("hashmap size : " + clientMap.size());
                System.out.println("채팅에 접속한 유저이름의 아웃풋 스트림 : " + clientMap.get(user_id));
                showPreviousMessage(data);
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
                    String sql2 = "INSERT INTO ADDFRIENDTB(sendid, receiveid, readstatus) values(?,?,?)";
                    pstmt = conn.prepareStatement(sql2);
                    pstmt.setString(1, data.getAddFriend().getSendId());
                    pstmt.setString(2, data.getAddFriend().getReceiveId());
                    pstmt.setString(3, "x");
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
        String sql = "SELECT sendid FROM addfriendtb WHERE receiveid = ? and readstatus = ? ";
        ArrayList<String> requestList = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getId());
            pstmt.setString(2, "x");
            rs = pstmt.executeQuery();
            while (rs.next()){
                requestList.add(rs.getString(1));
            }
            if(requestList.size() != 0){ // 나에게 친구요청이 하나라도 온 경우
                data.setProtocol(Protocol.FRIEND_REQUEST_LIST_SHOW);
                data.setList(requestList);
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
    private void acceptFriend(Data data) throws IOException{
        String sql = "insert into friendtable(my_id, friend_id) values(?, ?)";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getmId());
            pstmt.setString(2, data.getfId());
            pstmt.executeUpdate();
            pstmt.setString(1, data.getfId());
            pstmt.setString(2,data.getmId());
            pstmt.executeUpdate();
            data.setProtocol(Protocol.FRIED_REQUEST_ACCEPT_OK);
            oos.writeObject(data);
            String sql2 = "update addfriendtb set readstatus = ? where sendid = ?";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, "o");
            pstmt.setString(2, data.getfId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            data.setProtocol(Protocol.FRIED_REQUEST_ACCEPT_NO);
            oos.writeObject(data);
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
        }

    } // 친구요청수락
    private void refuseFriend(Data data) throws IOException{
        String sql = "update addfriendtb set readstatus = ? where sendid = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "o");
            pstmt.setString(2, data.getfId());
            pstmt.executeUpdate();
            oos.writeObject(data);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
        }

    } // 친구요청거절
    private void showFriendList(Data data) throws  IOException {
        String sql =  "SELECT friend_id from friendtable where my_id = ? ";
        ArrayList<String> frinedList = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getId());
            rs = pstmt.executeQuery();
            while (rs.next()){
               frinedList.add(rs.getString("friend_id"));
            }
            if(frinedList.size() != 0 ){
                data.setProtocol(Protocol.FRIEND_LIST_SHOW_EXIST);
                data.setList(frinedList);
                oos.writeObject(data);
            } else {
                data.setProtocol(Protocol.FRIEND_LIST_SHOW_EMPTY);
                oos.writeObject(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pstmt);
        }

    } // 친구목록 보여주기
    private void sendMessage(Data data)throws IOException{
        String sql = "INSERT INTO message(send_id, receive_id, message_contents, readstatus, sendtime) values(?, ?, ?, ?, ?)";
        Timestamp timestamp = data.getMessage().getTime();
        String send_id = data.getMessage().getSend_id();
        String receive_id = data.getMessage().getReceive_id();
        String message = data.getMessage().getContents();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, send_id);
            pstmt.setString(2,receive_id);
            pstmt.setString(3, message);
            pstmt.setString(4,data.getMessage().getReadStatus());
            pstmt.setTimestamp(5, timestamp);
            pstmt.executeUpdate();

            clientMap.get(receive_id).writeObject(new Data(Protocol.TEST, message)); // nullpoint exception 이 안뜨면 접속해있다는 것.



/*
            String sql2 = "SELECT message_contents from message where send_id=? and receive_id =? and sendtime =?";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, send_id );
            pstmt.setString(2, receive_id );
            pstmt.setTimestamp(3, timestamp);
            rs = pstmt.executeQuery();
*/

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
        }
    } // 메세지 보내기 ( DB 에 message 저장 )
    private void showPreviousMessage(Data data) throws IOException{
        String sql = "SELECT message_contents from message where send_id = ? and receive_id = ?";
        ArrayList<String> pReceiveMessage = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, data.getfId());
            pstmt.setString(2, data.getmId());
            rs = pstmt.executeQuery();
            while (rs.next()){
                pReceiveMessage.add(rs.getString("message_contents"));
            } if (pReceiveMessage.size() != 0){
                data.setProtocol(Protocol.PREVIOUS_MESSAGE_EXIST);
                data.setList(pReceiveMessage);
                oos.writeObject(data);
            } else {
                data.setProtocol(Protocol.PRRVIOUS_MESSAGE_EMPTY);
                oos.writeObject(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    } //이전 메세지 불러오기






} // class



