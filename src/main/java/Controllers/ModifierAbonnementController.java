package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceAbonnement;
import entities.Abonnement;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

public class ModifierAbonnementController {

    @FXML
    private Button AB;

    @FXML
    private Button AFF;

    @FXML
    private Button back;

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


    private Abonnement abonnement;
    private ServiceAbonnement serviceAbonnement;


    public void setServiceAbonnement(ServiceAbonnement serviceAbonnement) {
        this.serviceAbonnement = serviceAbonnement;
    }

    // La méthode initData est utilisée pour initialiser les données d'un objet Abonnement et d'un service ServiceAbonnement
    public void initData(Abonnement abonnement, ServiceAbonnement serviceAbonnement) {
        // Assigner l'objet Abonnement et le service ServiceAbonnement aux variables de la classe
        this.abonnement = abonnement;
        this.serviceAbonnement = serviceAbonnement;

        // Afficher les informations de l'abonnement dans les champs correspondants de l'interface utilisateur
        idC.setText(String.valueOf(abonnement.getNomUser())); // Afficher le nom de l'utilisateur dans un champ de texte
        idC1.setText(String.valueOf(abonnement.getNumtel())); // Afficher le numéro de téléphone dans un champ de texte

        // Convertir la date de l'abonnement en java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(abonnement.getDate().getTime());

        // Convertir java.sql.Date en LocalDate pour l'affichage dans un DatePicker
        LocalDate localDate = sqlDate.toLocalDate();

        // Définir la date dans le DatePicker
        date.setValue(localDate);

        // Initialisez les ComboBoxes avec les valeurs nécessaires

        // Récupérer les noms des terrains depuis le service
        List<String> nomsTerrain = null; // Initialisation fictive
        try {
            nomsTerrain = serviceAbonnement.getAllTerrainIds(); // Obtenir la liste des noms de terrain
        } catch (SQLException e) {
            throw new RuntimeException(e); // Gérer l'exception s'il y a un problème lors de la récupération des noms des terrains
        }
        idT.setValue(abonnement.getNomTerrain()); // Sélectionner le nom du terrain dans le ComboBox
        idT.setItems(FXCollections.observableArrayList(nomsTerrain)); // Remplir le ComboBox avec les noms des terrains

        // Récupérer les noms des packs depuis le service
        List<String> nomsPack = null; // Initialisation fictive
        try {
            nomsPack = serviceAbonnement.getAllPackNames(); // Obtenir la liste des noms de pack
        } catch (SQLException e) {
            throw new RuntimeException(e); // Gérer l'exception s'il y a un problème lors de la récupération des noms des packs
        }
        nomP.setValue(abonnement.getNomPack()); // Sélectionner le nom du pack dans le ComboBox
        nomP.setItems(FXCollections.observableArrayList(nomsPack)); // Remplir le ComboBox avec les noms des packs

        // Autres initialisations...
    }


    @FXML
    void sauvegarder(ActionEvent event) {
        if (serviceAbonnement == null || abonnement == null) {
            showAlert("Erreur", "ServiceAbonnement ou Abonnement non initialisé", "Le serviceAbonnement ou l'abonnement n'est pas initialisé.", Alert.AlertType.ERROR);
            return;
        }

        // Récupération des valeurs des champs
        String nomUser = idC.getText();
        String numeroTelephoneText = idC1.getText();
        LocalDate dateAbonnement = date.getValue();
        String nomTerrain = idT.getValue();
        String nomPack = nomP.getValue();

        // Contrôle de saisie pour le nom de l'utilisateur
        if (!validerNomUser(nomUser)) {
            showAlert("Erreur", "Nom d'utilisateur invalide", "Le nom de l'utilisateur doit contenir uniquement des lettres.", Alert.AlertType.ERROR);
            return;
        }

        // Contrôle de saisie pour le numéro de téléphone
        int numeroTelephone;
        try {
            numeroTelephone = Integer.parseInt(numeroTelephoneText);
            if (numeroTelephone < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Numéro de téléphone invalide", "Le numéro de téléphone doit être un entier positif.", Alert.AlertType.ERROR);
            return;
        }



        // Mise à jour des données de l'abonnement avec les nouvelles valeurs des champs
        abonnement.setNomUser(nomUser);
        abonnement.setDate(Date.from(dateAbonnement.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        abonnement.setNomTerrain(nomTerrain);
        abonnement.setNomPack(nomPack);

        try {
            // Mettre à jour l'abonnement dans la base de données
            serviceAbonnement.modifierAbonnement(abonnement);
            showAlert("Succès", "Abonnement modifié avec succès.", "L'abonnement a été modifié avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la sauvegarde des modifications.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherA.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
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
    private boolean validerNumeroTelephone(String numero) {
        // Expression régulière pour vérifier si le numéro de téléphone contient uniquement des chiffres et a une longueur de 10 chiffres
        String regex = "^[0-9]{10}$";
        // Créer un objet Pattern à partir de l'expression régulière
        Pattern pattern = Pattern.compile(regex);
        // Créer un objet Matcher à partir du numéro de téléphone fourni
        Matcher matcher = pattern.matcher(numero);
        // Vérifier si le numéro de téléphone correspond à l'expression régulière
        return matcher.matches();
    }

}
