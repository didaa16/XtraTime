package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import entities.terrain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceComplexe;
import services.ServiceTerrain;

public class AddTController {
    private Image image;
    ServiceTerrain ServiceTerrain = new ServiceTerrain();

    @FXML
    private TextField re;

    @FXML
    private TextField no;

    @FXML
    private TextField cp;

    @FXML
    private TextField tp;

    @FXML
    private TextField pr;

    @FXML
    private TextField dp;

    @FXML
    private ImageView img;
    @FXML
    private Button back;
    @FXML
    private Button upload;
    private String imagePath;
    @FXML
    void initialize() {

        // Event listener for upload button
        upload.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            // fileChooser.getExtensionFilters().addAll(
            //     new ExtensionFilter("Image Files", ".png", ".jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
                Image image = new Image(selectedFile.toURI().toString());
                img.setImage(image);
            }
        });


    }
    @FXML
    void back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/inscription.fxml"));
            Scene scene = back.getScene();
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
    private boolean isValidType(String type) {
        return type.equals("football") || type.equals("handball") || type.equals("basketball") || type.equals("volleyball") || type.equals("tennis");
    }
    private static boolean isValidCapacite(String capacite) {
        // Utiliser une expression régulière pour vérifier si la chaîne est composée uniquement de chiffres
        return capacite.matches("\\d+");
    }

    @FXML
    void addT(ActionEvent event) {
        // Vérifier que tous les champs sont remplis
        if ( no.getText().isEmpty() || cp.getText().isEmpty() || tp.getText().isEmpty() || pr.getText().isEmpty() || dp.getText().isEmpty() || imagePath.isEmpty()) {
            showAlert("Erreur", "Champs vides", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }
        // Vérifier si le type est valide
        String type = tp.getText().toLowerCase(); // Convertir en minuscules pour une comparaison insensible à la casse
        if (!isValidType(type)) {
            showAlert("Erreur", "Type invalide", "Le type doit être football, handball, basketball, volleyball ou tennis.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidCapacite(cp.getText())) {
            showAlert("Erreur", "capacite invalide", "La capacite doit être number.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidCapacite(pr.getText())) {
            showAlert("Erreur", "prix invalide", "Le prix doit être number.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidCapacite(dp.getText())) {
            showAlert("Erreur", "disponibilite invalide", "La disponibilite doit être number.", Alert.AlertType.ERROR);
            return;
        }
        try {
            ServiceTerrain.ajouter(new terrain( no.getText(),Integer.parseInt(cp.getText()) , tp.getText(), Integer.parseInt(pr.getText()), dp.getText(),  imagePath));

            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("succes");
            alert.setContentText("personne ajoute");
            alert.showAndWait(); }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @FXML
    void afficherT(ActionEvent event) {
        try {
            Parent root= FXMLLoader.load(getClass().getResource("/AffTerrain.fxml"));
            no.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    }





