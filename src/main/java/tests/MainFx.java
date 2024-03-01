package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("/inscription.fxml"));
        try {
            Parent root= fxmlLoader.load();
            Scene scene= new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestion locaux");
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}
