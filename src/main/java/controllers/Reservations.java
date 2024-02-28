package controllers;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
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



    @FXML
    void annulerResOnClick(ActionEvent event) {

    }

    @FXML
    void confirmerResOnClick(ActionEvent event) {
        try {
        String selectedEquipment = equipementsAjout.getText();
            String date = dateAjout.getText();
            String duree = dureeAjout.getText();

            // Créer la réservation avec les informations récupérées
            Reservation r = new Reservation(selectedEquipment, date, duree);
            r.setTerrainId(terrainId);
            service.ajouter(r);
            JOptionPane.showMessageDialog(null,"Reservation Ajoutée avec succés !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private void navigateToAffichageTerrainInterface() {
        confirmerResOnClick(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichageTerrain.fxml"));
            Parent root = loader.load();
            affichageTerrain affichageTerrain = loader.getController();

            // Set the terrain ID in the Reservations controller
            affichageTerrain.toString();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage (window) from the VBox and set the scene
            Stage stage = (Stage) reserverButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle any potential errors loading the FXML file
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void navigateAffichageTerrainInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichageTerrain.fxml"));
            Parent root = loader.load();
            affichageTerrain affichageTerrain = loader.getController();

            // Set the terrain ID in the Reservations controller
            affichageTerrain.toString();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Get the stage (window) from the VBox and set the scene
            Stage stage = (Stage) annulerRes.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Handle any potential errors loading the FXML file
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void initialize() {
        reserverButton.setOnAction(event -> {
            navigateToAffichageTerrainInterface();
        });
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
