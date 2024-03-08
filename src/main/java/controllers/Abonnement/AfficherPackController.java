package controllers.Abonnement;

import entities.Abonnement.Pack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import services.Abonnement.ServicePack;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;


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
            Parent root = FXMLLoader.load(getClass().getResource("/FXMLUtilisateur/dashboard.fxml"));
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLAbonnement/ModifierP.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/FXMLAbonnement/AfficherA.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/FXMLAbonnement/HomeStats.fxml"));
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
            XSSFSheet sheet = workbook.createSheet("Liste des packs");

            // Créer une ligne pour les en-têtes
            XSSFRow headerRow = sheet.createRow(0);
            ObservableList<TableColumn<Pack, ?>> columns = tabV.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i).getText());
            }

            // Appliquer le style à la première ligne
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            XSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 10);
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setBorderTop(BorderStyle.THICK);
            headerStyle.setBorderBottom(BorderStyle.THICK);
            headerStyle.setBorderLeft(BorderStyle.THICK);
            headerStyle.setBorderRight(BorderStyle.THICK);
            headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            for (int i = 0; i < columns.size(); i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }

            // Remplir les données
            ObservableList<Pack> packs =tabV.getItems();
            for (int i = 0; i < packs.size(); i++) {
                XSSFRow row = sheet.createRow(i + 1);
                for (int j = 0; j < columns.size(); j++) {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellValue(columns.get(j).getCellData(packs.get(i)).toString());
                }
            }

            // Ajuster la largeur des colonnes
            for (int i = 0; i < columns.size(); i++) {

                    sheet.setColumnWidth(i, 20 * 256);                }


            // Enregistrer le classeur Excel dans un fichier
            FileOutputStream fileOut = new FileOutputStream("packs.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            // Ouvrir le fichier Excel avec l'application par défaut
            File file = new File("packs.xlsx");
            Desktop.getDesktop().open(file);

            // Afficher un message de confirmation
            showAlert("Exportation réussie", "Les données ont été exportées vers le fichier packs.xlsx", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'exportation vers Excel", e.getMessage(), Alert.AlertType.ERROR);
        }
    }




}
