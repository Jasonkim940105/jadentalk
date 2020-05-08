package server;

import oracle.jdbc.proxy.annotation.Pre;

import java.sql.*;

public class JdbcUtil {
    public static Connection getConnection(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XEPDB1", "jaden", "jaden");
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public static void close(PreparedStatement pstmt){
        if(pstmt!=null){
            try {
                pstmt.close();
            } catch (SQLException sqle){
                sqle.printStackTrace();
            }
        }
    }
    public static void close(ResultSet rs){
        if(rs!= null){
            try{
                rs.close();
            } catch (SQLException sqle){
                sqle.printStackTrace();
            }
        }
    }
    public static void close(Connection con){
        if(con != null){
            try{
                con.close();
            } catch (SQLException sqle){
                sqle.printStackTrace();
            }
        }
    }
    public static void close(ResultSet rs, PreparedStatement pstmt){
        close(rs);
        close(pstmt);
    }
    public static void close(PreparedStatement pstmt, Connection con){
        close(pstmt);
        close(con);
    }
    public static void close(ResultSet rs, PreparedStatement pstmt, Connection con){
       close(rs);
       close(pstmt);
       close(con);
    }
}
