package firstpage.con;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //멤버
    @FXML
    private Button btnLogIn, btnMakeAccount, btnsignUpFormErr;

    @FXML
    private Label myId;

    @FXML
    private TextField txtId, signupId, signupEmail;
    @FXML
    private PasswordField txtPw ,signupPw, signupPwCheck;
    @FXML
    private AnchorPane firstPage, signUpPage, noId, noPw, impossibleId, signUpFormErr, myPage;

    private Socket socket = null;

    private BufferedReader br;
    private BufferedWriter bw;


    //메소드
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            socket = new Socket("localhost", 11111);
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // 서버로 out
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    //첫페이지
    @FXML
    public void singUpBtnAction(ActionEvent event){
        firstPage.setVisible(false);
        signUpPage.setVisible(true);
    }

    @FXML
    public synchronized void imsiLoginBtnAction(ActionEvent event) {
        String id = txtId.getText();
        //id가 입력안됐을경우
        id = id.trim();
        if(id.length()==0){
            noId.setVisible(true);
            return;
        }
        String pw = txtPw.getText();
        //pw가 입력안됐을경우
        pw = pw.trim();
        if(pw.length()==0){
            noPw.setVisible(true);
            return;
        }
        try {
            bw.write("1"+"@"+id+":"+pw+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String check = "no";
        try {
            check = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(check.equals("okay")){
            try{
                firstPage.setVisible(false);
                myPage.setVisible(true);
                bw.write("4@"+txtId.getText()+"\n");
                bw.flush();
            } catch (Exception e){}
        } else {
            txtId.setText("");
            txtPw.setText("");
        }
        try {
            myId.setText(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void btnNoIdOk(ActionEvent event){
        noId.setVisible(false);
        firstPage.setVisible(true);
    }

    @FXML
    public void btnNoPwOk(ActionEvent event){
        noPw.setVisible(false);
        firstPage.setVisible(true);
    }

    // signUpPage
    @FXML
    public void txtCheckPw(ActionEvent event){
        if(!signupPw.getText().equals( signupPwCheck.getText())){
            signupPwCheck.setText("");
        } else {

        }
    }
    @FXML
    public void btnLoginBack(ActionEvent event){ //회원가입 페이지 뒤로가기
        signupId.setText("");
        signupPw.setText("");
        signupPwCheck.setText("");
        signupEmail.setText("");
        signUpPage.setVisible(false);
        firstPage.setVisible(true);
    }

    @FXML
    public void idCheck(ActionEvent event){
        String id = signupId.getText();
        String pw = signupPw.getText();
        String data = "3@"+id;
        try{
            bw.write(data+"\n");
            bw.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

        String check = "idNotPossible";
        try{
            check = br.readLine();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        if(check.equals("idPossible")){
            btnMakeAccount.setDisable(false);

        } else{
            btnMakeAccount.setDisable(true);
            impossibleId.setVisible(true);
        }

    }
    @FXML
    public void btnImpossibleID(ActionEvent event){
        impossibleId.setVisible(false);
    }

    @FXML
    public void btnsignUpFormErr(ActionEvent event){
        signUpFormErr.setVisible(false);
    }

    @FXML
    public void imsiJoinBtnAction(ActionEvent event){
        txtId.setText("");
        txtPw.setText("");
        String id = signupId.getText();
        id = id.trim();
        // 아이디 입력크기 조절
        if(id.length() > 10 ){
            signUpFormErr.setVisible(true);
            signupId.setText("");
            return;
        }
        String pw = signupPw.getText();
        pw = pw.trim();
        if(pw.length() > 10){
            signUpFormErr.setVisible(true);
            signupPw.setText("");
            signupPwCheck.setText("");
            return;
        }
        // 비밀번호 입력크기 조절
        String email = signupEmail.getText();
        email = email.trim();
        if(!email.contains("@")){
            signUpFormErr.setVisible(true);
            signupEmail.setText("");
            return;
        }
        // 이메일 형식 확인
        String data = "2"+"@"+ id + ":" + pw + ":"+ email;
        try {
            bw.write(data+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        signupId.setText("");
        signupPw.setText("");
        signupPwCheck.setText("");
        signupEmail.setText("");
        signUpPage.setVisible(false);
        firstPage.setVisible(true);
    }

    //myPage
    @FXML
    public void btnLogout(ActionEvent event){
        txtId.setText("");
        txtPw.setText("");
        myPage.setVisible(false);
        firstPage.setVisible(true);
    }

}
