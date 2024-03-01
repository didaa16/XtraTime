package controllers;

import entities.Commande;
import entities.Produit;
import entities.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceCommande;
import services.ServiceCommandeProduit;
import services.ServiceProduit;

import java.sql.SQLException;
import java.util.List;

public class ClientController {

    @FXML
    private Button ajt;

    @FXML
    private TableView<Produit> listeProduit;

    @FXML
    private TableColumn<Produit, String> c_description;

    @FXML
    private TableColumn<Produit, String> c_image;

    @FXML
    private TableColumn<Produit, String> c_marque;

    @FXML
    private TableColumn<Produit, String> c_nom;

    @FXML
    private TableColumn<Produit, Double> c_prix;

    @FXML
    private TableColumn<Produit, Integer> c_quantite;

    @FXML
    private TableColumn<Produit, String> c_taille;

    @FXML
    private TableColumn<Produit, String> c_typesport;

    @FXML
    private TableView<Commande> tablecom;

    @FXML
    private TableColumn<Commande, Integer> c_ref;

    @FXML
    private TableColumn<Commande, Integer> c_refc;

    @FXML
    private TableColumn<Commande, String> c_reference;

    @FXML
    private TableColumn<Commande, String> mar; // marque

    @FXML
    private TableColumn<Commande, String> desc; // description

    @FXML
    private TableColumn<Commande, String> nom;

    @FXML
    private TableColumn<Commande, Double> pr; // prix

    @FXML
    private TableColumn<Commande, String> tail; // taille

    @FXML
    private Button save;

    private Commande currentCommande;

    private ServiceProduit serviceProduit = new ServiceProduit();
    private ServiceCommande serviceCommande = new ServiceCommande();
    private ServiceCommandeProduit serviceCommandeProduit = new ServiceCommandeProduit();

    public ClientController() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {
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

    private void refreshCommandeTable() throws SQLException {
        ObservableList<Commande> commandes = FXCollections.observableList(serviceCommande.afficher());
        tablecom.setItems(commandes);
    }

    @FXML
    public void afficherProduitsCommande() {
        Commande commande = tablecom.getSelectionModel().getSelectedItem();
        if (commande != null) {
            try {
                List<Produit> produits = serviceCommandeProduit.listerP("WHERE refCommande = '" + commande.getRefCommande() + "'");
                // Afficher les produits dans la tablecom
                tablecom.getItems().clear();
                tablecom.getItems().addAll((Commande) produits);
            } catch (SQLException e) {
                System.out.println("Erreur lors de la récupération des produits de la commande : " + e.getMessage());
            }
        } else {
            System.out.println("Veuillez sélectionner une commande.");
        }}

    public void ajouterproduit(ActionEvent actionEvent) {
        Produit produit = listeProduit.getSelectionModel().getSelectedItem();
        if (produit != null) {
            if (currentCommande == null) {
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
        }
    }


    public void ajoutercommande(ActionEvent actionEvent) {
        if (currentCommande != null) {
            try {
                // Enregistrer la commande dans la base de données
                serviceCommande.ajouter(currentCommande);
                // Rafraîchir la table des commandes
                refreshCommandeTable();
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'enregistrement de la commande : " + e.getMessage());
            }
        } else {
            System.out.println("Aucune commande à enregistrer.");
        }
    }
}
