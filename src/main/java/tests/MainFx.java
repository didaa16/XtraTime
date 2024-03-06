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
        FXMLLoader fxmlLoaderAjouter = new FXMLLoader(getClass().getResource("/fxmlStore/AjouterProduit.fxml"));

        try {
            Parent rootAjouter = fxmlLoaderAjouter.load();
            Scene sceneAjouter = new Scene(rootAjouter);
            primaryStage.setScene(sceneAjouter);
            primaryStage.setTitle("gestion de produit");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
