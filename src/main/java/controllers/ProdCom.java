package controllers;

import entities.Commande;
import entities.Produit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import services.ServiceCommande;
import services.ServiceCommandeProduit;
import tests.IListener;


import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProdCom implements Initializable {

    @FXML
    private Label description;

    @FXML
    private ImageView imageProduit;

    @FXML
    private Label marque;

    @FXML
    private Label nomProd;

    @FXML
    private Label prixProduit;

    @FXML
    private VBox produitDetails;

    @FXML
    private Label reffff;

    @FXML
    private ImageView supprimer;

    @FXML
    private Label taille;
    @FXML
    private Label tailleeeee;

    @FXML
    private Label typeee;
    private ServiceCommande serviceCommande;
    private ServiceCommandeProduit serviceCommandeProduit;
    private Produit produit;
    private String currency = " DT";
    private IListener iListener;


    public ProdCom() throws SQLException {
        this.serviceCommande = new ServiceCommande();
        this.serviceCommandeProduit = new ServiceCommandeProduit();


    }
    @FXML

    private void click(MouseEvent mouseEvent) {
        if (iListener != null) {
            iListener.onClickListener(produit);
        } else {
            System.out.println("iListener is null");
        }
    }

    public void setData(Produit produit, IListener iListener) {
        this.produit = produit;
        this.iListener = iListener;
        nomProd.setText(produit.getNom());
        prixProduit.setText(String.valueOf(produit.getPrix()) + currency);
        reffff.setText(produit.getRef());
        marque.setText(String.valueOf(produit.getMarque()));
        tailleeeee.setText(produit.getTaille());
        typeee.setText(String.valueOf(produit.getTypeSport()));
        description.setText(produit.getDescription());
        String imageUrl = produit.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageProduit.setImage(image);
        }
    }
    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supprimer.setOnMouseClicked(mouseEvent -> {
            Connection connection = (Connection) utils.DataBase.getInstance();
            try {
                if (!connection.isClosed()) {
                    try {
                        int refCommande = serviceCommande.getCommande("dida16").getRefCommande();
                        System.out.println("1");
                        String refProduit = reffff.getText();
                        System.out.println("2");
                        serviceCommandeProduit.deleteProduitFromCommande(refProduit, refCommande);
                        System.out.println("3");
                        showAlert("Suppression réussie", "Le produit a été supprimé de la commande", "", Alert.AlertType.INFORMATION);
                   
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
