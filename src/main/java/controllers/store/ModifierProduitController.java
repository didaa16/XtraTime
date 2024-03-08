package controllers.store;

import entities.store.Marque;
import entities.store.Produit;
import entities.store.TypeSport;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.store.ServiceProduit;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifierProduitController {
    ServiceProduit serviceProduit = new ServiceProduit();
    @FXML
    private ImageView imageView;

    @FXML
    private HBox back;
    @FXML
    private HBox backCom;

    @FXML
    private Button btnsave;
    @FXML
    private Button chargerI;

    @FXML
    private TextField description;

    @FXML
    private ComboBox<Marque> marque;

    @FXML
    private TextField nomProduit;

    @FXML
    private TextField prix;

    @FXML
    private TextField quantite;

    @FXML
    private TextField refProduit;

    @FXML
    private TextField taille;

    @FXML
    private ComboBox<TypeSport> typeSport;

    @FXML
    private TableView<Produit> listeProduit;
    private Produit produit;
    private AfficherProduitController afficherProduitController;
    @FXML
    void initialize() {
        // Initialiser les champs de texte
        refProduit.setText("");
        nomProduit.setText("");
        description.setText("");
        prix.setText("");
        marque.setValue(null);
        taille.setText("");
        typeSport.setValue(null);
        quantite.setText("");

        // Initialiser l'ImageView
        imageView.setImage(null);

            marque.getItems().addAll(Marque.values());
            typeSport.getItems().addAll(TypeSport.values());


    }

    public void setProduit(Produit produit) throws SQLException {
        ObservableList<Produit> produits = FXCollections.observableList(serviceProduit.afficher());
        listeProduit.setItems(produits);
        refProduit.setText(produit.getRef());
        nomProduit.setText(produit.getNom());
        description.setText(produit.getDescription());
        prix.setText(String.valueOf(produit.getPrix()));
        marque.setValue(produit.getMarque());
        taille.setText(produit.getTaille());
        typeSport.setValue(produit.getTypeSport());
        quantite.setText(String.valueOf(produit.getQuantite()));
        String imageUrl = produit.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageView.setImage(image);
        }
    }


    public void setAfficherProduitController(AfficherProduitController afficherProduitController) {
        this.afficherProduitController = afficherProduitController;
    }


    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    public void setAfficherPackController(AfficherProduitController afficherProduitController) {
        this.afficherProduitController = afficherProduitController;
    }

    public void setServiceProduit(ServiceProduit serviceProduit) {
        this.serviceProduit = serviceProduit;
    }

    public void initData(Produit produit) {
        this.produit = produit;
        refProduit.setText(produit.getRef());
        nomProduit.setText(produit.getNom());
        description.setText(produit.getDescription());
        prix.setText(String.valueOf(produit.getPrix()));
        marque.setValue(produit.getMarque());
        taille.setText(produit.getTaille());
        typeSport.setValue(produit.getTypeSport());
        quantite.setText(String.valueOf(produit.getQuantite()));

        // Charger l'image dans l'ImageView
        String imageUrl = produit.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageView.setImage(image);
        }
    }
    public void sauvegarder(javafx.event.ActionEvent actionEvent) {
        if (!verifChamps()) {
            return;
        }
        // Récupérer les données du produit à partir des champs de texte
        Produit produit = new Produit();
        produit.setRef(refProduit.getText());
        produit.setNom(nomProduit.getText());
        produit.setDescription(description.getText());
        produit.setPrix(Double.parseDouble(prix.getText()));
        produit.setMarque(marque.getValue());
        produit.setTaille(taille.getText());
        produit.setTypeSport(typeSport.getValue());
        produit.setQuantite(Integer.parseInt(quantite.getText()));
        produit.setImage(chargerI.getText());

        // Vérifier si l'image est vide
        String imageUrl = imageView.getImage().getUrl();
        if (!imageUrl.isEmpty()) {
            produit.setImage(imageUrl);
        }
        // Enregistrer les modifications dans la base de données ou dans votre service
        try {
            serviceProduit.modifier(produit); // Appel de la méthode pour modifier le produit dans la base de données
            if (afficherProduitController != null) {
                afficherProduitController.refreshTable(); // Mettre à jour la liste des produits dans le contrôleur AfficherProduitController
            } else {
                System.out.println("AfficherProduitController est null, impossible de rafraîchir la table.");
            }

            // Afficher une alerte de succès
            showAlert("Succès", "Produit modifié", "Le produit a été modifié avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la sauvegarde des modifications.", e.getMessage(), Alert.AlertType.ERROR);
            return; // Quitte la méthode si une erreur se produit lors de la mise à jour de la base de données
        }

        // Fermer la fenêtre de modification
    }

    private boolean verifChamps() {
        if (nomProduit.getText().isEmpty() || refProduit.getText().isEmpty() || description.getText().isEmpty() || prix.getText().isEmpty() || quantite.getText().isEmpty() || taille.getText().isEmpty() || typeSport.getValue() == null || marque.getValue() == null || imageView.getImage() == null) {
            showAlert("Erreur", "Erreur lors de l'ajout.", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return false;
        }


        if (!verifNomProduit() || !verifRefProduit() || !verifDescription() || !verifQuantite() || !verifPrix()) {
            return false;
        }

        return true;
    }

    private boolean verifNomProduit() {
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m = p.matcher(nomProduit.getText());
        if (m.find() && m.group().equals(nomProduit.getText())) {
            return true;
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout.", "Le nom du produit doit contenir uniquement des lettres.", Alert.AlertType.ERROR);
            return false;
        }
    }

    private boolean verifRefProduit() {
        Pattern p = Pattern.compile("RF-\\d+");
        Matcher m = p.matcher(refProduit.getText());
        if (m.find() && m.group().equals(refProduit.getText())) {
            return true;
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout.", "La référence du produit doit commencer par 'RF-' suivi d'un entier.", Alert.AlertType.ERROR);
            return false;
        }
    }


    private boolean verifDescription() {
        if (description.getText().length() >= 10) {
            return true;
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout.", "La description du produit doit contenir au moins 10 caractères.", Alert.AlertType.ERROR);
            return false;
        }
    }

    private boolean verifQuantite() {
        try {
            int qte = Integer.parseInt(quantite.getText());
            if (qte >= 0) {
                return true;
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout.", "La quantité doit être un nombre positif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Erreur lors de l'ajout.", "La quantité doit être un nombre entier.", Alert.AlertType.ERROR);
            return false;
        }
    }

    private boolean verifPrix() {
        try {
            double prx = Double.parseDouble(prix.getText());
            if (prx >= 0) {
                return true;
            } else {
                showAlert("Erreur", "Erreur lors de l'ajout.", "Le prix doit être un nombre positif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Erreur lors de l'ajout.", "Le prix doit être un nombre réel.", Alert.AlertType.ERROR);
            return false;
        }
    }


   /* @FXML
    void goBack(MouseEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherProduit.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }


    }*/

    @FXML
    private void goBack() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/AjouterProduit.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void goBackCom() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/GestionCommande.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void upload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                String imagePath = selectedFile.toURI().toString();
                javafx.scene.image.Image newImage = new Image(imagePath);
                imageView.setImage(newImage);
                System.out.println("URL de l'image : " + imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshTable() {
        try {
            ObservableList<Produit> produits = FXCollections.observableList(serviceProduit.afficher());
            listeProduit.setItems(produits);
        } catch (Exception e) {
            System.out.println("An error occurred while refreshing table: " + e.getMessage());
        }
    }



}

