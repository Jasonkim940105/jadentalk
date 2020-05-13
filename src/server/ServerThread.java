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
            case Protocol.JOIN: {
                joinClick(data);
                break;
            }
            case Protocol.ID_CHECK: {
                checkIdClick(data);
                break;
            }
            case Protocol.TEST: {
                oos.writeObject(data);
            }
        }

    }
    private void checkIdClick(Data data) throws IOException{
        String sql = "SELECT ID FROM usertable WHERE ID = '" + data.getId() +"'";
        try {
            pstmt = conn.prepareStatement(sql);
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
    private void loginClick(Data data) throws  IOException{
        String id = data.getUser().getId();
        String pw = data.getUser().getPw();
        try{
            String sql =  "SELECT ID, PW FROM usertable WHERE ID = '" + id + "'";
            pstmt = conn.prepareStatement(sql);
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
    private void joinClick(Data data) throws IOException{
        try {
            pstmt = conn.prepareStatement("INSERT INTO usertable(ID, PW, EMAIL) values (?, ?, ?)");
            pstmt.setString(1, data.getUser().getId());
            pstmt.setString(2, data.getUser().getPw());
            pstmt.setString(3, data.getUser().getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // 중복아이디 접근시, 처리
            e.printStackTrace();
        }finally {
            JdbcUtil.close(pstmt);
        }

    } // 회원가입 버튼 클릭
    private void loginOk(Data data) throws IOException{
        data.setProtocol(Protocol.LOGIN_OK);
        oos.writeObject(data);
    } // 로그인 가능
    private void loginNo() throws  IOException{
        Data data = new Data(Protocol.LOGIN_NO);
        oos.writeObject(data);
    } // 로그인 불가능
    private void loginSuccess(Data data) throws IOException{
        oos.writeObject(data);
    } // 로그인 가능시, 클라이언트에게 데이터 던져줌


} // class



