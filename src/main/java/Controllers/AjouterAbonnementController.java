package Controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceAbonnement;

import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.sql.SQLException;
import java.sql.Date;

import javafx.scene.control.Alert;

import java.time.LocalDate;

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

    public static final String ACCOUNT_SID = "AC294826a0d7e01332b57990ad5f8149d6";
    public static final String AUTH_TOKEN = "ac5f577e5b9b37e9e6114295d4cab892";

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
            nomP.getItems().addAll(serviceAbonnement.getAllPackNames());

            // Rendre le ComboBox nomP non modifiable
            nomP.setEditable(false);
            nomP.setDisable(true);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon votre logique
        }
    }

    @FXML
    void change(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/frontC.fxml"));
            idC.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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
            LocalDate selectedDate = date.getValue();
            LocalDate currentDate = LocalDate.now(); // Obtenez la date actuelle

            // Vérifiez si la date sélectionnée est antérieure à la date actuelle
            if (selectedDate.isBefore(currentDate)) {
                // Affichez un message d'erreur
                showAlert("Erreur", "Date invalide", "Veuillez sélectionner une date ultérieure à la date actuelle.", Alert.AlertType.ERROR);
                return; // Sortez de la méthode sans poursuivre l'ajout d'abonnement
            }
            String nomTerrain = idT.getValue();
            String nomPack = nomP.getValue();
            String nomUser = idC.getText();
            int numtel = Integer.parseInt(idC1.getText());
            String numTelTwilio = "+216" + idC1.getText();
            System.out.println(numTelTwilio);

            // Récupérer l'ID du terrain sélectionné
            int terrainId = serviceAbonnement.getTerrainIdByNom(nomTerrain);
            // Récupérer l'ID du pack sélectionné
            int packId = serviceAbonnement.getPackIdByNom(nomPack);
            float prixTotalAvantReduction;

            // Vérifier si le pack est "premium" pour obtenir le prix du terrain avec le pack "premium"
            if (nomPack.equalsIgnoreCase("premium")) {
                prixTotalAvantReduction = serviceAbonnement.getPrixTerrainPourPackPremium(terrainId);
            } else {
                prixTotalAvantReduction = serviceAbonnement.calculerPrixTotalAvantReduction(nomPack, terrainId, packId);
            }

            float prixAbonnement = serviceAbonnement.calculerPrixApresReduction(prixTotalAvantReduction, packId, terrainId, nomPack);

            if (validerNomUser(nomUser)) {
                try {
                    serviceAbonnement.ajouterAbonnement(nomUser, selectedDate, nomTerrain, nomPack, numtel);
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

                    Message message = Message
                            .creator(
                                    new com.twilio.type.PhoneNumber(numTelTwilio),
                                    new com.twilio.type.PhoneNumber("+13392290039"),
                                    "Félécitations Mr/Mme " + nomUser.toUpperCase() + " Votre abonnement au pack " + nomPack.toUpperCase() + " a été effectué avec succés :) !"
                            )
                            .create();

                    System.out.println(message.getSid());
                    clearFields();
                    afficherDetailsAbonnement(nomUser,selectedDate, nomTerrain, nomPack, numtel, prixAbonnement);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Erreur lors de l'ajout de l'abonnement.");
                }
            } else {
                showAlert("Erreur", "Nom d'utilisateur invalide", "Le nom d'utilisateur doit contenir uniquement des lettres.", Alert.AlertType.ERROR);
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

    public  void slectedItem(String nom){
        nomP.setValue(nom);
    }


    private void afficherDetailsAbonnement(String nomUser, LocalDate date, String nomTerrain, String nomPack, int numtel, float prixAbonnement) {
        try {
            // Récupérer l'ID du terrain et du pack à partir de leur nom
            int terrainId = serviceAbonnement.getTerrainIdByNom(nomTerrain);
            int packId = serviceAbonnement.getPackIdByNom(nomPack);

            // Récupérer le prix total avant réduction
            float prixTotalAvantReduction = serviceAbonnement.calculerPrixTotalAvantReduction(nomPack, terrainId, packId);

            // Calculer le prix de l'abonnement après réduction
            prixAbonnement = serviceAbonnement.calculerPrixApresReduction(prixTotalAvantReduction, packId, terrainId, nomPack);

            // Créer une boîte de dialogue de type INFORMATION
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Détails de l'abonnement");
            alert.setHeaderText("Détails de l'abonnement enregistré avec succès :");

            // Afficher les données de l'abonnement dans le contenu de la boîte de dialogue, y compris le prix après réduction
            alert.setContentText("Nom : " + nomUser + "\n" +
                    "Date : " + date.toString() + "\n" +
                    "Terrain : " + nomTerrain + "\n" +
                    "Pack : " + nomPack + "\n" +
                    "Numéro de téléphone : " + numtel + "\n" +
                    "Prix de l'abonnement avant réduction : " + prixTotalAvantReduction + "\n" +
                    "Prix de l'abonnement après réduction : " + prixAbonnement);

            // Afficher la boîte de dialogue
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon votre logique
        }
    }






}








