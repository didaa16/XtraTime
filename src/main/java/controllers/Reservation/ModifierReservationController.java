package controllers.Reservation;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entities.Equipement;
import entities.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceEquipement;
import services.ServiceModifierReservation;
import services.ServiceReservation;

import javax.swing.*;

public class ModifierReservationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button confirmerRes;

    @FXML
    private TextField dateAjout;

    @FXML
    private Label dateError;

    @FXML
    private TextField dureeAjout;

    @FXML
    private Label dureeError;

    @FXML
    private MenuButton equipementsAjout;

    @FXML
    private Label equipementsError;
    private static Reservation reservation;
    ServiceReservation ser = new ServiceReservation();
    public static void setReservation(Reservation res){reservation = res;}
    ServiceModifierReservation serviceModifierReservation = new ServiceModifierReservation();
    ServiceEquipement serviceE = new ServiceEquipement();

    @FXML
    void confirmerResOnClick(ActionEvent event) {
        if (!champsSontValides()) {
            return;
        }
        Reservation reservationModifiee = new Reservation(reservation.getId(), dateAjout.getText(), dureeAjout.getText(),reservation.getPrix(), reservation.getTerrainId(), reservation.getClientPseudo(), equipementsAjout.getText() );
        try {
            serviceModifierReservation.modifier(reservationModifiee);
            JOptionPane.showMessageDialog(null, "Réservation modifiée avec succès !");
            ser.changeScreen(event, "/mesreservations.fxml", "XTRATIME");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private boolean champsSontValides() {
        return !dateAjout.getText().isEmpty() && !dureeAjout.getText().isEmpty();
    }

    private void afficher(){
        if (reservation != null){
            dateAjout.setText(reservation.getDate());
            dureeAjout.setText(reservation.getDuree());
            equipementsAjout.setText(reservation.getEquipements());
        }
    }
    @FXML
    void initialize() {
        equipementsAjout.getItems().clear();
        List<Equipement> equipements = new ArrayList<>();
        equipements = serviceE.getEquipementsByTerrainId(12);
        for (Equipement equipement : equipements) {
            CheckBox checkBox = new CheckBox(equipement.getNom());
            equipementsAjout.getItems().add(new MenuItem(null, checkBox));
        }

        afficher();
    }

}