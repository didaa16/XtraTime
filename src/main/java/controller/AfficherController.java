package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.scene.control.TextField;
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
import entities.complexe;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceComplexe;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
public class AfficherController {

    ServiceComplexe ServiceComplexe = new ServiceComplexe();
    @FXML
    private TableView<complexe> tv_complexe;

    @FXML
    private TableColumn<complexe, String> col_nom;

    @FXML
    private TableColumn<complexe, String> col_idl;

    @FXML
    private TableColumn<complexe, String> col_adr;

    @FXML
    private TableColumn<complexe, String> col_tel;

    @FXML
    private TableColumn<complexe, String> col_pt;

    @FXML
    private TableColumn<complexe, String> col_im;
    @FXML
    private Button back;
    @FXML
    private TextField searchField; // Ajoutez ce champ

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
    @FXML

    void modifier(ActionEvent event) {
        complexe complexe = tv_complexe.getSelectionModel().getSelectedItem();
        if (complexe != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierComplexe.fxml"));
                Parent root = loader.load();
                ModifierController controller = loader.getController();

                // Passer le pack sélectionné au contrôleur de modification
                controller.initData(complexe);

                // Passer l'instance de ServicePack au contrôleur de modification
                controller.setServiceComplexe(ServiceComplexe);

                // Remplacer le contenu de la fenêtre actuelle avec celui de l'interface de modification
                tv_complexe.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de l'ouverture de l'interface de modification", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun pack sélectionné", "Veuillez sélectionner un pack à modifier.", Alert.AlertType.WARNING);
        }
    }



    public void refreshTable() {
        try {
            ObservableList<complexe> complexes = FXCollections.observableList(ServiceComplexe.afficher());
            tv_complexe.setItems(complexes);
        } catch (Exception e) {
            System.out.println("An error occurred while refreshing table: " + e.getMessage());
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
    void supprimer(ActionEvent event) {
        complexe complexe = tv_complexe.getSelectionModel().getSelectedItem();
        if (complexe != null) {
            try {
                ServiceComplexe.supprimer(complexe.getRef());
                tv_complexe.getItems().remove(complexe);
                showAlert("Succès", "complexe supprimé avec succès.", "La complexe a été supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la suppression du pack.", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun complexe sélectionné", "Veuillez sélectionner une complexe à supprimer.", Alert.AlertType.WARNING);
        }
    }



    @FXML
    void initialize() {
        try {
            ObservableList<complexe> complexes = FXCollections.observableList(ServiceComplexe.afficher());
            tv_complexe.setItems(complexes);
            col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            col_idl.setCellValueFactory(new PropertyValueFactory<>("idlocateur"));
            col_adr.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            col_tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
            col_pt.setCellValueFactory(new PropertyValueFactory<>("patente"));
            col_im.setCellValueFactory(new PropertyValueFactory<>("image"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void search() {
        String keyword = searchField.getText().toLowerCase();
        ObservableList<complexe> filteredList = FXCollections.observableArrayList();

        // Si le champ de recherche est vide, rétablissez la liste complète
        if (keyword.isEmpty()) {
            try {
                tv_complexe.setItems(FXCollections.observableList(ServiceComplexe.afficher()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        // Filtrer la liste selon le mot-clé de recherche
        for (complexe complex : tv_complexe.getItems()) {
            if (complex.getNom().toLowerCase().contains(keyword) ||
                    complex.getAdresse().toLowerCase().contains(keyword) ||
                    complex.getPatente().toLowerCase().contains(keyword) ||
                    complex.getIdlocateur().toLowerCase().contains(keyword) ||
                    complex.getTel().toLowerCase().contains(keyword)) {
                filteredList.add(complex);
            }
        }
        tv_complexe.setItems(filteredList);
    }



}
