package controllers;

import entities.Commande;
import entities.Produit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import services.ServiceCommande;
import services.ServiceCommandeProduit;
import javafx.scene.control.Label;
import services.ServiceProduit;
import tests.IListener;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterComController implements Initializable {
    private IListener iListener;
    private List<Produit> produits = new ArrayList<>();
    @FXML
    private Button annuler;
    @FXML
    private Label refCommandeDesProd;
    @FXML
    private Button commander;
    @FXML
    private ImageView back;
    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField total;
    @FXML
    private Label taille;
    @FXML
    private ImageView refresh;
    private int row = 1;

    private String refCommande; // Ajoutez une variable pour stocker la référence de la commande

    private List<Produit> getData() {
        List<Produit> produits = new ArrayList<>();
        try {
            ServiceCommandeProduit serviceCommandeProduit = new ServiceCommandeProduit();
            // Utilisez la référence de la commande pour obtenir les produits de la commande
            List<Produit> allProducts = serviceCommandeProduit.getProduitsByRefCommande(refCommande);
            for (Produit produit : allProducts) {
                Produit produitInfo = new Produit();
                produitInfo.setNom(produit.getNom());
                produitInfo.setPrix(produit.getPrix());
                produitInfo.setImage(produit.getImage());
                produitInfo.setRef(produit.getRef());
                produitInfo.setTaille(produit.getTaille());
                produitInfo.setMarque(produit.getMarque());
                produitInfo.setTypeSport(produit.getTypeSport());
                produits.add(produitInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    public void initialize(URL location, ResourceBundle resources) {
        // Récupérez la commande actuelle à partir de la base de données
        ServiceCommande serviceCommande = new ServiceCommande();
        Commande commandeActuelle = null;
        try {
            commandeActuelle = serviceCommande.getCommande("dida16");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        refCommande = String.valueOf(commandeActuelle.getRefCommande()); // Obtenez la référence de la commande actuelle
        this.produits.addAll(this.getData());

        // Calculer la somme des prix des produits
        double totalPrix = 0.0;
        for (Produit produit : this.produits) {
            totalPrix += produit.getPrix();
        }

        // Mettre à jour le prix de la commande
        try {
            serviceCommande.updatePrixCommande(Integer.parseInt(refCommande), totalPrix);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < this.produits.size(); ++i) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ProdCom.fxml")); // Chemin vers le fichier FXML de l'élément
            AnchorPane anchorPane = null;
            try {
                anchorPane = (AnchorPane) fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ProdCom prodCom = fxmlLoader.getController();
            prodCom.setData(this.produits.get(i), iListener);

            // Ajoutez l'élément à la même ligne
            grid.add(anchorPane, i, row);

            // Ajustez les marges pour l'espacement
            GridPane.setMargin(anchorPane, new Insets(10));
        }
        refCommandeDesProd.setText("Réference de commande: "+refCommande);
        total.setText(String.valueOf(totalPrix)+" Dt");
        refresh.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> refresh());
    }

    @FXML
    private void refresh() {
        grid.getChildren().clear(); // Effacez tous les éléments actuels de la grille
        this.produits.clear(); // Effacez toutes les données actuelles

        // Rechargez les données et ajoutez-les à la grille
        this.produits.addAll(this.getData());
        for (int i = 0; i < this.produits.size(); ++i) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ProdCom.fxml")); // Chemin vers le fichier FXML de l'élément
            AnchorPane anchorPane = null;
            try {
                anchorPane = (AnchorPane) fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ProdCom prodCom = fxmlLoader.getController();
            prodCom.setData(this.produits.get(i), iListener);

            // Ajoutez l'élément à la même ligne
            grid.add(anchorPane, i, row);

            // Ajustez les marges pour l'espacement
            GridPane.setMargin(anchorPane, new Insets(10));

        }
    }
    @FXML
    void goBack(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/store.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
              } catch (IOException e) {
            e.printStackTrace();

        }

    }
    @FXML
    private void annulerCommande() {
        // Supprimez la commande de la base de données
        ServiceCommande serviceCommande = new ServiceCommande();
        try {
            serviceCommande.supprimer(refCommande);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        // Redirigez l'utilisateur vers la page d'accueil ou une autre page appropriée
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/store.fxml"));
            Scene scene = annuler.getScene();
           scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


