package controllers.Reservation;

import entities.Reservation.Equipement;
import entities.Reservation.Reservation;
import entities.local.terrain;
import entities.utilisateur.Utilisateur;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import services.Reservation.ServiceEquipement;
import services.Reservation.ServiceReservation;
import services.utilisateur.ServiceUtilisateurs;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class Reservations {
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }
    private static terrain currentTerrain;
    public static void setCurrentTerrain(terrain tr) {
        currentTerrain = tr;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button annulerRes;

    @FXML
    private Button reserverButton;

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


    ServiceReservation service = new ServiceReservation();
    ServiceEquipement serviceE = new ServiceEquipement();
    private List<Equipement> equipements = new ArrayList<>();

    ServiceUtilisateurs serviceUtilisateurs = new ServiceUtilisateurs();

    private boolean getErrors(){
        dateError.setText("");
        dureeError.setText("");
        boolean errorFound = false;
        String dateString = dateAjout.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate dateValue = LocalDate.parse(dateString, dateFormatter);
        } catch (DateTimeParseException e) {
            dateError.setTextFill(Color.RED);
            dateError.setText("La date est obligatoire et doit être au format jj/mm/yyyy");
            errorFound = true;
        }
        String dureeAjoutText = dureeAjout.getText();
        if(dureeAjoutText.isBlank() || !dureeAjoutText.matches("\\d{2}:\\d{2}")){
            dureeError.setTextFill(Color.RED);
            dureeError.setText("La durée doit être au format hh:mm");
            errorFound = true;
        }
        return errorFound;
    }


    @FXML
    void annulerrResOnClick(ActionEvent event) {
        serviceUtilisateurs.changeScreen(event,"/FxmlUtilisateur/clientFront.fxml", "XTRATIME");
    }


    @FXML
    void confirmerrResOnClick(ActionEvent event) {
        if (!getErrors()) {
            try {
                ObservableList<MenuItem> items = equipementsAjout.getItems();
                List<Equipement> equipementsSelectionnes = new ArrayList<>();
                for (MenuItem item : items) {
                    if (item instanceof MenuItem) {
                        CheckBox checkBox = (CheckBox) ((MenuItem) item).getGraphic();
                        if (checkBox.isSelected()) {
                            for (Equipement equipement : equipements) {
                                if (equipement.getNom().equals(checkBox.getText())) {
                                    equipementsSelectionnes.add(equipement);
                                    break;
                                }
                            }
                        }
                    }
                }

                String date = dateAjout.getText();
                String duree = dureeAjout.getText();
                int prix = 15;
                StringBuilder equipementsString = new StringBuilder();
                for (Equipement equipement : equipementsSelectionnes) {
                    equipementsString.append(equipement.getNom()).append(", ");
                }
                Reservation r = new Reservation(prix, currentTerrain.getId(), loggedInUser.getPseudo(), equipementsString.toString(), date, duree);
                System.out.println(currentTerrain);
                service.ajouter(r);
                JOptionPane.showMessageDialog(null, "Reservation Ajoutée avec succès !");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }else {
            equipementsError.setTextFill(Color.RED);
            equipementsError.setText("Veullez remplir tous les champs !");
        }


    }

    @FXML
    private void navigateAffichageTerrainInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/card.fxml"));
            Parent root = loader.load();
            CardController CardController = loader.getController();
            CardController.toString();
            Scene scene = new Scene(root);
            Stage stage = (Stage) annulerRes.getScene().getWindow();
            stage.setScene(scene);
            stage.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void initialize() {

        equipementsAjout.getItems().clear();
        List<Equipement> equipements = new ArrayList<>();
        equipements = serviceE.getEquipementsByTerrainId(currentTerrain.getId());
        for (Equipement equipement : equipements) {
            CheckBox checkBox = new CheckBox(equipement.getNom());
            equipementsAjout.getItems().add(new MenuItem(null, checkBox));
        }
    }


}