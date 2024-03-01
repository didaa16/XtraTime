package controllers;

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
        // Vérifiez d'abord si tous les champs sont remplis correctement
        if (!champsSontValides()) {
            // Affichez un message d'erreur ou effectuez une action appropriée si les champs ne sont pas valides
            return;
        }

        // Créez un objet Reservation avec les données modifiées à partir des champs de texte
        Reservation reservationModifiee = new Reservation(reservation.getId(), dateAjout.getText(), dureeAjout.getText(),150, 12, "bohmid", equipementsAjout.getText() );
        try {
            // Appelez la méthode de service pour modifier la réservation dans la base de données
            serviceModifierReservation.modifier(reservationModifiee);

            // Affichez un message de succès
            JOptionPane.showMessageDialog(null, "Réservation modifiée avec succès !");
            ser.changeScreen(event, "/mesreservations.fxml", "XTRATIME");
        } catch (SQLException ex) {
            // Gérez les erreurs de modification de la réservation dans la base de données
            ex.printStackTrace();
            // Affichez un message d'erreur ou effectuez une action appropriée en cas d'erreur
        }
    }

    // Méthode pour valider les champs avant de modifier la réservation
    private boolean champsSontValides() {
        // Vérifiez si tous les champs sont remplis correctement
        // Vous pouvez ajouter ici toute logique de validation nécessaire pour vos champs
        return !dateAjout.getText().isEmpty() && !dureeAjout.getText().isEmpty() && !equipementsAjout.getText().isEmpty();
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
