package controllers.event;

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

public class backController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bevents;
    @FXML
    private Button bstat;

    @FXML
    private BorderPane border;
    @FXML
    private Button Calendrier;

    @FXML
    private Button bsponsor;
    @FXML
    private Button chatbox;

    @FXML
    void chatbox(ActionEvent event) {
        loadUi("/chatbot");

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
    void events(ActionEvent event) {
        loadUi("/eventback");

    }

    @FXML
    void sponsor(ActionEvent event) {
        loadUi("/sponsoback");

    }
    @FXML
    void stat(ActionEvent event) {
        loadUi("/BarChartEvent");

    }



    @FXML
    void initialize() {

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

}
