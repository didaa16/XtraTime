package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class frontController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bevents;

    @FXML
    private BorderPane border;

    @FXML
    private Button bout;

    @FXML
    private Button bsponsor;

    @FXML
    void events(ActionEvent event) {
        loadUi("/listevents");


    }

    public void logout(ActionEvent actionEvent) throws IOException {
        loadFXML("/login.fxml");

    }

    private void loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    void sponsor(ActionEvent event) {
        loadUi("/listsponso");

    }
    private void loadUi(String ui) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(ui + ".fxml"));
        } catch (IOException ex) {
            ex.getMessage();
        }
        border.setCenter(root);
    }

    @FXML
    void initialize() {
        assert bevents != null : "fx:id=\"bevents\" was not injected: check your FXML file 'baseFront.fxml'.";
        assert border != null : "fx:id=\"border\" was not injected: check your FXML file 'baseFront.fxml'.";
        assert bout != null : "fx:id=\"bout\" was not injected: check your FXML file 'baseFront.fxml'.";
        assert bsponsor != null : "fx:id=\"bsponsor\" was not injected: check your FXML file 'baseFront.fxml'.";

    }

}
