package controllers;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.ServiceProduit;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.itextpdf.layout.properties.TextAlignment.CENTER;

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
                sheet.autoSizeColumn(i);
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
            title.setTextAlignment(CENTER);
            document.add(title);

            // Ajouter un bloc de texte pour centrer le tableau
            Paragraph centeredText = new Paragraph();
            centeredText.setTextAlignment(CENTER);

            // Ajouter un tableau pour les données
            Table table = new Table(listeProduit.getColumns().size());
            ObservableList<TableColumn<Produit, ?>> columns = listeProduit.getColumns();
            for (TableColumn<Produit, ?> column : columns) {
                table.addHeaderCell(column.getText());
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
                        table.addCell(new Paragraph(cellValue));
                    } else {
                        // Sinon, ajouter la valeur de la cellule
                        String cellValue = column.getCellData(produit).toString();
                        table.addCell(cellValue);
                    }
                }
            }
            // Ajuster la taille des autres colonnes

            c_nom.setPrefWidth(100); // Ajuster la largeur de la colonne "Nom" à 100 pixels
            c_reference.setPrefWidth(100); // Ajuster la largeur de la colonne "Référence" à 100 pixels
            c_description.setPrefWidth(100); // Ajuster la largeur de la colonne "Description" à 200 pixels
            c_prix.setPrefWidth(100); // Ajuster la largeur de la colonne "Prix" à 100 pixels
            c_taille.setPrefWidth(100); // Ajuster la largeur de la colonne "Taille" à 100 pixels
            c_typesport.setPrefWidth(100); // Ajuster la largeur de la colonne "Type de sport" à 100 pixels
            c_marque.setPrefWidth(100); // Ajuster la largeur de la colonne "Marque" à 100 pixels
            c_quantite.setPrefWidth(100); // Ajuster la largeur de la colonne "Quantité" à 100 pixels


            // Ajouter le tableau au document
            document.add(table);
            document.add(centeredText);

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
