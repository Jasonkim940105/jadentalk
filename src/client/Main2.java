package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main2 extends Application {

    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("fxml/first.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("까까오똒");
            stage.setResizable(false);
            stage.show();

        } catch (Exception e){

        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
