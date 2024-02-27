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
    ServiceProduit serviceProduit =new ServiceProduit();
    ServiceCommande serviceCommande =new ServiceCommande();
    ServiceCommandeProduit serviceCommandeProduit=new ServiceCommandeProduit();
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
       /* // Initialiser les colonnes de la table de commandes
        c_ref.setCellValueFactory(new PropertyValueFactory<>("refCommande"));
        c_reference.setCellValueFactory(new PropertyValueFactory<>("ref"));
        c_refc.setCellValueFactory(new PropertyValueFactory<>("refCommande"));
        mar.setCellValueFactory(new PropertyValueFactory<>("marque"));
        desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pr.setCellValueFactory(new PropertyValueFactory<>("prix"));
        tail.setCellValueFactory(new PropertyValueFactory<>("taille"));*/

        // Charger les commandes dans la tableCommande
      //  refreshCommandeTable();
    }

    private void refreshCommandeTable() throws SQLException {
        ObservableList<Commande> commandes = FXCollections.observableList(serviceCommande.afficher());
        tablecom.setItems(commandes);
    }

    public void ajouterproduit(ActionEvent actionEvent) {
        // Récupérer le produit sélectionné
        Produit produit = listeProduit.getSelectionModel().getSelectedItem();
        if (produit != null) {
            // Ajouter le produit à la commande
            Commande commande = new Commande();
            commande.addProduit(produit);
            // Mettre à jour le prix total de la commande
            commande.setPrix(commande.getPrix() + produit.getPrix());
            // Ajouter la commande à la table tablecom
            tablecom.getItems().add(commande);
        }
    }

    public void ajoutercommande(ActionEvent actionEvent) throws SQLException {
        // Créer une nouvelle commande
        Commande commande = new Commande();
        commande.setRefCommande(0); // Laissez la base de données générer l'ID
        commande.setPrix(0.0); // Prix initial
        commande.setStatus(Status.enAttente); // Status initial
        commande.setIdUser("idUser"); // ID de l'utilisateur actuel

        // Ajouter la commande à la base de données
        serviceCommande.ajouter(commande);

        // Rafraîchir la table des commandes
        refreshCommandeTable();
    }
}
