package controllers.event;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import services.event.eventService;
import entities.event.event;
import javafx.scene.image.Image;


import javafx.scene.control.Label;

import static controllers.event.ListeventsController.holdIDs;

public class eventdetailscontroller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imagedetails;

    @FXML
    private Label titredetails;
    @FXML
    private Label datedetails;

    @FXML
    private Label descriptiondetails;

    @FXML
    private Label sponsodetails;

    @FXML
    private Label terraindetails;
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
        Image image = new Image(e.getImage());
        this.imagedetails.setImage(image);
        this.imagedetails.setFitWidth(200); // Définir la largeur souhaitée
        this.imagedetails.setFitHeight(150); // Définir la hauteur souhaitée


        // Initialiser les autres étiquettes avec les informations de l'événement
        this.titredetails.setText(e.getTitre());
        this.datedetails.setText(e.getDatedebut().toString()); // Utilisez la méthode appropriée pour afficher la date
        this.descriptiondetails.setText(e.getDescription());

        // Récupérer et afficher le nom du sponsor à partir de son ID
        int sponsorId = e.getIdsponso();
        String sponsorName = this.se.getNomsponsoByID(sponsorId);
        this.sponsodetails.setText(sponsorName);

        // Récupérer et afficher le nom du terrain à partir de son ID
        int terrainId = e.getIdterrain();
        String terrainName = this.se.getNomterrainIdByID(terrainId);
        this.terraindetails.setText(terrainName);
    }


}
