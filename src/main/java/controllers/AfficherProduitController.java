package controllers;

import entities.Marque;
import entities.Produit;
import entities.TypeSport;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.ServiceProduit;

import java.io.IOException;
import java.rmi.ServerError;
import java.sql.SQLException;
import java.util.Optional;

public class AfficherProduitController {

    ServiceProduit serviceProduit =new ServiceProduit();

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;


    @FXML
    private TableColumn<Produit, String> c_description;

    @FXML
    private TableColumn<Produit, String> c_image;

    @FXML
    private TableColumn<Produit, Marque> c_marque;

    @FXML
    private TableColumn<Produit, String> c_nom;

    @FXML
    private TableColumn<Produit, Double> c_prix;

    @FXML
    private TableColumn<Produit, Integer> c_quantite;

    @FXML
    private TableColumn<Produit, String> c_reference;

    @FXML
    private TableColumn<Produit, String> c_taille;

    @FXML
    private TableColumn<Produit, TypeSport> c_typesport;

    @FXML
    private TableView<Produit> listeProduit;


    @FXML
    void initialize(){
        try {
            ObservableList<Produit> produits = FXCollections.observableList(serviceProduit.afficher());
            listeProduit.setItems(produits);
            c_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            c_reference.setCellValueFactory(new PropertyValueFactory<>("ref"));
            c_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            c_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            c_taille.setCellValueFactory(new PropertyValueFactory<>("taille"));
            c_typesport.setCellValueFactory(new PropertyValueFactory<>("typeSport"));
            c_marque.setCellValueFactory(new PropertyValueFactory<>("marque"));
            c_quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            c_image.setCellValueFactory(new PropertyValueFactory<>("image"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    void supprimerproduit(ActionEvent event) {
        Produit produit = listeProduit.getSelectionModel().getSelectedItem();
        if (produit != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le produit ?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer le produit " + produit.getNom() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    serviceProduit.supprimer(produit.getRef());
                    listeProduit.getItems().remove(produit);

                    // Afficher un message de confirmation
                    Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                    confirmationAlert.setTitle("Suppression réussie");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Le produit " + produit.getNom() + " a été supprimé avec succès.");
                    confirmationAlert.showAndWait();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            showAlert("Aucune sélection", "Aucun produit sélectionné", "Veuillez sélectionner un produit à supprimer.", Alert.AlertType.WARNING);
        }
    }



    @FXML
    void modifierProduit(ActionEvent event) {
        Produit produit = listeProduit.getSelectionModel().getSelectedItem();
        if (produit != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduit.fxml"));
                Parent root = loader.load();
                ModifierProduitController controller = loader.getController();

                controller.initData(produit);
                controller.setServiceProduit(serviceProduit);
                controller.setAfficherProduitController(this);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de l'ouverture de l'interface de modification", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun produit sélectionné", "Veuillez sélectionner un produit à modifier.", Alert.AlertType.WARNING);
        }
    }



    public void refreshTable() {
        try {
            ObservableList<Produit> produits = FXCollections.observableList(serviceProduit.afficher());
            listeProduit.setItems(produits);
        } catch (Exception e) {
            System.out.println("An error occurred while refreshing table: " + e.getMessage());
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
    void ajouterProduit(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterProduit.fxml"));

            Scene scene = btnAjouter.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page AjouterProduit", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


}
