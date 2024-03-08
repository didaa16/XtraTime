package controllers.Reservation;

import entities.Reservation.Equipement;
import entities.Reservation.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.Reservation.ServiceEquipement;
import services.Reservation.ServiceModifierReservation;
import services.Reservation.ServiceReservation;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
            ser.changeScreen(event, "/FxmlUtilisateur/clientFront.fxml", "XTRATIME");
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