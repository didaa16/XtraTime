package controllers;

import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import services.ServiceCommande;
import services.ServiceCommandeProduit;
import services.ServiceProduit;

import java.io.IOException;
import java.sql.SQLException;

public class CommandeController {
    @FXML
    private Button liv;
    @FXML
    private Button liv1;
    @FXML
    private HBox back;


    @FXML
    private TextField nbrCom;

    @FXML
    private TableView<Commande> tableCommande;
    @FXML
    private TableColumn<Commande, Integer> c_refC;

    @FXML
    private TableColumn<Commande, String> c_pseudo;

    @FXML
    private TableColumn<Commande, Status> c_status;

    @FXML
    private TableColumn<Commande, String> c_total;


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

    private ServiceCommande serviceCommande;
    private ServiceCommandeProduit serviceCommandeProduit;
    @FXML
    private ComboBox<Status> statusFilterComboBox;
    @FXML
    private AnchorPane page;


    @FXML
    public void initialize() throws SQLException {
        try {
            // Initialiser le service de commande
            serviceCommande = new ServiceCommande();
            serviceCommandeProduit = new ServiceCommandeProduit();

            // Charger les commandes dans la tableCommande
            ObservableList<Commande> commandes = FXCollections.observableList(serviceCommande.afficher());
            tableCommande.setItems(commandes);

            // Initialiser les colonnes de la table de commandes
            c_refC.setCellValueFactory(new PropertyValueFactory<>("refCommande"));
            c_pseudo.setCellValueFactory(new PropertyValueFactory<>("idUser"));
            c_status.setCellValueFactory(new PropertyValueFactory<>("status"));
            c_total.setCellValueFactory(new PropertyValueFactory<>("prix"));

            // Ajouter un élément vide à la liste des éléments du ComboBox
            statusFilterComboBox.getItems().add(null);
            // Ajouter tous les autres éléments de l'énumération Status
            statusFilterComboBox.getItems().addAll(Status.values());

            // Sélectionner l'élément vide par défaut
            statusFilterComboBox.getSelectionModel().selectFirst();

            // Ajoutez un gestionnaire d'événements pour le ComboBox
            statusFilterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                filterCommandes();
            });

            // Charger les produits dans la tableProduits lorsque la commande est sélectionnée
            tableCommande.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    try {
                        ObservableList<Produit> produits = FXCollections.observableList(serviceCommandeProduit.getProduitsByRefCommande(String.valueOf(newValue.getRefCommande())));
                        listeProduit.setItems(produits);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Initialiser les colonnes de la table de produits
            c_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            c_reference.setCellValueFactory(new PropertyValueFactory<>("ref"));
            c_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            c_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            c_taille.setCellValueFactory(new PropertyValueFactory<>("taille"));
            c_typesport.setCellValueFactory(new PropertyValueFactory<>("typeSport"));
            c_marque.setCellValueFactory(new PropertyValueFactory<>("marque"));
            c_quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            c_image.setCellValueFactory(new PropertyValueFactory<>("image"));
            // Afficher le nombre de commandes
            afficherNombreCommandes(); // Ajoutez cette ligne pour appeler la méthode afficherNombreCommandes


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    @FXML
    private void afficherNombreCommandes() {

        int nombreCommandes =0;
        try {
            nombreCommandes = serviceCommande.Count();
            nbrCom.setText(String.valueOf(nombreCommandes));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void livCom(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LivreurCommande.fxml"));
            liv.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    @FXML
    void livCom2(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Client.fxml"));
            liv.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println(e.getMessage());
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
    private void goBack() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterProduit.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void filterCommandes() {
        // Récupérez le statut sélectionné
        Status selectedStatus = statusFilterComboBox.getValue();

        // Appliquez le filtre sur la liste des commandes
        ObservableList<Commande> filteredCommandes = FXCollections.observableArrayList();
        try {
            if (selectedStatus == null) {
                // Si aucun statut n'est sélectionné, affichez toutes les commandes
                filteredCommandes.addAll(serviceCommande.afficher());
            } else {
                // Sinon, affichez uniquement les commandes avec le statut sélectionné
                for (Commande commande : serviceCommande.afficher()) {
                    if (commande.getStatus() == selectedStatus) {
                        filteredCommandes.add(commande);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Mettez à jour la table des commandes avec les commandes filtrées
        tableCommande.setItems(filteredCommandes);
    }

}

