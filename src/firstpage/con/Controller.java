package firstpage.con;

import firstpage.DB;
import firstpage.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //멤버
    @FXML
    private Button btnLogIn, btnMakeAccount, btnImpossibleID;

    @FXML
    private TextField txtId, signupId, signupEmail;
    @FXML
    private PasswordField txtPw ,signupPw, signupPwCheck;
    @FXML
    private AnchorPane firstPage, signUpPage, noId, noPw, impossibleId;

    private ArrayList<User> users;

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
    public void imsiLoginBtnAction(ActionEvent event) {
        String id = txtId.getText();
        String pw = txtPw.getText();
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
                Stage stage = (Stage)btnLogIn.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("../fxml/myPage.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Jaden's talk");
                stage.show();
            } catch (Exception e){}
        } else {
            txtId.setText("");
            txtPw.setText("");
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
    public void btnLoginBack(ActionEvent event){
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
    public void imsiJoinBtnAction(ActionEvent event){
        txtId.setText("");
        txtPw.setText("");
        String id = signupId.getText();
        String pw = signupPw.getText();
        String email = signupEmail.getText();
        String data = "2"+"@"+ id + ":" + pw + ":"+ email;
        try {
            bw.write(data+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        signUpPage.setVisible(false);
        firstPage.setVisible(true);
    }

}
