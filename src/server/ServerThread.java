package server;

import firstpage.DB;
import firstpage.User;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class ServerThread extends Thread {
    Socket socket;
    BufferedWriter bw;
    BufferedReader br;
    Connection conn = null;
    PreparedStatement pstmt = null;
    Statement stmt = null;
    ResultSet rs = null;


    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            System.err.println("드라이버 검색 실패");
        }
        try {
            String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
            conn = DriverManager.getConnection(url, "jaden", "jaden");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.err.println("데이터베이스 연결 실패");
        }
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
        }
    }

    private void loginClick(String data) throws IOException {
        String arr[] = data.split(":");
        ArrayList<String> idlist = new ArrayList<>();
        ArrayList<String> pwList = new ArrayList<>();

        try {
            System.out.println(arr[0]);
            String sql = "SELECT ID, PW FROM usertable WHERE ID = '" + arr[0] + "'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println(rs);
            System.out.println(rs.isBeforeFirst());
            if (rs != null && !rs.isBeforeFirst()) { // 없는경우
                loginNo();
            } else {
                while (rs != null && rs.next()) {
                    System.out.println(rs.getString("ID") + "/" + rs.getString("PW"));
                    idlist.add(rs.getString("ID"));
                    pwList.add(rs.getString("PW"));
                }

                for (int i = 0; i < idlist.size(); i++) {
                    System.out.println(idlist.get(i));
                    if (idlist.get(i).equals(arr[0])) {
                        if (pwList.get(i).equals(arr[1])) {
                            loginOk();
                        } else {
                            loginNo();
                        }
                    } else
                        loginNo();
                }

            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.err.println("쿼리오류");
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException sqle) {
            }
            try {
                if (pstmt != null) pstmt.clearParameters();
            } catch (SQLException sqle) {
            }
        }
        /*// arr[0] = id , arr[1] = pw
        if (DB.getUsers().size() != 0) {
            for (int i = 0; i < DB.getUsers().size(); i++) {
                if (arr[0].equals(DB.getUsers().get(i).getId())) {
                    if (arr[1].equals(DB.getUsers().get(i).getPw())) {
                        loginOk();
                    } else { //비밀번호 다른경우
                        loginNo();
                    }
                } else { //아이디 없는경우
                    loginNo();
                }
            }
        } else { // 디비에 데이터가 없는경우
            loginNo();
        }*/

    }

    private void joinClick(String data) {
        String brr[] = data.split(":");
        try {
            pstmt = conn.prepareStatement("INSERT INTO usertable(ID, PW, EMAIL) values (?, ?, ?)");
            pstmt.setString(1, brr[0]);
            pstmt.setString(2, brr[1]);
            pstmt.setString(3, brr[2]);
            pstmt.executeUpdate();
            System.out.println("성공");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.err.println("쿼리오류");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException sqle) {
            }
            //try{if(conn!=null)conn.close();}catch (SQLException sqle){}
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



