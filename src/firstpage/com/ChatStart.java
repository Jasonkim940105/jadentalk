package firstpage.com;

import firstpage.con.ChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatStart extends Application {




    @Override
    public void start(Stage stage) throws Exception {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/chat.fxml"));
            Parent root = loader.load();
            ChatController chatController = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("채팅창");
            stage.show();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}
