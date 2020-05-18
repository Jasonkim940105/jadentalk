package client.com;

import client.controller.ChatController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

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
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        chatController.getSocket().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}
