package firstpage.con;

import firstpage.UserThread;
import firstpage.com.Data;
import firstpage.com.Protocol;
import firstpage.vo.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


import javax.swing.text.Document;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //멤버
    @FXML
    private AnchorPane firstPage, signUpPage, noId, noPw, impossibleId, signUpFormErr, myPage, addPane;


    //note firstPage
    @FXML
    private TextField txtId;
    @FXML
    private PasswordField txtPw;
    @FXML
    private Button btnLogIn;
    //note signUpPage
    @FXML
    private TextField signupId, signupEmail;
    @FXML
    private PasswordField signupPw, signupPwCheck;
    @FXML
    private Button btnMakeAccount;
    //note myPage
    @FXML
    private Label myId;
    //note addPane
    @FXML
    private TextField txtFid, txtMystate;
    @FXML
    private Button btnAddF, btnChangeMyState;



    private Socket socket = null;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    //메소드
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            socket = new Socket("localhost", 11111);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            //서버가 전송한 데이터를 받는 쓰레드
            UserThread user = new UserThread(ois);
            user.start();

        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    //note 첫페이지
    @FXML
    public void singUpBtnAction(ActionEvent event){
        firstPage.setVisible(false);
        signUpPage.setVisible(true);
    }

    @FXML
    public synchronized void LoginBtnAction(ActionEvent event) {
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
            User user = new User(id, pw);
            Data data = new Data(Protocol.LOGIN, user);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Data data = (Data)ois.readObject();
            int protocol = data.getProtocol();
            if(protocol == Protocol.LOGIN_OK){
                firstPage.setVisible(false);
                myPage.setVisible(true);
                data.setProtocol(Protocol.LOGIN_SUCCESS);
                oos.writeObject(data);
            } else if (protocol == Protocol.LOGIN_NO){
                txtId.setText("");
                txtPw.setText("");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Data data = (Data)ois.readObject();
            myId.setText( data.getUser().getId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

    //note signUpPage
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
        try {
            String id = signupId.getText();
            Data data = new Data(Protocol.ID_CHECK, id);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Data data = (Data)ois.readObject();
            if(data.getProtocol() == Protocol.ID_USINGNOW){
                btnMakeAccount.setDisable(true);
                impossibleId.setVisible(true);
            } else if (data.getProtocol() == Protocol.ID_UNUSING){
                btnMakeAccount.setDisable(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
    public void joinBtnAction(ActionEvent event){
        if(!(signupPw.getText().equals(signupPwCheck.getText()))){
            signupPw.setText("");
            signupPwCheck.setText("");
            signUpFormErr.setVisible(true);
            return;
        }
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
        User user = new User(id, pw ,email);
        Data data = new Data(Protocol.JOIN, user);
        try {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        signupId.setText("");
        signupPw.setText("");
        signupPwCheck.setText("");
        signupEmail.setText("");
        btnMakeAccount.setDisable(true);
        signUpPage.setVisible(false);
        firstPage.setVisible(true);
    }


    //note myPage
    @FXML
    public void btnLogout(ActionEvent event){
        txtId.setText("");
        txtPw.setText("");
        myPage.setVisible(false);
        firstPage.setVisible(true);
    }

    @FXML
    public void btnAdd(ActionEvent event){
        myPage.setVisible(false);
        addPane.setVisible(true);

/*
        try {
            Data data = new Data(Protocol.TEST);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Data data =  (Data)ois.readObject();
            System.out.println(data.getProtocol());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

    }

    @FXML
    public void btnAddPaneComplete(ActionEvent event){
        txtFid.setText("");
        txtMystate.setText("");
        addPane.setVisible(false);
        myPage.setVisible(true);
    }

}
