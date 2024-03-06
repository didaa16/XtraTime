package controllers.Abonnement;
import javafx.scene.image.Image; // Assurez-vous d'importer javafx.scene.image.Image

import entities.Abonnement.Pack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;


import java.io.File;
import java.io.IOException;

public class CardController {
    @FXML
    private javafx.scene.control.Label description;

    @FXML
    private javafx.scene.image.ImageView image;

    @FXML
    private javafx.scene.control.Label nom;

    @FXML
    private javafx.scene.image.ImageView  packImageView2;

    @FXML
    private javafx.scene.control.Label reduction;
    private Pack pack ;
    private PackControllerClient PackControllerClient;

    private   AfficherAbonnementController afficherpack ;
    public void setPack(Pack pack) {
        this.pack = pack;
    }





    public void initializeData(Pack p) {
        nom.setText(p.getNom());
        description.setText(p.getDescription());
        reduction.setText(String.valueOf(p.getReduction()));
        setImage(p.getImage());
    }

    public void setImage(String imagePath) {
        Image img = new Image(new File(imagePath).toURI().toString());
        image.setImage(img);
    }















    public void setAfficherpack(AfficherAbonnementController afficherpack1) {
        this.afficherpack = afficherpack1;
    }

    public void setPackControllerClient(controllers.Abonnement.PackControllerClient PackControllerClient) {
        this.PackControllerClient = PackControllerClient;
    }





    @FXML
    void Abonnement(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXMLAbonnement/AjoutA.fxml"));
            Parent root = fxmlLoader.load();

            // Accéder à la scène à partir du contrôleur AjouterAbonnementController
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);

            AjouterAbonnementController ajouteabonnement = fxmlLoader.getController();
            ajouteabonnement.slectedItem(this.pack.getNom()); //SELECT NOM PACK CHOISIE ET L'AFFICHER
            System.out.println(this.pack.getNom());
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de la navigation", e.getMessage());
        }
    }


    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
