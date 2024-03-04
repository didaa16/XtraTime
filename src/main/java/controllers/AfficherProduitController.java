package controllers;
import com.itextpdf.kernel.colors.Color;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import entities.Marque;
import entities.Produit;
import entities.TypeSport;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import services.ServiceProduit;
import com.itextpdf.layout.element.Cell;
import java.awt.*;
//import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.itextpdf.kernel.colors.Color;

import static com.itextpdf.layout.properties.TextAlignment.CENTER;
import static java.awt.Color.red;

public class AfficherProduitController {

    ServiceProduit serviceProduit =new ServiceProduit();

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnpdf;
    @FXML
    private Button btnExcel;
    @FXML
    private HBox back;



    @FXML
    private TableColumn<Produit, String> c_description;

    @FXML
    private TableColumn<Produit, String> c_image;

    @FXML
    private TableColumn<Produit, Marque> c_marque;

    @FXML
    private TableColumn<Produit, String> c_nom;

    @FXML
    private TableColumn<Produit, Double> c_prix;

    @FXML
    private TableColumn<Produit, Integer> c_quantite;

    @FXML
    private TableColumn<Produit, String> c_reference;

    @FXML
    private TableColumn<Produit, String> c_taille;

    @FXML
    private TableColumn<Produit, TypeSport> c_typesport;

    @FXML
    private TableView<Produit> listeProduit;

    private static List<Produit> deletedProducts = new ArrayList<>();

    @FXML
    void initialize(){
        try {
            ObservableList<Produit> produits = FXCollections.observableList(serviceProduit.afficher());
            listeProduit.setItems(produits);
            c_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            c_reference.setCellValueFactory(new PropertyValueFactory<>("ref"));
            c_description.setCellValueFactory(new PropertyValueFactory<>("description"));
            c_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            c_taille.setCellValueFactory(new PropertyValueFactory<>("taille"));
            c_typesport.setCellValueFactory(new PropertyValueFactory<>("typeSport"));
            c_marque.setCellValueFactory(new PropertyValueFactory<>("marque"));
            c_quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            c_image.setCellValueFactory(new PropertyValueFactory<>("image"));
            listeProduit.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listeProduit.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Produit> change) -> {
                if (change.getList().size() > 0 && change.getList().get(0) != null && change.getList().get(0).equals(KeyCode.CONTROL)) {
                    recupererProducts();
                }
            });


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void recupererProducts() {
        List<Produit> products = listeProduit.getSelectionModel().getSelectedItems();
        deletedProducts.clear();
        deletedProducts.addAll(products);
    }
    @FXML
    void supprimerproduit(ActionEvent event) {
        Produit produit = listeProduit.getSelectionModel().getSelectedItem();
        if (produit != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Supprimer le produit ?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer le produit " + produit.getNom() + " ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    serviceProduit.supprimer(produit.getRef());
                    listeProduit.getItems().remove(produit);

                    // Afficher un message de confirmation
                    Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                    confirmationAlert.setTitle("Suppression réussie");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Le produit " + produit.getNom() + " a été supprimé avec succès.");
                    confirmationAlert.showAndWait();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            showAlert("Aucune sélection", "Aucun produit sélectionné", "Veuillez sélectionner un produit à supprimer.", Alert.AlertType.WARNING);
        }
    }



