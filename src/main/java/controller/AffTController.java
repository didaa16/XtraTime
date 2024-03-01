package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entities.terrain;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceTerrain;
public class AffTController {
    ServiceTerrain ServiceTerrain = new ServiceTerrain();



    @FXML
    private TableView<terrain> tv_t;

    @FXML
    private TableColumn<terrain, Integer> col_r;

    @FXML
    private TableColumn<terrain, String> col_n;

    @FXML
    private TableColumn<terrain,Integer > col_c;

    @FXML
    private TableColumn<terrain, String> col_t;

    @FXML
    private TableColumn<terrain, Integer> col_p;

    @FXML
    private TableColumn<terrain, String> col_d;

    @FXML
    private TableColumn<terrain, String> col_i;
    @FXML
    private Button back;
    @FXML
    void back(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AddTerrain.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void modt(ActionEvent event) {
        terrain terrain = tv_t.getSelectionModel().getSelectedItem();
        if (terrain != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierT.fxml"));
                Parent root = loader.load();
                ModController controller = loader.getController();

                // Passer le pack sélectionné au contrôleur de modification
                controller.initData(terrain);

                // Passer l'instance de ServicePack au contrôleur de modification
                controller.setServiceTerrain(ServiceTerrain);

                // Remplacer le contenu de la fenêtre actuelle avec celui de l'interface de modification
                tv_t.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de l'ouverture de l'interface de modification", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun pack sélectionné", "Veuillez sélectionner un pack à modifier.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void supt(ActionEvent event) {
        terrain terrain = tv_t.getSelectionModel().getSelectedItem();
        if (terrain != null) {
            try {
                ServiceTerrain.supprimer(terrain.getId());
                tv_t.getItems().remove(terrain);
                showAlert("Succès", "terrain supprimé avec succès.", "Le terrain a été supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la suppression du pack.", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun terrain sélectionné", "Veuillez sélectionner un terrain à supprimer.", Alert.AlertType.WARNING);
        }
    }
    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    public void refreshTable() {
        try {
            ObservableList<terrain> terrains = FXCollections.observableList(ServiceTerrain.afficher());
            tv_t.setItems(terrains);
        } catch (Exception e) {
            System.out.println("An error occurred while refreshing table: " + e.getMessage());
        }
    }

    @FXML
    void initialize() {
        try {
            ObservableList<terrain> terrains= FXCollections.observableList(ServiceTerrain.afficher());
            tv_t.setItems(terrains);
            col_r.setCellValueFactory(new PropertyValueFactory<>("ref"));
            col_n.setCellValueFactory(new PropertyValueFactory<>("nom"));
            col_c.setCellValueFactory(new PropertyValueFactory<>("capacite"));
            col_t.setCellValueFactory(new PropertyValueFactory<>("type"));
            col_p.setCellValueFactory(new PropertyValueFactory<>("prix"));
            col_d.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
            col_i.setCellValueFactory(new PropertyValueFactory<>("img"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
