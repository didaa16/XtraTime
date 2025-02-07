package controllers.store;


import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.LineSeparator;
import entities.store.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import services.store.ServiceCommande;
import services.store.ServiceCommandeProduit;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;



public class LivreurCommandeController {

    @FXML
    private Button btnpdf;

    @FXML
    private Button btnsave;

    @FXML
    private ImageView retour;
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
    private TextField nbrA;

    @FXML
    private TextField nbrCOURS;

    @FXML
    private TextField nbrliv;
    @FXML
    private ComboBox<Status> stat;


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
            //  afficherNombreCommandes(); // Ajoutez cette ligne pour appeler la méthode afficherNombreCommandes
            // Afficher le nombre de commandes
//            nbrA.setText(String.valueOf(serviceCommande.countEnAttente()));
//            nbrCOURS.setText(String.valueOf(serviceCommande.countEnCours()));
//            nbrliv.setText(String.valueOf(serviceCommande.countLivre()));
            // Afficher le nombre de commandes
            afficherNombreCommandes(); // Ajoutez cette ligne pour appeler la méthode afficherNombreCommandes
            stat.setItems(FXCollections.observableArrayList(Status.values()));



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void afficherNombreCommandes() {
        try {
            // Mettre à jour le nombre de commandes en attente
            nbrA.setText(String.valueOf(serviceCommande.countEnAttente()));

            // Mettre à jour le nombre de commandes en cours
            nbrCOURS.setText(String.valueOf(serviceCommande.countEnCours()));

            // Mettre à jour le nombre de commandes livrées
            nbrliv.setText(String.valueOf(serviceCommande.countLivre()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void enregistrerS(ActionEvent event) throws SQLException {
        // Récupérer la commande sélectionnée
        Commande commande = tableCommande.getSelectionModel().getSelectedItem();
        if (commande != null) {
            // Mettre à jour le statut de la commande dans la base de données
            commande.setStatus(stat.getValue());
            try {
                serviceCommande.modifier(commande);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Rafraîchir la table des commandes
            tableCommande.refresh();

            // Mettre à jour le nombre de commandes
            afficherNombreCommandes();




        }
    }

    @FXML
    void retourner(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/GestionCommande.fxml"));
            Scene scene = retour.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }



    //dependance itext7-core
    @FXML
    void exporterPDF(ActionEvent event) {
        // Récupérer la commande sélectionnée
        Commande commande = tableCommande.getSelectionModel().getSelectedItem();
        if (commande != null) {
            // Récupérer les produits de la commande
            ObservableList<Produit> produits = listeProduit.getItems();

            // Générer un nom de fichier unique pour le PDF
            String fileName = "commande_" + commande.getRefCommande() + ".pdf";

            // Créer un document PDF
            try (PdfWriter writer = new PdfWriter(fileName);
                 PdfDocument pdf = new PdfDocument(writer);
                 Document document = new Document(pdf)) {

                // Ajouter le titre du document
                Paragraph title = new Paragraph("Commande " + commande.getRefCommande());
                title.setFontSize(24);
                title.setBold();
                //title.setTextAlignment(CENTER);
                document.add(title);

                // Ajouter les informations de la commande
//                Paragraph statutParagraph = new Paragraph("Statut: " + commande.getStatus());
//                statutParagraph.setBold(); // Set Statut in bold
//                statutParagraph.setFontSize(20);
//                document.add(statutParagraph);

                Paragraph totalParagraph = new Paragraph("Total: " + commande.getPrix()+ " DT");
                totalParagraph.setBold(); // Set Total in bold
                totalParagraph.setFontSize(20);

                document.add(totalParagraph);

                // Ajouter les produits de la commande
               // document.add(new Paragraph("Produits:"));
                for (Produit produit : produits) {
                    document.add(new Paragraph("produit: " + produit.getNom()));
                    document.add(new Paragraph("Prix: " + produit.getPrix()+ " DT"));
                    document.add(new Paragraph("Description: " + produit.getDescription()));
                    document.add(new Paragraph("Marque: " + produit.getMarque()));
                    document.add(new Paragraph("Taille: " + produit.getTaille()));
                    document.add(new LineSeparator(new SolidLine()));
                }

                // Ouvrir le fichier PDF avec le lecteur PDF par défaut
                Desktop.getDesktop().open(new File(fileName));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
