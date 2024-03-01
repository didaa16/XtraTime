package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entities.Equipement;
import entities.Reservation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import services.ServiceEquipement;
import services.ServiceReservation;

import javax.swing.*;
import javafx.scene.control.Button;
public class Reservations {

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
    private static int terrainId;
    public static void setTerrainId() {
        terrainId = 12;
    }
    private List<Equipement> equipements = new ArrayList<>();


    private boolean getErrors(){
        dateError.setText("");
        dureeError.setText("");


        boolean errorFound = false;

        // Vérification du champ date
        String dateString = dateAjout.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate dateValue = LocalDate.parse(dateString, dateFormatter);
        } catch (DateTimeParseException e) {
            dateError.setTextFill(Color.RED);
            dateError.setText("La date est obligatoire et doit être au format jj/mm/yyyy");
            errorFound = true;
        }

        // Vérification du champ durée
        String dureeAjoutText = dureeAjout.getText();
        if(dureeAjoutText.isBlank() || !dureeAjoutText.matches("\\d{2}:\\d{2}")){
            dureeError.setTextFill(Color.RED);
            dureeError.setText("La durée doit être au format hh:mm");
            errorFound = true;
        }

        // Vérification des équipements


        return errorFound;
    }


    @FXML
    void annulerrResOnClick(ActionEvent event) {

    }


    @FXML
    void confirmerrResOnClick(ActionEvent event) {
        if (!getErrors()) {
            Reservation res = new Reservation(dateAjout.getText(), dureeAjout.getText(), equipementsAjout.getText());
            try {
                ObservableList<MenuItem> items = equipementsAjout.getItems();

                // Créer une liste pour stocker les équipements sélectionnés
                List<Equipement> equipementsSelectionnes = new ArrayList<>();

                // Parcourir tous les items
                for (MenuItem item : items) {
                    // Vérifier si l'item est une CheckBox
                    if (item instanceof MenuItem) {
                        CheckBox checkBox = (CheckBox) ((MenuItem) item).getGraphic();
                        // Vérifier si la case à cocher est sélectionnée
                        if (checkBox.isSelected()) {
                            // Parcourir la liste d'équipements pour trouver celui correspondant au nom de la case à cocher
                            for (Equipement equipement : equipements) {
                                if (equipement.getNom().equals(checkBox.getText())) {
                                    equipementsSelectionnes.add(equipement);
                                    break; // Sortir de la boucle une fois que l'équipement est trouvé
                                }
                            }
                        }
                    }
                }

                String date = dateAjout.getText();
                String duree = dureeAjout.getText();
                int prix = 15;
                String pseudo = "dida";
                // Concatenate equipment names into a string
                StringBuilder equipementsString = new StringBuilder();
                for (Equipement equipement : equipementsSelectionnes) {
                    equipementsString.append(equipement.getNom()).append(", ");
                }

// Create the reservation with the concatenated equipment string
                Reservation r = new Reservation(prix, 12, pseudo, equipementsString.toString(), date, duree);

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
//
//            // Set the terrain ID in the Reservations controller
            CardController.toString();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage (window) from the VBox and set the scene
            Stage stage = (Stage) annulerRes.getScene().getWindow();
            stage.setScene(scene);
            stage.close();
        } catch (IOException e) {
            // Handle any potential errors loading the FXML file
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void initialize() {
        annulerRes.setOnAction(event -> {
            navigateAffichageTerrainInterface();
        });
//        dateAjout.setText("12");
        equipementsAjout.getItems().clear();
        List<Equipement> equipements = new ArrayList<>();
        equipements = serviceE.getEquipementsByTerrainId(12);
        for (Equipement equipement : equipements) {
            CheckBox checkBox = new CheckBox(equipement.getNom());
            equipementsAjout.getItems().add(new MenuItem(null, checkBox));
        }
    }


}
