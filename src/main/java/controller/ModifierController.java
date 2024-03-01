package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceComplexe;
import entities.complexe;
public class ModifierController {



    @FXML
    private TextField nom;

    @FXML
    private TextField idl;

    @FXML
    private TextField adr;

    @FXML
    private TextField tel;
    private  String imagePath;
    private  String imagePath1;
    @FXML
    private  ImageView pt;

    @FXML
    private ImageView im;
    @FXML
    private Button back;
    @FXML
    private Button charger1;
    @FXML
    private Button charger;
   // private Image image;
//private  String imageUrl;
    private  AfficherController afficherController;
    private ServiceComplexe serviceComplexe;
    private complexe complexe;

    public void setAfficherController(AfficherController afficherController) {
        this.afficherController = afficherController;
    }

    public void setServiceComplexe(ServiceComplexe serviceComplexe) {
        this.serviceComplexe = serviceComplexe;
    }
    @FXML
    void initialize() {

        // Event listener for upload button
        charger.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            // fileChooser.getExtensionFilters().addAll(
            //     new ExtensionFilter("Image Files", ".png", ".jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                 imagePath = selectedFile.toURI().toString();
                javafx.scene.image.Image newImage = new Image(imagePath);
                im.setImage(newImage);
                System.out.println("URL de l'image : " + imagePath);
            }
        });
        // Event listener for upload button
        charger1.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            // fileChooser.getExtensionFilters().addAll(
            //     new ExtensionFilter("Image Files", ".png", ".jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                imagePath1 = selectedFile.toURI().toString();
                javafx.scene.image.Image newImage = new Image(imagePath1);
                pt.setImage(newImage);
                System.out.println("URL de l'image : " + imagePath1);
            }
        });


    }
    @FXML
    void back(ActionEvent event) {
        {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/AfficherComplexe.fxml"));
                Scene scene = back.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }



  // Attribut pour stocker la référence au contrôleur AfficherPackController


    private boolean isValidPhoneNumber(String phoneNumber) {
        // Utiliser une expression régulière pour valider le numéro de téléphone
        // Par exemple, la regex suivante vérifie si le numéro a 10 chiffres et commence par un chiffre entre 0 et 9
        return Pattern.matches("\\d{8}", phoneNumber);
    }
    private static boolean isValidNom(String nom) {
        // Utiliser une expression régulière pour vérifier si la chaîne de caractères ne contient que des lettres
        // La regex ^[a-zA-Z]+$ vérifie que la chaîne contient uniquement des lettres (minuscules ou majuscules)
        return Pattern.matches("^[a-zA-Z]+$", nom);
    }
    private static boolean isValidAdresse(String adresse) {
        // Utiliser une expression régulière pour vérifier si la chaîne de caractères de l'adresse est valide
        // La regex ^[a-zA-Z0-9\s.,'-]+$ vérifie que la chaîne contient uniquement des lettres, chiffres, espaces et caractères spéciaux couramment utilisés dans les adresses
        return Pattern.matches("^[a-zA-Z0-9\\s.,'-]+$", adresse);
    }


    public void initData(complexe complexe) {
        this.complexe = complexe;
        nom.setText(complexe.getNom());
        idl.setText(complexe.getIdlocateur());
        adr.setText(complexe.getAdresse());
        tel.setText(complexe.getTel());
        // Charger l'image dans l'ImageView
        String imageUrl1 = complexe.getPatente();

        try {
            Image image = new Image(imageUrl1);
            pt.setImage(image);
        } catch (Exception e) {
            // Gérer l'erreur lors du chargement de l'image
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }

        // Charger l'image dans l'ImageView
        String imageUrl = complexe.getImage();

            try {
                Image image = new Image(imageUrl);
                im.setImage(image);
            } catch (Exception e) {
                // Gérer l'erreur lors du chargement de l'image
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
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
    void sauvgarder(ActionEvent event) {
        if (serviceComplexe == null) {
            showAlert("Erreur", "ServiceComplexe non initialisé", "Le ServiceComplexe n'est pas initialisé.", Alert.AlertType.ERROR);
            return;
        }
        if (nom.getText().isEmpty() || idl.getText().isEmpty() || adr.getText().isEmpty() || tel.getText().isEmpty() ) {
            showAlert("Erreur", "Champs vides", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidPhoneNumber(tel.getText())) {
            showAlert("Erreur", "Numéro de téléphone invalide", "Veuillez saisir un numéro de téléphone valide.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidNom(nom.getText())) {
            showAlert("Erreur", "Nom invalide", "Veuillez saisir un nom valide.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidAdresse(adr.getText())) {
            showAlert("Erreur", "adresse invalide", "Veuillez saisir une adresse valide.", Alert.AlertType.ERROR);
            return;
        }
        // Mettre à jour les données du pack avec les nouvelles valeurs des champs

        complexe.setNom(nom.getText());
        complexe.setIdlocateur(idl.getText());
        complexe.setAdresse(adr.getText());
        complexe.setTel(tel.getText());
        complexe.setPatente(imagePath1);
        complexe.setImage(imagePath);
        // Enregistrer les modifications dans la base de données ou dans votre service
        try {
            serviceComplexe.modifier(complexe);// Appel de la méthode pour modifier le pack dans la base de données
            if (afficherController != null) afficherController.refreshTable();
            else {
                System.out.println("AfficherController est null, impossible de rafraîchir la table.");
            }
// Rafraîchir la TableView dans le contrôleur AfficherPackController
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la sauvegarde des modifications.", e.getMessage(), Alert.AlertType.ERROR);
            return;// Quitte la méthode si une erreur se produit lors de la mise à jour de la base de données
        }

        // Fermer la fenêtre de modification
    }



}
