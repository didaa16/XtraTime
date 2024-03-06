package controllers.store;

import entities.store.Commande;
import entities.store.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import services.store.ServiceCommande;
import services.store.ServiceCommandeProduit;
import tests.IListener;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CommandeJdida implements Initializable {
    private IListener iListener;
    private List<Produit> produits = new ArrayList<>();
    @FXML
    private ImageView refresh;
    @FXML
    private Button annuler;
    @FXML
    private Label refCommandeDesProd;

    @FXML
    private ImageView back;
    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField total;
    @FXML
    private AnchorPane page;
    @FXML
    private Button payer;
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

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Récupérez la commande actuelle à partir de la base de données
        ServiceCommande serviceCommande = new ServiceCommande();
        Commande commandeActuelle = null;
        try {
            commandeActuelle = serviceCommande.getCommande("dida16");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (commandeActuelle != null) {
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
            fxmlLoader.setLocation(getClass().getResource("/fxmlStore/imagesss/ProdCom.fxml")); // Chemin vers le fichier FXML de l'élément
            AnchorPane anchorPane = null;
            try {
                anchorPane = (AnchorPane) fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ProduitDansCom produitDansCom = fxmlLoader.getController();
            produitDansCom.setData(this.produits.get(i));

            // Ajoutez l'élément à la même ligne
            grid.add(anchorPane, i, row);

            // Ajustez les marges pour l'espacement
            GridPane.setMargin(anchorPane, new Insets(10));
        }
        refCommandeDesProd.setText("Réference de commande: "+refCommande);
        total.setText(String.valueOf(totalPrix)+" Dt");
        //refresh.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> refresh());
    }else { showAlert("", "Vous n'avez pas de commande pour le moment", "commandez chez nous et vous ne le regretterez pas!", Alert.AlertType.INFORMATION);
        }
        refresh();
    }

    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }



    @FXML
    void goBack(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/imagesss/store.fxml"));
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

        // Redirigez l'utilisateur vers la page d'accueil ou une autre page appropriée
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/imagesss/store.fxml"));
            Scene scene = annuler.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void refresh() {

        this.grid.getChildren().clear(); // Effacez tous les éléments actuels de la grille
        this.produits.clear(); // Effacez toutes les données actuelles


        // Rechargez les données et ajoutez-les à la grille
        this.produits.addAll(this.getData());
        for (int i = 0; i < this.produits.size(); ++i) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxmlStore/imagesss/ProdCom.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane = (AnchorPane) fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ProduitDansCom prodCom = fxmlLoader.getController();
            prodCom.setData(this.produits.get(i));

            // Ajoutez l'élément à la même ligne
            grid.add(anchorPane, i, row);

            // Ajustez les marges pour l'espacement
            GridPane.setMargin(anchorPane, new Insets(10));



        }
        updateTotalPrice(); // Mettre à jour le champ total

    }
    public void updateTotalPrice() {
        double totalPrix = 0.0;
        for (Produit produit : this.produits) {
            totalPrix += produit.getPrix();
        }
        total.setText(String.valueOf(totalPrix) + " Dt");
    }

    @FXML
    void payment(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/imagesss/paiement.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}