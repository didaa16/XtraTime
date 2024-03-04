package Controllers;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.scene.web.WebEngine;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.ServicePack;
import entities.Pack;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import javafx.stage.Stage; // Importez la classe Stage depuis javafx.stage

import javax.swing.*;


public class AfficherPackController {

    ServicePack servicePack = new ServicePack();

    @FXML
    private TableColumn<Pack, String> desc;

    @FXML
    private TableColumn<Pack, Integer> idP;

    @FXML
    private TableColumn<Pack, String> nom;

    @FXML
    private TableColumn<Pack, Integer> red;
    @FXML
    private TableColumn<Pack, String> img;

    @FXML
    private TableView<Pack> tabV;
    @FXML
    private Button PDF_Button;


    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        try {
            ObservableList<Pack> packs = FXCollections.observableList(servicePack.afficher());
            tabV.setItems(packs);
            idP.setCellValueFactory(new PropertyValueFactory<>("idP"));
            nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            desc.setCellValueFactory(new PropertyValueFactory<>("description"));
            red.setCellValueFactory(new PropertyValueFactory<>("reduction"));
            img.setCellValueFactory(new PropertyValueFactory<>("image"));

        } catch (Exception e) {
            System.out.println("An error occurred while initializing: " + e.getMessage());
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        Pack pack = tabV.getSelectionModel().getSelectedItem();
        if (pack != null) {
            try {
                servicePack.supprimer(pack.getIdP());
                tabV.getItems().remove(pack);
                showAlert("Succès", "Pack supprimé avec succès.", "Le pack a été supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la suppression du pack.", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun pack sélectionné", "Veuillez sélectionner un pack à supprimer.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Ajout.fxml"));
            Scene scene = backButton.getScene();
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
    @FXML
    void modifier(ActionEvent event) {
        Pack pack = tabV.getSelectionModel().getSelectedItem();
        if (pack != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierP.fxml"));
                Parent root = loader.load();
                ModifierPackController controller = loader.getController();

                // Passer le pack sélectionné au contrôleur de modification
                controller.initData(pack);

                controller.setServicePack(servicePack);
                controller.setAfficherPackController(this);

                // Vérifier si tabV est attachée à une scène avant d'accéder à sa scène
                if (tabV.getScene() != null) {
                    tabV.getScene().setRoot(root);
                } else {
                    // Si tabV n'est pas attachée à une scène, affichez une erreur
                    showAlert("Erreur", "La table n'est pas attachée à une scène", "Impossible de modifier la scène", Alert.AlertType.ERROR);
                }
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
            ObservableList<Pack> packs = FXCollections.observableList(servicePack.afficher());
            tabV.setItems(packs);
        } catch (Exception e) {
            System.out.println("An error occurred while refreshing table: " + e.getMessage());
        }
    }

    @FXML
    void Switch(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherA.fxml"));
            Scene scene = backButton.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    void changeH(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/HomeStats.fxml"));
            backButton.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void exportToExcel(ActionEvent event) {
        try {
            // Créer un nouveau classeur Excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Packs");

            // En-têtes de colonnes
            String[] headers = {"ID", "Nom", "Description", "Réduction", "Image"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Récupérer les données de la table et les écrire dans le classeur Excel
            ObservableList<Pack> packs = tabV.getItems();
            for (int i = 0; i < packs.size(); i++) {
                Pack pack = packs.get(i);
                XSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(pack.getIdP());
                row.createCell(1).setCellValue(pack.getNom());
                row.createCell(2).setCellValue(pack.getDescription());
                row.createCell(3).setCellValue(pack.getReduction());
                row.createCell(4).setCellValue(pack.getImage());
            }

            // Enregistrer le classeur Excel
            String filePath = "C:\\Users\\Guide Info\\Desktop\\GestionAB + apis/packs.xlsx";
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.close();

            showAlert("Succès", "Exportation vers Excel réussie", "Les données ont été exportées vers le fichier Excel avec succès.", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'exportation vers Excel", e.getMessage(), Alert.AlertType.ERROR);
        }
    }









}
