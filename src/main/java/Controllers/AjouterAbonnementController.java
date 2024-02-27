package Controllers;

import entities.Abonnement;
import entities.Pack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceAbonnement;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.sql.SQLException;
import java.sql.Date;
import java.time.ZoneId;
import javafx.scene.image.ImageView;

import javafx.scene.image.Image;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServicePack;

import java.sql.SQLException;
import java.time.LocalDate;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterAbonnementController {

    @FXML
    private Button AB;

    @FXML
    private Button AFF;

    @FXML
    private Button PK;

    @FXML
    private DatePicker date;

    @FXML
    private TextField idC;

    @FXML
    private TextField idC1;

    @FXML
    private ComboBox<String> idT;

    @FXML
    private ComboBox<String> nomP;

    @FXML
    private Button save;

    private ServiceAbonnement serviceAbonnement = new ServiceAbonnement();

    @FXML
    public void initialize() {
        try {
            List<String> terrainIds = serviceAbonnement.getAllTerrainIds();
            ObservableList<String> terrainIdsObservable = FXCollections.observableArrayList(terrainIds);
            idT.setItems(terrainIdsObservable);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon votre logique
        }



        try {
            List<String> packNames = serviceAbonnement.getAllPackNames();
            ObservableList<String> packNamesObservable = FXCollections.observableArrayList(packNames);
            nomP.setItems(packNamesObservable);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon votre logique
        }
    }




    private int generateUniqueId() {
        // Vous pouvez utiliser différentes stratégies pour générer un identifiant unique,
        // par exemple, en utilisant un compteur statique ou en utilisant la date actuelle
        // combinée à un numéro aléatoire.
        // Dans cet exemple, nous utilisons simplement un compteur statique.

        // Incrémentez le compteur statique pour chaque nouvel abonnement
        return +uniqueIdCounter;
    }

    // Définissez un compteur statique pour générer des identifiants uniques
    private static int uniqueIdCounter = 0;

    @FXML
    private void ajouterAbonnement(ActionEvent event) {
        try {
            LocalDate localDate = date.getValue(); // Obtenir la date sélectionnée depuis le DatePicker
            Date utilDate = java.sql.Date.valueOf(localDate); // Convertir LocalDate en java.sql.Date
            String nomTerrain = idT.getValue(); // Obtient le nom du terrain à partir de la ComboBox
            String nomPack = nomP.getValue(); // Obtient le nom du pack à partir de la ComboBox
            String nomUser = idC.getText(); // Obtient le nom de l'utilisateur à partir du champ de texte
            int numtel = Integer.parseInt(idC1.getText()); // Convertir le numéro de téléphone en entier à partir du champ de texte

            // Récupérer l'ID du terrain sélectionné
            int idTerrain = serviceAbonnement.getTerrainIdByNom(nomTerrain);
            // Récupérer l'ID du pack sélectionné
            int idPack = serviceAbonnement.getPackIdByNom(nomPack);

            if (validerNomUser(nomUser)) {
                try {
                    ServiceAbonnement serviceAbonnement = new ServiceAbonnement();
                    serviceAbonnement.ajouterAbonnement(nomUser, localDate , nomTerrain, nomPack, numtel);
                    clearFields();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Erreur lors de l'ajout du User.");
                }
            }
            else {
                // Affichez un message d'erreur indiquant que le nom du pack est invalide
                showAlert("Erreur", "Nom de Usee invalide", "Le nom du pack doit contenir uniquement des lettres.", Alert.AlertType.ERROR);
            }


        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir des valeurs valides pour les champs numériques.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'abonnement: " + e.getMessage());
        }
    }
/*
    @FXML
    private void ajouterAbonnement(ActionEvent event) {
        try {
            LocalDate localDate = date.getValue(); // Obtenir la date sélectionnée depuis le DatePicker
            Date utilDate = java.sql.Date.valueOf(localDate); // Convertir LocalDate en java.sql.Date
            String nomTerrain = idT.getValue(); // Obtient le nom du terrain à partir de la ComboBox
            String nomPack = nomP.getValue(); // Obtient le nom du pack à partir de la ComboBox
            String nomUser = idC.getText(); // Obtient le nom de l'utilisateur à partir du champ de texte
            int numtel = Integer.parseInt(idC1.getText()); // Convertir le numéro de téléphone en entier à partir du champ de texte

            // Appeler le service pour ajouter l'abonnement
            serviceAbonnement.ajouterAbonnement(nomUser, localDate , nomTerrain, nomPack, numtel);
            // Afficher un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Abonnement ajouté avec succès.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir des valeurs valides pour les champs numériques.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout de l'abonnement: " + e.getMessage());
        }
    }
*/

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validerNomUser(String nom) {
        // Expression régulière pour vérifier si le nom contient uniquement des lettres
        String regex = "^[a-zA-Z]+$";
        // Créer un objet Pattern à partir de l'expression régulière
        Pattern pattern = Pattern.compile(regex);
        // Créer un objet Matcher à partir du nom fourni
        Matcher matcher = pattern.matcher(nom);
        // Vérifier si le nom correspond à l'expression régulière
        return matcher.matches();
    }
    private void clearFields() {
        // Effacez le contenu des champs de texte, des zones de texte ou d'autres composants que vous souhaitez vider.
        idC.setText("");
        idC1.setText("");
        date.setValue(null);
        idT.setValue(null);
        nomP.setValue(null);
        // Autres champs à effacer...
    }
    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }




}




