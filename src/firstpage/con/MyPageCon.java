package firstpage.con;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;


import java.net.URL;
import java.util.ResourceBundle;


public class MyPageCon implements Initializable {
    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private ListView<String> friendsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    public void loadData(){
        list.removeAll(list);
        String a = "frined1";
        String b = "friend2";
        String c = "Friend3";
        String d = "Friend4";
        list.addAll(a,b,c,d);
        friendsList.getItems().addAll(list);

    }


}
