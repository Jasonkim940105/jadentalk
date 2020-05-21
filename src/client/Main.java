package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("fxml/first.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("까까오똒");
            stage.setResizable(false);
            //stage.initStyle(StageStyle.UTILITY);
            stage.show();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
