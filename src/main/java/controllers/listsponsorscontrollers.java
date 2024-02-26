package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class listsponsorscontrollers {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane Panesponsofx;

    @FXML
    private AnchorPane Panesponsofx1;

    @FXML
    private AnchorPane Panesponsofx11;

    @FXML
    private Button bouton;

    @FXML
    private ImageView imagesponsopanefx;

    @FXML
    private ImageView imagesponsopanefx1;

    @FXML
    private ImageView imagesponsopanefx11;

    @FXML
    private AnchorPane liste_sponsors;

    @FXML
    private Label subjectsponsopanefx;

    @FXML
    private Label subjectsponsopanefx1;

    @FXML
    private Label subjectsponsopanefx11;

    @FXML
    void viewmore(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert Panesponsofx != null : "fx:id=\"Panesponsofx\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert Panesponsofx1 != null : "fx:id=\"Panesponsofx1\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert Panesponsofx11 != null : "fx:id=\"Panesponsofx11\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert bouton != null : "fx:id=\"bouton\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert imagesponsopanefx != null : "fx:id=\"imagesponsopanefx\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert imagesponsopanefx1 != null : "fx:id=\"imagesponsopanefx1\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert imagesponsopanefx11 != null : "fx:id=\"imagesponsopanefx11\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert liste_sponsors != null : "fx:id=\"liste_sponsors\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert subjectsponsopanefx != null : "fx:id=\"subjectsponsopanefx\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert subjectsponsopanefx1 != null : "fx:id=\"subjectsponsopanefx1\" was not injected: check your FXML file 'listsponsors.fxml'.";
        assert subjectsponsopanefx11 != null : "fx:id=\"subjectsponsopanefx11\" was not injected: check your FXML file 'listsponsors.fxml'.";

    }

}
