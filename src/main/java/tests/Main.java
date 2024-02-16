package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.ServiceUtilisateurs;

import java.io.IOException;
import java.util.Date;

public class Main extends Application {

    public static void main(String[] args) {
        ServiceUtilisateurs su = new ServiceUtilisateurs();
        launch(args);
        System.out.println(su.afficher());
    }

    private double x, y =0;
    @Override
    public void start(Stage stage) throws Exception{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Login");
            stage.initStyle(StageStyle.DECORATED);
            root.setOnMousePressed(evt ->{
                x= evt.getSceneX();
                y= evt.getSceneY();
            });
            root.setOnMouseDragged(evt ->{
                stage.setX(evt.getScreenX()- x);
                stage.setX(evt.getScreenY()- y);
            });
            stage.setScene(scene);
            stage.show();
    }
}