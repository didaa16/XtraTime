package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import entities.event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import services.eventService;

public class ShoweventController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane cardevent;

    @FXML
    private Label edesceiption;

    @FXML
    private ImageView eimage;
    eventService es=new eventService();
    @FXML
    private Label etitre;

    @FXML
    void initialize() {
        ObservableList<event> events = FXCollections.observableList(es.readAll());


    }

}
