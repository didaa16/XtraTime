package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import services.ServicePack;
import entities.Pack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javafx.scene.control.Alert;

public class AjouterPackController {

    @FXML
    private Button AB;

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

    private String imagePath;
    @FXML
    private Button upload;


    @FXML
    private void initialize() {
        // Event listener for upload button
        upload.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            }
        });
    }

    @FXML
    private void ajouterPack(ActionEvent event) {
        String packNom = nom.getText();
        String packDescription = desc.getText();

        String reductionString = red.getText();
        try {
            int packReduction = Integer.parseInt(reductionString);
            if (packReduction < 0 || packReduction > 100) {
                showAlert("Erreur", "Réduction invalide", "La réduction doit être un nombre entre 0 et 100.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Réduction invalide", "La réduction doit être un nombre entier entre 0 et 100.", Alert.AlertType.ERROR);
            return;
        }
        if (validerNomPack(packNom)) {
            try {
                ServicePack servicePack = new ServicePack();
                servicePack.ajouter(new Pack(packNom, packDescription, imagePath, Integer.parseInt(reductionString)));
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de l'ajout du pack.");
            }
        }
        else {
            // Affichez un message d'erreur indiquant que le nom du pack est invalide
            showAlert("Erreur", "Nom de pack invalide", "Le nom du pack doit contenir uniquement des lettres.", Alert.AlertType.ERROR);
        }


        if (packNom.isEmpty() || packDescription.isEmpty() || red.getText().isEmpty() || imagePath.isEmpty()) {
            // Handle empty fields
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }


    }






    private void clearFields() {
        nom.clear();
        desc.clear();
        red.clear();
        imageView.setImage(null);
        imagePath = null;
    }
    @FXML
    void navigate(ActionEvent event) {
        try {
            Parent root= FXMLLoader.load(getClass().getResource("/AfficheP.fxml"));
            nom.getScene().setRoot(root);
            //  Scene scene= new Scene(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void switchToAjoutC(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherA.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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

    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void switchToAjout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/HomeStats.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


}
