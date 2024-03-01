package controllers;

import entities.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import services.ServiceProduit;
import tests.IListener;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.itextpdf.kernel.pdf.PdfName.St;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.title;

public class StoreController implements Initializable {
    @FXML
    private ImageView back;
    @FXML
    private Label RefProd;
    @FXML
    private ImageView imageProd;

    @FXML
    private Label nomProd;

    @FXML
    private Label prixProd;
    @FXML
    private Label DescriprionProd;

    @FXML
    private Label TailleProd;

    @FXML
    private VBox chosenProdCard;

    @FXML
    private GridPane grid;
    @FXML
    private Label typee;

    @FXML
    private Label MarqueProd;

    @FXML
    private ScrollPane scroll;
    private IListener iListener;
    @FXML
    private TextField recherche;
    @FXML
    private HBox consulter;


    private List<Produit> produits = new ArrayList<>();

    private List<Produit> getData() {
        List<Produit> produits = new ArrayList<>();
        try {
            ServiceProduit serviceProduit = new ServiceProduit();
            List<Produit> allProducts = serviceProduit.afficher();
            for (Produit produit : allProducts) {
                Produit produitInfo = new Produit();
                produitInfo.setNom(produit.getNom());
                produitInfo.setPrix(produit.getPrix());
                produitInfo.setImage(produit.getImage());
                produitInfo.setRef(produit.getRef());
                produitInfo.setDescription(produit.getDescription());
                produitInfo.setTaille(produit.getTaille());
                produitInfo.setMarque(produit.getMarque());
                produitInfo.setTypeSport(produit.getTypeSport());
                produits.add(produitInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.produits.addAll(this.getData());
        if (this.produits.size() > 0) {
            this.setProd((Produit)this.produits.get(0));
            this.iListener= new IListener() {

                public void onClickListener(Produit produit) {
                    setProd(produit);
                }
            };
        }
        int column = 0;
        int row = 1;

        try {
            for(int i = 0; i < this.produits.size(); ++i) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/item.fxml")); // Chemin vers le fichier FXML de l'élément
                AnchorPane anchorPane = (AnchorPane)fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                itemController.setData(this.produits.get(i),iListener);
                if (column == 3) {
                    column = 0;
                    ++row;
                }
                grid.add(anchorPane, column++, row);
               grid.setMinWidth(-1.0);
               grid.setPrefWidth(-1.0);
              grid.setMaxWidth(Double.NEGATIVE_INFINITY);
               grid.setMinHeight(-1.0);
                grid.setPrefHeight(-1.0);
                grid.setMaxHeight(Double.NEGATIVE_INFINITY);
                grid.setHgap(5);
                grid.setVgap(5);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException var9) {
            var9.printStackTrace();
        }
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

    }

    private String currency = " DT";
    private void setProd(Produit produit){
        nomProd.setText(produit.getNom());
        prixProd.setText("Prix: " +String.valueOf(produit.getPrix())+currency );
        TailleProd.setText("Taille: "+produit.getTaille());
        RefProd.setText("Référence: "+ produit.getRef());
        DescriprionProd.setText("Description: "+ produit.getDescription());
        typee.setText(String.valueOf("Type Sport: "+produit.getTypeSport()));
        MarqueProd.setText(String.valueOf("Marque: "+produit.getMarque()));
        String imageUrl = produit.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            imageProd.setImage(image);
        }
    }
    @FXML
    void goBack(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterProduit.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @FXML
    void consulter(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjoutCom.fxml"));
            Scene scene = consulter.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

//recherche
@FXML
private void handleSearch(KeyEvent event) throws IOException {
    String searchText = recherche.getText().toLowerCase();
    grid.getChildren().clear(); // Effacer les résultats précédents

    int column = 0;
    int row = 1;

    for (Produit produit : produits) {
        if (produit.getNom().toLowerCase().contains(searchText)) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/item.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            ItemController itemController = fxmlLoader.getController();
            itemController.setData(produit, iListener);

            grid.add(anchorPane, column++, row);
            if (column == 3) {
                column = 0;
                row++;
            }
            grid.setMinWidth(-1.0);
            grid.setPrefWidth(-1.0);
            grid.setMaxWidth(Double.NEGATIVE_INFINITY);
            grid.setMinHeight(-1.0);
            grid.setPrefHeight(-1.0);
            grid.setMaxHeight(Double.NEGATIVE_INFINITY);
            grid.setHgap(5);
            grid.setVgap(5);

            GridPane.setMargin(anchorPane, new Insets(10));
        }
    }
}

}