    @FXML
    void modifierProduit(ActionEvent event) {
        Produit produit = listeProduit.getSelectionModel().getSelectedItem();
        if (produit != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProduit.fxml"));
                Parent root = loader.load();
                ModifierProduitController controller = loader.getController();

                controller.initData(produit);
                controller.setServiceProduit(serviceProduit);
                controller.setAfficherProduitController(this);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de l'ouverture de l'interface de modification", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune sélection", "Aucun produit sélectionné", "Veuillez sélectionner un produit à modifier.", Alert.AlertType.WARNING);
        }
    }



    public void refreshTable() {
        try {
            ObservableList<Produit> produits = FXCollections.observableList(serviceProduit.afficher());
            listeProduit.setItems(produits);
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
    void ajouterProduit(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterProduit.fxml"));

            Scene scene = btnAjouter.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de la page AjouterProduit", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void exporteeExcel(ActionEvent event) {
        try {
            // Créer un nouveau classeur Excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Liste des produits");

            // Créer une ligne pour les en-têtes
            XSSFRow headerRow = sheet.createRow(0);
            ObservableList<TableColumn<Produit, ?>> columns = listeProduit.getColumns();
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
            ObservableList<Produit> produits = listeProduit.getItems();
            for (int i = 0; i < produits.size(); i++) {
                XSSFRow row = sheet.createRow(i + 1);
                for (int j = 0; j < columns.size(); j++) {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellValue(columns.get(j).getCellData(produits.get(i)).toString());
                }
            }

            // Ajuster la largeur des colonnes
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i).equals(c_image) || columns.get(i).equals(c_description)) {
                    sheet.setColumnWidth(i, 40 * 256); // Définir la largeur de la colonne en unités de 1/256 de caractère
                } else {
                    sheet.setColumnWidth(i, 20 * 256);                }
            }

            // Enregistrer le classeur Excel dans un fichier
            FileOutputStream fileOut = new FileOutputStream("produits.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            // Ouvrir le fichier Excel avec l'application par défaut
            File file = new File("produits.xlsx");
            Desktop.getDesktop().open(file);

            // Afficher un message de confirmation
            showAlert("Exportation réussie", "Les données ont été exportées vers le fichier produits.xlsx", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'exportation vers Excel", e.getMessage(), Alert.AlertType.ERROR);
        }
    }




    @FXML
    void exporterPDF(ActionEvent event) {
        try {
            // Créer un nouveau document PDF en mode paysage
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter("produits.pdf"));
            Document document = new Document(pdfDocument, PageSize.A3.rotate());

            // Ajouter un titre
            Paragraph title = new Paragraph("Liste des produits");
            title.setFontSize(24);
            title.setBold();
            title.setTextAlignment(TextAlignment.CENTER);
            title.setFontColor(ColorConstants.RED); // Set the title color to red
            document.add(title);

            // Ajouter un tableau pour les données
            Table table = new Table(listeProduit.getColumns().size());
            ObservableList<TableColumn<Produit, ?>> columns = listeProduit.getColumns();

            // Ajouter une ligne de cellules de titre en gras et en couleur
            for (TableColumn<Produit, ?> column : columns) {
                Cell cell = new Cell();
                cell.add(new Paragraph(column.getText()).setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY).setFontSize(20));
                table.addHeaderCell(cell);
            }

            // Remplir les données
            ObservableList<Produit> produits = listeProduit.getItems();
            for (Produit produit : produits) {
                for (TableColumn<Produit, ?> column : columns) {
                    if (column.equals(c_image)) {
                        // Si la colonne est celle de l'image, charger l'image et l'ajouter à la cellule
                        Image image = new Image(produit.getImage());
                        com.itextpdf.layout.element.Image pdfImage = new com.itextpdf.layout.element.Image(ImageDataFactory.create(image.getUrl()));
                        pdfImage.scaleToFit(100, 100);
                        table.addCell(pdfImage);
                    } else if (column.equals(c_description)) {
                        // Si la colonne est celle de la description, ajouter une cellule avec un retour à la ligne
                        String cellValue = column.getCellData(produit).toString();
                        table.addCell(new Paragraph(cellValue).setMinWidth(50));
                    } else {
                        Object cellValue = column.getCellData(produit);
                        Cell cell = new Cell();
                        cell.add(new Paragraph(cellValue.toString()));
                        cell.setWidth(100); // Ajuster la largeur de la cellule à 100 pixels
                        table.addCell(cell);
                    }
                }
            }

            // Ajuster la taille des colonnes
            for (TableColumn<Produit, ?> column : columns) {
                column.setPrefWidth(100); // Ajuster la largeur de chaque colonne à 100 pixels
            }

            // Ajouter le tableau au document
            document.add(table);

            // Fermer le document
            document.close();

            // Ouvrir le fichier PDF avec l'application par défaut
            File file = new File("produits.pdf");
            Desktop.getDesktop().open(file);

            // Afficher un message de confirmation
            showAlert("Exportation réussie", "Les données ont été exportées vers le fichier produits.pdf", "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'exportation vers PDF", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void goBack() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GestionCommande.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à l'interface précédente", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


}
