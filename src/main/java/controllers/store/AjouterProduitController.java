package controllers.store;

import entities.store.Marque;
import entities.store.Produit;
import entities.store.TypeSport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class AjouterProduitController {
    ServiceProduit serviceProduit = new ServiceProduit();

    @FXML
    private HBox back;
    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private ImageView imageView;

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
    private Button store;
    @FXML
    void initialize() {
        marque.getItems().addAll(Marque.values());
        typeSport.getItems().addAll(TypeSport.values());

    }

    @FXML
    void afficherProduit(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/AfficherProduit.fxml"));
            refProduit.getScene().setRoot(root);

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
    @FXML
    void ajouterProduit(ActionEvent event) {
        if (!verifChamps()) {
            return;
        }
        try {
            String imageUrl = imageView.getImage().getUrl();
            if (imageUrl.isEmpty()) {
                showAlert("Erreur", "Erreur lors de l'ajout.", "Veuillez sélectionner une image.", Alert.AlertType.ERROR);
                return;
            }
            if (serviceProduit.produitExistsWithSameName(nomProduit.getText())){
                Alert eventExistsAlert = new Alert(Alert.AlertType.WARNING);
                eventExistsAlert.setTitle(" \n" +
                        "Produit  double");
                eventExistsAlert.setHeaderText(null);
                eventExistsAlert.setContentText("Un produit du même nom existe déjà !");
                eventExistsAlert.showAndWait();
                return;
            }
            serviceProduit.ajouter(new Produit(refProduit.getText(), nomProduit.getText(), description.getText(), Double.parseDouble(prix.getText()), marque.getValue(), taille.getText(), typeSport.getValue(), Integer.parseInt(quantite.getText()), imageUrl));
            showAlert("Succès", "Produit ajouté", "Le produit a été ajouté avec succès.", Alert.AlertType.INFORMATION);
            refProduit.clear();
            nomProduit.clear();
            description.clear();
            prix.clear();
            marque.setValue(null);
            taille.clear();
            typeSport.setValue(null);
            quantite.clear();
            imageView.setImage(null);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout.", e.getMessage(), Alert.AlertType.ERROR);
            return;
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
                Image newImage = new Image(imagePath);
                imageView.setImage(newImage);
                System.out.println("URL de l'image : " + imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @FXML
    private void goBack() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/GestionCommande.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    void store(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/store.fxml"));

            Scene scene = store.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page ", e.getMessage(), Alert.AlertType.ERROR);

        }
    }





}
