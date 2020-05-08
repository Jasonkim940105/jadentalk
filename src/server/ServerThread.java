package server;

import java.io.*;
import java.net.Socket;
import java.sql.*;

public class ServerThread extends Thread {
    Socket socket;
    BufferedWriter bw;
    BufferedReader br;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;


    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        conn = JdbcUtil.getConnection();
    }

    @Override
    public void run() {
        while (true) {
            try {
                btnCase(getData());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
// 1 로그인 2 조인

    private String[] getData() throws IOException {
        String fromClient = br.readLine();
        String str[] = fromClient.split("@");
        return str;
    }

    private void btnCase(String[] str) throws IOException {
        String data = str[1];  //필요한 데이터와 : 구분자
        switch (Integer.parseInt(str[0])) {
            case 1: // 로그인 버튼
                String arr[] = data.split(":");
                loginClick(data);
                break;
            case 2: // 조인 버튼
                joinClick(data);
                break;
            case 3: // 아이디 중복확인
                checkIdClick(data);
                break;
        }
    }
    private void checkIdClick(String data) throws IOException{ //ID 중복검사확인
        String arr = data;
        try{
            String sql = "SELECT ID FROM usertable WHERE ID = '" + arr +"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs != null && !rs.isBeforeFirst()) { // DB에 데이터가 없는경우
                //아이디이용가능
                bw.write("idPossible"+"\n");
                bw.flush();
            } else {
                // 아이디 이용불가
                bw.write("idNotPossible"+"\n");
                bw.flush();
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pstmt);
        }
    }

    private void loginClick(String data) throws IOException {
        String arr[] = data.split(":");
        try {
            String sql = "SELECT ID, PW FROM usertable WHERE ID = '" + arr[0] + "'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs != null && !rs.isBeforeFirst()) { // DB에 데이터가 없는경우
                loginNo();
            } else {
                while (rs != null && rs.next()) {
                    String pw = rs.getString("PW");
                    if(arr[1].equals(pw)){
                        loginOk();
                    } else{
                        loginNo();
                    }
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.err.println("쿼리오류");
        } finally {
            JdbcUtil.close(rs,pstmt);
        }
    }

    private void joinClick(String data) {
        String brr[] = data.split(":");
        try {
            pstmt = conn.prepareStatement("INSERT INTO usertable(ID, PW, EMAIL) values (?, ?, ?)");
            pstmt.setString(1, brr[0]);
            pstmt.setString(2, brr[1]);
            pstmt.setString(3, brr[2]);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
           JdbcUtil.close(pstmt);
        }
    }

    private void loginOk() throws IOException {
        bw.write("okay" + "\n");
        bw.flush();
    }

    private void loginNo() throws IOException {
        bw.write("no" + "\n");
        bw.flush();
    }


} // class



