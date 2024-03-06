package controllers.store;


import entities.store.Commande;
import entities.store.Produit;
import entities.store.Status;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;
import services.store.ServiceCommande;
import services.store.ServiceCommandeProduit;
import services.store.ServiceRating;
import tests.IListener;

import java.sql.SQLException;


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
    private ServiceRating serviceRating ;
    @FXML
    private Spinner<Integer> spinner;


    public ItemController() throws SQLException {
        this.serviceCommande = new ServiceCommande();
        this.serviceCommandeProduit = new ServiceCommandeProduit();
        this.serviceRating = new ServiceRating();

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
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0)); // Remplacez 100 par le nombre maximum de produits que vous pouvez ajouter
        initialize();

    }
    @FXML
    public void initialize() {
        if (produit != null) {
            try {
                double averageRating = serviceRating.getAverageRating(produit.getRef());
                rating.setRating(averageRating);
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer l'erreur, par exemple afficher un message à l'utilisateur
            }
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
            for (int i = 0; i < spinner.getValue(); i++) {
                // Ajouter le produit à la commande
                serviceCommandeProduit.ajouterProduitACommande(currentCommande.getRefCommande(), produit.getRef());
                System.out.println("khatfet");
                // Mettre à jour le prix total de la commande
                currentCommande.setPrix(currentCommande.getPrix() + produit.getPrix());
                // Rafraîchir la table des commandes
                refreshCommandeTable();
            }
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
        double ratingValue = rating.getRating();
        try {
            serviceRating.addOrUpdateRating("dida16", produit.getRef(), (int) ratingValue);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Votre évaluation a été enregistrée avec succès!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'enregistrement de votre évaluation.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }


}