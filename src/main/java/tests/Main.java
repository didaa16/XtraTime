package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Image logo = new Image("/Design/LOGO.png"); // Replace "path_to_your_logo.png" with the actual path to your logo

        // Set the icon of the stage to your logo
        stage.getIcons().add(logo);
        Parent root = FXMLLoader.load(getClass().getResource("/FxmlUtilisateur/login.fxml"));
        stage.setTitle("XTRATIME");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
