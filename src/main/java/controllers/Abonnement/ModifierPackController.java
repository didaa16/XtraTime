package controllers.Abonnement;

import java.io.IOException;
import java.sql.SQLException;
import entities.Abonnement.Pack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import java.net.MalformedURLException;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import services.Abonnement.ServicePack;
import javafx.stage.Stage; // Importez la classe Stage depuis javafx.stage

import javafx.stage.FileChooser;

public class ModifierPackController {
    private ServicePack servicePack;


    @FXML
    private Button Back;

    @FXML
    private TextArea desc;

    @FXML
    private TextField id;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField nom;

    @FXML
    private TextField red;

    @FXML
    private Button upload;

    private Pack pack;
    @FXML
    private TableView<Pack> tabV;
    private File newImageFile;



    private AfficherPackController afficherPackController; // Attribut pour stocker la référence au contrôleur AfficherPackController

    public void setAfficherPackController(AfficherPackController afficherPackController) {
        this.afficherPackController = afficherPackController;
    }

    public void setPack(Pack pack) {
        ObservableList<Pack> produits = FXCollections.observableList(servicePack.afficher());
        tabV.setItems(produits);
        // Définir les valeurs des champs de l'interface utilisateur avec les attributs du pack

        nom.setText(pack.getNom());
        desc.setText(pack.getDescription());
        red.setText(String.valueOf(pack.getReduction()));

        // Récupérer l'URL de l'image du pack
        String imageUrl = pack.getImage();

        // Vérifier si l'URL de l'image n'est pas vide ou nulle, puis charger et afficher l'image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageView.setImage(image);
        }
    }


    public void setServicePack(ServicePack servicePack) {
        this.servicePack = servicePack;
    }

    public void initData(Pack pack) {
        this.pack = pack;

        nom.setText(pack.getNom());
        desc.setText(pack.getDescription());
        red.setText(String.valueOf(pack.getReduction()));
        String imagePath = pack.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                // Créer un objet File avec le chemin d'accès de fichier local
                File file = new File(imagePath);
                // Convertir le fichier en URL file:
                String fileUrl = file.toURI().toURL().toString();
                Image image = new Image(fileUrl);
                imageView.setImage(image);
            } catch (MalformedURLException e) {
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
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
    void sauvegarder(ActionEvent event) {
        if (servicePack == null) {
            showAlert("Erreur", "ServicePack non initialisé", "Le servicePack n'est pas initialisé.", Alert.AlertType.ERROR);
            return;
        }

        // Récupérer les valeurs des champs
        String packNom = nom.getText();
        String packDescription = desc.getText();
        String packReductionStr = red.getText();

        // Contrôle de saisie pour le nom du pack
        if (!validerNomPack(packNom)) {
            showAlert("Erreur", "Nom de pack invalide", "Le nom du pack doit contenir uniquement des lettres.", Alert.AlertType.ERROR);
            return;
        }

        // Contrôle de saisie pour la réduction
        if (!validerReduction(packReductionStr)) {
            showAlert("Erreur", "Réduction invalide", "La réduction doit être un nombre entier compris entre 0 et 100.", Alert.AlertType.ERROR);
            return;
        }

        // Convertir la réduction en entier
        int packReduction = Integer.parseInt(packReductionStr);

        // Mettre à jour les données du pack avec les nouvelles valeurs des champs
       // Mise à jour de idP
        pack.setNom(packNom);
        pack.setDescription(packDescription);
        pack.setReduction(packReduction);

        // Enregistrer les modifications dans la base de données ou dans votre service
        try {
            // Modify the image if a new one is selected
            if (newImageFile != null) {
                String newImagePath = newImageFile.getAbsolutePath();
                pack.setImage(newImagePath);
            }

            servicePack.modifier(pack); // Appel de la méthode pour modifier le pack dans la base de données

            if (afficherPackController != null) {
                afficherPackController.refreshTable();
            } else {
                System.out.println("AfficherPackController est null, impossible de rafraîchir la table.");
            }
            // Rafraîchir la TableView dans le contrôleur AfficherPackController
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la sauvegarde des modifications.", e.getMessage(), Alert.AlertType.ERROR);
            return; // Quitte la méthode si une erreur se produit lors de la mise à jour de la base de données
        }

        // Fermer la fenêtre de modification
    }



    @FXML
    void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXMLAbonnement/AfficheP.fxml"));
            Scene scene = Back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    void upload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                // Assign selected image file to newImageFile
                newImageFile = selectedFile;

                String imagePath = selectedFile.toURI().toString();
                Image newImage = new Image(imagePath);
                imageView.setImage(newImage);
                System.out.println("URL de l'image : " + imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private boolean validerNomPack(String nom) {
        // Expression régulière pour vérifier si le nom contient uniquement des lettres
        String regex = "^[a-zA-Z]+$";
        // Créer un objet Pattern à partir de l'expression régulière
        Pattern pattern = Pattern.compile(regex);
        // Créer un objet Matcher à partir du nom fourni
        Matcher matcher = pattern.matcher(nom);
        // Vérifier si le nom correspond à l'expression régulière
        return matcher.matches();
    }
    private boolean validerReduction(String reductionString) {
        // Vérifier si la chaîne est vide
        if (reductionString.isEmpty()) {
            return false;
        }

        // Vérifier si la chaîne contient uniquement des chiffres et si elle est inférieure ou égale à 100
        try {
            int reduction = Integer.parseInt(reductionString);
            return reduction >= 0 && reduction <= 100;
        } catch (NumberFormatException e) {
            return false; // La chaîne ne peut pas être convertie en un nombre valide
        }
    }


}
