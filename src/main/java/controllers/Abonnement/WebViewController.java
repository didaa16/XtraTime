package controllers.Abonnement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;

public class WebViewController {

    @FXML
    private WebView webView; // Corrected field type to WebView

    // Méthode d'initialisation du contrôleur
    public void initialize() {
        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://www.facebook.com/profile.php?id=61556202778293");
    }


@FXML
    void switchP(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXMLAbonnement/frontC.fxml"));
            Scene scene = ((Button) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


}
