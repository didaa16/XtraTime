package controllers.local;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
import services.local.ServiceTerrain;
import entities.local.terrain;
public class ModController {



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
    private ImageView img;
    private  String imagePath;
    @FXML
    private Button back;
    @FXML
    private Button charger;
    @FXML
    private TextField dp;
    private ServiceTerrain serviceTerrain;
    private AffTController affTController; // Attribut pour stocker la référence au contrôleur AfficherPackController

    private terrain terrain;
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
                img.setImage(newImage);
                System.out.println("URL de l'image : " + imagePath);
            }
        });
    }
    @FXML
    void back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FxmlLocal/AffTerrain.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }




    public void setAffTController (AffTController affTController) {
        this.affTController = affTController;
    }

    public void setServiceTerrain(ServiceTerrain serviceTerrain) {
        this.serviceTerrain = serviceTerrain;
    }



    public void initData(terrain terrain)
    {
        this.terrain = terrain;
        re.setText(String.valueOf(terrain.getRef()));
        no.setText(terrain.getNom());
        cp.setText(String.valueOf(terrain.getCapacite()));
        tp.setText(terrain.getType());
        pr.setText(String.valueOf(terrain.getPrix()));
        dp.setText(terrain.getDisponibilite());
        String imageUrl1 = terrain.getImg();

        try {
            Image image = new Image(imageUrl1);
            img.setImage(image);
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
    private static boolean isValidCapacite(String capacite) {
        // Utiliser une expression régulière pour vérifier si la chaîne est composée uniquement de chiffres
        return capacite.matches("\\d+");
    }
    private boolean isValidType(String type) {
        return type.equals("football") || type.equals("handball") || type.equals("basketball") || type.equals("volleyball") || type.equals("tennis");
    }
    @FXML
    void sauvgarder(ActionEvent event) {
        if (serviceTerrain == null) {
            showAlert("Erreur", "Serviceterrain non initialisé", "Le serviceTerrain n'est pas initialisé.", Alert.AlertType.ERROR);
            return;
        }
        if ( no.getText().isEmpty() || cp.getText().isEmpty() || tp.getText().isEmpty() || pr.getText().isEmpty() || dp.getText().isEmpty() ) {
            showAlert("Erreur", "Champs vides", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }
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
        // Mettre à jour les données du pack avec les nouvelles valeurs des champs

        terrain.setRef(Integer.parseInt(re.getText()));
        terrain.setNom(no.getText());
        terrain.setCapacite(Integer.parseInt(cp.getText()));
        terrain.setType(tp.getText());
        terrain.setPrix(Integer.parseInt(pr.getText()));
        terrain.setDisponibilite(dp.getText());
        terrain.setImg(imagePath);
        // Enregistrer les modifications dans la base de données ou dans votre service
        try {
            serviceTerrain.modifier(terrain);
           // Appel de la méthode pour modifier le pack dans la base de données
            if (affTController == null) {
                System.out.println("*");
            } else {
               affTController.refreshTable();
            }
// Rafraîchir la TableView dans le contrôleur AfficherPackController
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la sauvegarde des modifications.", e.getMessage(), Alert.AlertType.ERROR);
             // Quitte la méthode si une erreur se produit lors de la mise à jour de la base de données
        }

        // Fermer la fenêtre de modification
    }
}
