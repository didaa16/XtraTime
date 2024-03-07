package controllers.local;

import entities.local.complexe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.local.ServiceComplexe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AfficherController {
complexe complexe;
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
    }
    @FXML
    private void ExportExcel(ActionEvent event) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Liste des complexes");

            // En-tête
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("nom");
            headerRow.createCell(1).setCellValue("adresse");
            headerRow.createCell(2).setCellValue("tel");
            headerRow.createCell(3).setCellValue("patente");
            headerRow.createCell(4).setCellValue("image");

            // Données
            ObservableList<complexe> complexes = FXCollections.observableList(ServiceComplexe.afficher());
            for (int i = 0; i < complexes.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(complexes.get(i).getNom());
                row.createCell(1).setCellValue(complexes.get(i).getAdresse());
                row.createCell(2).setCellValue(complexes.get(i).getTel());
                row.createCell(3).setCellValue(complexes.get(i).getPatente());
                row.createCell(4).setCellValue(complexes.get(i).getImage());

            }

            // Sauvegarde du fichier
            String fileName = "Liste des Complexes.xlsx";
            try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
                fileOut.flush();
            }

            System.out.println("Export Excel réussi.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public List<complexe> triEmail() throws SQLException {

        List<complexe> list1 = new ArrayList<>();
        List<complexe> list2 = ServiceComplexe.afficher();

        list1 = list2.stream().sorted((o1, o2) -> o1.getNom().compareTo(o2.getNom())).collect(Collectors.toList());
        return list1;

    }
    @FXML
    private void Trie() throws SQLException {
        ServiceComplexe serviceComplexe = new ServiceComplexe();
        complexe complexe = new complexe();
        List<complexe> a = triEmail();
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_adr.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        col_tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        col_pt.setCellValueFactory(new PropertyValueFactory<>("patente"));
        col_im.setCellValueFactory(new PropertyValueFactory<>("image"));
        tv_complexe.getItems().setAll(a);

    }
    @FXML

    void modifier(ActionEvent event) {
        complexe complexe = tv_complexe.getSelectionModel().getSelectedItem();
        if (complexe != null) {
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/FxmlLocal/ModifierComplexe.fxml"));
                stage.setTitle("XTRATIME");
                stage.setScene(new Scene(root));
                stage.show();

                ModifierController controller = new ModifierController();
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
