package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mesreservations.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/equipements.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservation.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/listReservations.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Login");
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();

    }
}