package controllers.store;

import entities.store.Commande;
import entities.store.Produit;
import entities.store.Produit_Commande;
import entities.utilisateur.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import services.store.ServiceCommande;
import services.store.ServiceCommandeProduit;

import java.io.IOException;
import java.sql.SQLException;

public class ProduitDansCom {
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }

    @FXML
    private Label description;

    @FXML
    private ImageView imageProduit;
    @FXML
    private ScrollPane scroll;

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
    //private IListener iListener;


    public ProduitDansCom() throws SQLException {
        this.serviceCommande = new ServiceCommande();
        this.serviceCommandeProduit = new ServiceCommandeProduit();



    }
//    @FXML
//
//    private void click(MouseEvent mouseEvent) {
//        if (iListener != null) {
//            iListener.onClickListener(produit);
//        } else {
//            System.out.println("iListener is null");
//        }
//    }

    public void setData(Produit produit) {
        this.produit = produit;
        //this.iListener = iListener;
        nomProd.setText(produit.getNom());
        prixProduit.setText(produit.getPrix() + currency);
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


    @FXML
    void deleteProd(MouseEvent event) {
        try {
            // Obtenez la référence de la commande actuelle à partir de la base de données
            ServiceCommande serviceCommande = new ServiceCommande();
            Commande commandeActuelle = serviceCommande.getCommande(loggedInUser.getPseudo());
            int refCommande = commandeActuelle.getRefCommande();

            // Obtenez la référence du produit à partir de l'étiquette reffff
            String refProduit = reffff.getText();

            // Obtenez le nombre de ce produit dans la commande actuelle
            Produit_Commande produitCommande = serviceCommandeProduit.getProduitCommande(refProduit, refCommande);
            int nbrProduitDansCommande = produitCommande != null ? produitCommande.getNbr() : 0;

            // Supprimez le produit de la commande en fonction du nombre (nbr) dans la commande
            if (nbrProduitDansCommande > 0) {
                serviceCommandeProduit.supprimerProduitCommande(nbrProduitDansCommande);
                Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/AjoutCom.fxml"));
                description.getScene().setRoot(root);
                showAlert("Suppression réussie", "Le produit a été supprimé de la commande", "", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Aucune suppression", "Le produit n'existe pas dans la commande", "", Alert.AlertType.WARNING);
            }
        } catch (SQLException | IOException e) {
            System.out.println("Erreur lors de la suppression du produit de la commande : " + e.getMessage());
            showAlert("Erreur", "Erreur lors de la suppression du produit de la commande", e.getMessage(), Alert.AlertType.ERROR);
        }
    }





}



