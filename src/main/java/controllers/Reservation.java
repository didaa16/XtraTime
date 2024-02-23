package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceReservation;

public class Reservation {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ajoutBTN;

    @FXML
    private CheckBox ballAjout;

    @FXML
    private CheckBox cerceauxAjout;

    @FXML
    private TextArea clientPseudoAjout;

    @FXML
    private Label clientpseudoError;

    @FXML
    private TableColumn<Reservation, String> clientpseudoReservation;

    @FXML
    private CheckBox cramponAjout;

    @FXML
    private DatePicker dateAjout;

    @FXML
    private Label dateError;

    @FXML
    private TableColumn<Reservation, String> dateReservation;

    @FXML
    private CheckBox dossardAjout;

    @FXML
    private Label dureeError;

    @FXML
    private TableColumn<Reservation, String> dureeReservation;

    @FXML
    private CheckBox eauAjout;

    @FXML
    private Label equipementsError;

    @FXML
    private TableColumn<Reservation, String> equipementsReservation;

    @FXML
    private TextArea heureAjout;

    @FXML
    private TableColumn<Reservation, Integer> idReservation;

    @FXML
    private TextArea minuteAjout;

    @FXML
    private Button modifierBTN;

    @FXML
    private CheckBox piquetAjout;

    @FXML
    private TextArea prixAjout;

    @FXML
    private Label prixError;

    @FXML
    private TableColumn<Reservation, Integer> prixReservation;

    @FXML
    private Button rechercheBTN;

    @FXML
    private TextField rechercheRES;

    @FXML
    private Button supprimerBTN;

    @FXML
    private TableView<entities.Reservation> tableReservation;

    @FXML
    private TextArea terrainIdAjout;

    @FXML
    private Label terrainidError;

    @FXML
    private TableColumn<Reservation, Integer> terrainidReservation;

    @FXML
    void ajoutBTNOnClick(ActionEvent event) {

    }

    @FXML
    void modifierBTNOnClick(ActionEvent event) {

    }

    @FXML
    void rechercheBTNOnClick(ActionEvent event) {

    }

    @FXML
    void initialize() {
        ServiceReservation serviceReservation = new ServiceReservation(); // Utiliser ServiceReservation au lieu de ServiceEquipement
        ObservableList<entities.Reservation> liste = null;
        try {
            liste = FXCollections.observableList(serviceReservation.afficher());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        idReservation.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateReservation.setCellValueFactory(new PropertyValueFactory<>("date"));
        dureeReservation.setCellValueFactory(new PropertyValueFactory<>("duree"));
        prixReservation.setCellValueFactory(new PropertyValueFactory<>("prix"));
        terrainidReservation.setCellValueFactory(new PropertyValueFactory<>("terrainId"));
        clientpseudoReservation.setCellValueFactory(new PropertyValueFactory<>("clientPseudo"));
        equipementsReservation.setCellValueFactory(new PropertyValueFactory<>("equipements"));
        tableReservation.setItems(liste);
    }



}
