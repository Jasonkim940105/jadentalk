package client.controller;

import client.com.ChatStart;
import client.com.Data;
import client.com.Protocol;
import client.vo.AddFriend;
import client.vo.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //멤버
    @FXML
    private AnchorPane firstPage, signUpPage, noId, noPw, impossibleId, signUpFormErr, myPage, addPane, changePane;


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
    @FXML
    private Label lbMyState;

    //note chagePane
    @FXML
    private TextField txtStates;
    //note addPage
    @FXML
    private TextField txtFid, txtMystate;
    @FXML
    private Button btnAddF, btnChangeMyState;
    @FXML
    private ListView<String> lvRequestFriendList;

    @FXML
    private ListView<String> friendList;

    private Socket socket = null;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private User theUser = null; //현재 접속한 유저정보가 담김

    //메소드
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            socket = new Socket("localhost", 11111);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            lvRequestFriendList.setItems(FXCollections.observableArrayList());
            friendList.setItems(FXCollections.observableArrayList());
            signupId.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    btnMakeAccount.setDisable(true);
                }
            });

        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    private String getLoginId(){
        return theUser.getId();
    }
    private void showFriendList(){ // 접속한 아이디의 친구목록을 요청할 메소드
        String id = getLoginId();
        Data data = new Data(Protocol.FRIEND_LIST_SHOW, id);
        try {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showMyState(){
        String id = getLoginId();
        Data data = new Data(Protocol.MY_STATE_SHOW, id);
        try {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getSelectFriend(){
        String fid = friendList.getSelectionModel().getSelectedItem();
        return fid;
    }
    private void resetFriendList(){
        friendList.getItems().clear();
        try {
            Data data = (Data)ois.readObject();
            if(data.getList().size() != 0 ){
                for(int i = 0 ; i < data.getList().size(); i++){
                    // friendList.getItems().add(data.getList().get(i));
                    friendList.getItems().add(data.getList().get(i).toString());
                }
            }
        } catch (NullPointerException e){
            friendList.getItems().add("추가된 친구가 없습니다");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearFriendList(){
        friendList.getItems().clear();
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
            //theUser = user;
            Data data = new Data(Protocol.LOGIN, user);
            oos.writeObject(data);
        } catch (SocketException se){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("서버오류");
            alert.setContentText("서버가 종료된 상태입니다");
            alert.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Data data = (Data)ois.readObject();
            int protocol = data.getProtocol();
            if(protocol == Protocol.LOGIN_OK){
                firstPage.setVisible(false);
                myPage.setVisible(true);
                data.setProtocol(Protocol.LOGIN_SUCCESS);
                theUser = data.getUser();
                data.getUser().setLoginStatus("o");
                oos.writeObject(data);
            } else if (protocol == Protocol.LOGIN_NO){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("로그인실패");
                alert.setContentText("로그인에 실패하였습니다");
                alert.setContentText("아이디와 비밀번호를 다시 확인해주세요");
                alert.showAndWait();
                theUser = null;
                return;
            } else if (protocol == Protocol.LOGIN_ALREADY){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("로그인실패");
                alert.setContentText("로그인에 실패하였습니다");
                alert.setContentText("이미 접속된 상태입니다.");
                alert.showAndWait();
                theUser = null;
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

        showFriendList();
        resetFriendList();
        showMyState();

        try {
            Data data = (Data)ois.readObject();
            String state =data.getId();
            lbMyState.setText(state);
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
        lbMyState.setText("");
        txtStates.setText("");
        try {
            Data data = new Data(Protocol.LOGOUT, theUser);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Data data = (Data)ois.readObject();
            theUser = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        clearFriendList();
        myPage.setVisible(false);
        firstPage.setVisible(true);
    }

    @FXML
    public void btnAdd(ActionEvent event){
        myPage.setVisible(false);
        addPane.setVisible(true);
        //요청온 친구목록 띄어주기 ( theuser에서 아이디 가져와서 친구요청테이블 db 확인)
        String mId = theUser.getId();
        try {
            Data data = new Data(Protocol.FRIEND_REQUEST_TO_ME, mId);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Data data =(Data)ois.readObject();
            if(data.getProtocol() == Protocol.FRIEND_REQUEST_LIST_SHOW){
                ArrayList<String> requestList = data.getList();
                for(int i = 0; i < requestList.size(); i++){
                    lvRequestFriendList.getItems().add(requestList.get(i));
                }
            } else if (data.getProtocol() == Protocol.FRIEND_LIST_EMPTY) {
                System.out.println("새로운 친구요청이 없는경우");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnChatStartClick(){
        Stage stage = new Stage();
        ChatStart chatStart= new ChatStart();

        String fid = getSelectFriend();
        if(fid == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("친구를 선택해주세요");
            alert.setContentText("채팅을 시작할 친구를 선택해주세요");
            alert.showAndWait();
            return;
        }
        System.out.println("passing value = " + fid);
        ChatController.mid = theUser.getId();
        ChatController.fid = fid ;

        try {
            chatStart.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //note AddPage

    @FXML
    public void addFriend(ActionEvent event) {
        String sendId = theUser.getId();
        String receiveId = txtFid.getText().trim();
        try {
            AddFriend addFriend = new AddFriend(sendId, receiveId);
            Data data = new Data(Protocol.ADD_ID, addFriend);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Data data = (Data) ois.readObject();
            if (data.getProtocol() == Protocol.FRIEND_ID_NOT_EXIST) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("존재하지 않는 아이디");
                alert.setHeaderText("존재하지 않는 아이디입니다");
                alert.setContentText("입력하신 아이디가 존재하지 않습니다");
                alert.showAndWait();
            } else if (data.getProtocol() == Protocol.FRIEND_REQUEST_SUCCESS) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("친구요청");
                alert.setHeaderText("요청완료");
                alert.setContentText("친구요청메세지를 성공적으로 전송하였습니다!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("요청실패");
                alert.setHeaderText("요청실패");
                alert.setContentText("중복요청 혹은 알 수없는 이유로 친구요청메세지 전송에 실패하였습니다.");
                alert.showAndWait();

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void btnRequestAccept(ActionEvent event){
        String mId = theUser.getId();
        String fId = lvRequestFriendList.getSelectionModel().getSelectedItem();
        int idIndex = lvRequestFriendList.getSelectionModel().getSelectedIndex();
        try {
            Data data = new Data(Protocol.FRIEND_REQUEST_ACCEPT, mId, fId);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Data data = (Data)ois.readObject();
            if(data.getProtocol() == Protocol.FRIED_REQUEST_ACCEPT_OK){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("친구수락");
                alert.setHeaderText("친구수락완료");
                alert.setContentText(data.getfId()+ " 님을 친구추가하였습니다");
                alert.showAndWait();
                lvRequestFriendList.getItems().remove(idIndex);
            } else if (data.getProtocol() == Protocol.FRIED_REQUEST_ACCEPT_NO){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("친구수락");
                alert.setHeaderText("친구수락실패");
                alert.setContentText(data.getfId()+ " 님을 친구추가할 수 없습니다.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }  //친구요청수락

    @FXML
    public void btnRequestRefuse(ActionEvent event){
        String mId = theUser.getId();
        String fId = lvRequestFriendList.getSelectionModel().getSelectedItem();
        int idIndex = lvRequestFriendList.getSelectionModel().getSelectedIndex();
        try {
            Data data = new Data(Protocol.FRIEND_REQUEST_REFUSE, mId, fId);
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Data data = (Data)ois.readObject();
            lvRequestFriendList.getItems().remove(idIndex);
            if(data.getProtocol() == Protocol.FRIEND_REQUEST_REFUSE){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("친구수락");
                alert.setHeaderText("친구요청 거절");
                alert.setContentText("친구요청을 거절하였습니다");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    } //친구요청거절
    @FXML
    public void btnAddPaneComplete(ActionEvent event){
        txtFid.setText("");
        lvRequestFriendList.getItems().clear();
        addPane.setVisible(false);
        myPage.setVisible(true);

        showFriendList();
        resetFriendList();

    }

    //note ChagePane
    @FXML
    public void btnChangeClick(ActionEvent event){
        myPage.setVisible(false);
        changePane.setVisible(true);
        Data data = new Data(Protocol.MY_STATE_LOAD, theUser.getId());
        try {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Data data1 = (Data)ois.readObject();
            txtStates.setText(data1.getId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void btnChangeBackClick(ActionEvent event){
        txtStates.setText("");
        changePane.setVisible(false);
        myPage.setVisible(true);

        showMyState();

        try {
            Data data = (Data)ois.readObject();
            String state = data.getId();
            lbMyState.setText(state);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }
    @FXML
    public void btnStateChangeClick(ActionEvent event){
        String myState = txtStates.getText();
        Data data = new Data(Protocol.MY_STATE_CHAGE ,theUser.getId(), myState);
        try {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            data = (Data)ois.readObject();
            if(data.getProtocol() == Protocol.MY_STATE_CHAGE_OK){
                txtStates.setText(myState);
                theUser.setState(myState);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("상태메세지");
                alert.setHeaderText("상태메세지 변경");
                alert.setContentText("상태메세지 변경이 완료되었습니다");
                alert.showAndWait();
            } else if (data.getProtocol() == Protocol.MY_STATE_CHAGE_FAIL){
                txtStates.setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("상태메세지");
                alert.setHeaderText("상태메세지 변경");
                alert.setContentText("상태메세지 변경에 실패해였습니다.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                txtStates.setText("");
                alert.setTitle("상태메세지");
                alert.setHeaderText("상태메세지 변경");
                alert.setContentText("상태메세지 변경에 실패해였습니다.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
