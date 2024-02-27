package Controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.ServiceAbonnement;
import entities.Abonnement;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;

import javafx.stage.Stage;
import java.util.List;
import services.ServiceAbonnement;

public class AfficherAbonnementController {




    ServiceAbonnement serviceAbonnement = new ServiceAbonnement();

    @FXML
    private Button AB;

    @FXML
    private Button AB1;

    @FXML
    private TableColumn<Abonnement, Date> date;

    @FXML
    private TableColumn<Abonnement, Integer> id;

    @FXML
    private Button mod;

    @FXML
    private TableColumn<Abonnement, Integer> nomTel;

    @FXML
    private TableColumn<Abonnement, Integer> terrainId;

    @FXML
    private TableColumn<Abonnement, String> nomTerrain;

    @FXML
    private TableColumn<Abonnement, String> nomUser;

    @FXML
    private TableColumn<Abonnement, Float> prix;

    @FXML
    private TableColumn<Abonnement, Float> prixTotal;

    @FXML
    private TableView<Abonnement> tabA;

    @FXML
    private TableColumn<Abonnement, String>nomPack;

    @FXML
    private TableColumn<Abonnement, Integer> packId;



    @FXML
void initialize() {
    // Associer les PropertyValueFactory aux colonnes de la TableView
    id.setCellValueFactory(new PropertyValueFactory<>("id"));
    date.setCellValueFactory(new PropertyValueFactory<>("date"));
    prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
    terrainId.setCellValueFactory(new PropertyValueFactory<>("terrainId"));
    nomPack.setCellValueFactory(new PropertyValueFactory<>("nomPack"));
    nomTerrain.setCellValueFactory(new PropertyValueFactory<>("nomTerrain"));
    nomUser.setCellValueFactory(new PropertyValueFactory<>("nomUser"));
    nomTel.setCellValueFactory(new PropertyValueFactory<>("numtel"));
    prixTotal.setCellValueFactory(new PropertyValueFactory<>("prixTotal"));
    // Assurez-vous d'associer la colonne packId à la propriété correcte
    packId.setCellValueFactory(new PropertyValueFactory<>("packId"));

    try {
        // Récupérer les données d'abonnement depuis la base de données
        ServiceAbonnement serviceAbonnement = new ServiceAbonnement();
        List<Abonnement> abonnements = serviceAbonnement.getAllAbonnements();

        // Ajouter les données à la TableView
        tabA.getItems().addAll(abonnements);
    } catch (SQLException e) {
        e.printStackTrace();
        // Gérer l'erreur
    }
}


    private void loadComboBoxData() {
        try {
            // Assurez-vous de fournir une liste d'entiers pour terrainIds
            ObservableList<Integer> terrainIds = FXCollections.observableArrayList();
            // Assurez-vous de fournir une liste d'entiers pour packIds
            ObservableList<Integer> packIds = FXCollections.observableArrayList();
            // ComboBox pour pack_id

            // ComboBox pour clienPseudo
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des données ComboBox", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        Abonnement abonnement = tabA.getSelectionModel().getSelectedItem();
        if (abonnement != null) {
            try {
                serviceAbonnement.supprimer(abonnement.getId());
                tabA.getItems().remove(abonnement);
                showAlert("Succès", "Abonnement supprimé avec succès.", "L'abonnement a été supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la suppression de l'abonnement.", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun abonnement sélectionné", "Veuillez sélectionner un abonnement à supprimer.", Alert.AlertType.WARNING);
        }
    }


    @FXML
    void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ajout.fxml"));
            Scene scene = ((Button) event.getSource()).getScene();
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

    @FXML
    void modifierAbonnement(ActionEvent event) {
        // Récupérer l'abonnement sélectionné dans la table
        Abonnement abonnement = tabA.getSelectionModel().getSelectedItem();

        if (abonnement != null) {
            try {
                // Charger l'interface utilisateur pour la modification de l'abonnement
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierA.fxml"));
                Parent root = loader.load();

                // Passer les données de l'abonnement sélectionné au contrôleur de modification
                ModifierAbonnementController modifierAbonnementController = loader.getController();
                modifierAbonnementController.initData( abonnement,serviceAbonnement);

                // Afficher la nouvelle interface utilisateur dans une nouvelle fenêtre
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors du chargement de l'interface de modification", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun abonnement sélectionné", "Veuillez sélectionner un abonnement à modifier.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void switchToAjoutC(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficheP.fxml"));
            Scene scene = ((Button) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }



}
