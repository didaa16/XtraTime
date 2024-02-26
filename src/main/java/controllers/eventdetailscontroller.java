package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import services.eventService;
import entities.event;
import javafx.scene.image.Image;

import static controllers.ListeventsController.holdIDs;

public class eventdetailscontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imagedetails;
    eventService se;
    private event selected;
    String eventid;

    public eventdetailscontroller() {
        this.eventid = (String)holdIDs.value;
        this.se = new eventService();
    }
    @FXML
    void initialize() {
        event e = this.se.readById(Integer.parseInt(this.eventid));
        System.out.println("image:" + e.getImage());

        Image image = new Image( e.getImage());
        this.imagedetails.setImage(image);


    }

}
