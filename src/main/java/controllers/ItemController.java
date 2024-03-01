package controllers;


import com.sun.jdi.connect.spi.Connection;
import entities.Commande;
import entities.Produit;
import entities.Produit_Commande;
import entities.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;
import services.ServiceCommande;
import services.ServiceCommandeProduit;
import services.ServiceProduit;
import tests.IListener;

import javafx.event.ActionEvent;
import utils.DataBase;

import javax.net.ssl.SSLContext;
import java.sql.SQLException;
import java.util.List;


public class ItemController {

    @FXML
    private ImageView addBtn;


    @FXML
    private ImageView imageProduit;

    @FXML
    private Label nomProd;

    @FXML
    private Label prixProduit;

    @FXML
    private VBox produitDetails;


    @FXML
    private Rating rating;
    @FXML
    private Label description;


    @FXML
    private Label taille;
    @FXML
    private Label marque;

    private Produit produit;
    private String currency = " DT";
    private IListener iListener;

    @FXML
    private Label reffff;
    @FXML
    private Label typeee;
    private Commande currentCommande;
    private ServiceCommande serviceCommande;
    private ServiceCommandeProduit serviceCommandeProduit;


    public ItemController() throws SQLException {
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
        taille.setText(produit.getTaille());
        typeee.setText(String.valueOf(produit.getTypeSport()));
        description.setText(produit.getDescription());
        String imageUrl = produit.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageProduit.setImage(image);
        }
    }

            /*//currentCommande = serviceCommande.getCommande("dida16");
        if (produit != null) {
            if (!serviceCommande.commandeExiste(currentCommande)) {
                // Si aucune commande n'est sélectionnée, créer une nouvelle commande vide
                currentCommande = new Commande();
                currentCommande.setPrix(0.0); // Initialiser le prix total de la commande à 0
                currentCommande.setStatus(Status.enAttente); // Status initial
                currentCommande.setIdUser("dida16"); // ID de l'utilisateur actuel
                try {
                    // Ajouter la commande à la base de données
                    serviceCommande.ajouter(currentCommande);
                    // Rafraîchir la table des commandes
                    refreshCommandeTable();
                } catch (SQLException e) {
                    System.out.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
                }
            }
            try {
                currentCommande = serviceCommande.getCommande("dida16");
                // Ajouter le produit à la commande
                serviceCommandeProduit.ajouterProduitACommande(currentCommande.getRefCommande(), produit.getRef());
                // Mettre à jour le prix total de la commande
                currentCommande.setPrix(currentCommande.getPrix() + produit.getPrix());
                // Rafraîchir la table des commandes
                refreshCommandeTable();
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout du produit à la commande : " + e.getMessage());
            }
        } else {
            System.out.println("Veuillez sélectionner un produit.");
        }*/

    @FXML
    void AjouterACommande(MouseEvent event) throws SQLException {
        // Vérifier si l'utilisateur a une commande existante
        if (!serviceCommande.commandeExiste(currentCommande)) {
            // Si aucune commande n'est sélectionnée, créer une nouvelle commande vide
            currentCommande = new Commande();
            currentCommande.setPrix(0.0); // Initialiser le prix total de la commande à 0
            currentCommande.setStatus(Status.enAttente); // Status initial
            currentCommande.setIdUser("dida16"); // ID de l'utilisateur actuel
            try {
                // Ajouter la commande à la base de données
                serviceCommande.ajouter(currentCommande);
                System.out.println("khatfet");
                // Rafraîchir la table des commandes
                refreshCommandeTable();
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
            }
        }
        try {
            currentCommande = serviceCommande.getCommande("dida16");
            // Ajouter le produit à la commande
            serviceCommandeProduit.ajouterProduitACommande(currentCommande.getRefCommande(), produit.getRef());
            System.out.println("khatfet");
            // Mettre à jour le prix total de la commande
            currentCommande.setPrix(currentCommande.getPrix() + produit.getPrix());
            // Rafraîchir la table des commandes
            refreshCommandeTable();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du produit à la commande : " + e.getMessage());
        }
    }


    private void refreshCommandeTable() {
        // Mettre à jour les données de la table des commandes
       // commandeTable.getItems().clear();
       // commandeTable.getItems().addAll(serviceCommande.getAllCommandes());
    }


    @FXML
    private void handleRatingClick(MouseEvent event) {
        // Code à exécuter lorsque le composant de notation est cliqué
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Merci pour la note!");

        alert.showAndWait();
    }


}