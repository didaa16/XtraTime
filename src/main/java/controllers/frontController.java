package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

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

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void sponsor(ActionEvent event) {
        loadUi("/listsponsors");

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
