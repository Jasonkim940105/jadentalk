package firstpage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("fxml/first.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Jaden's talk");
            stage.show();

        } catch (Exception e){

        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
