package controllers.Abonnement;

import entities.Abonnement.Pack;
import entities.utilisateur.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import services.Abonnement.ServicePack;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class PackControllerClient {
    @FXML
    private ScrollPane ScrollPane;
    @FXML
    private FlowPane packFlowPane;

    @FXML
    private TextField SearchField;

    @FXML
    private WebView webview;
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }

    private void setDataModules() throws IOException {
        ServicePack servicePack = new ServicePack();
        List<Pack> packs = servicePack.afficher();
        for (Pack p : packs) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLAbonnement/Pack.fxml"));
            AnchorPane pane = loader.load();
            CardController cardController = loader.getController();
            cardController.initializeData(p);
            cardController.setPack(p);
            String imagePath = p.getImage();
            cardController.setImage(imagePath);
            packFlowPane.getChildren().add(pane);
        }
    }








    @FXML
    void initialize() {
        try {
            this.setDataModules();
        } catch (IOException exception) {
            throw new RuntimeException("Error loading data modules", exception);
        }

        loadVideo(); // Load the video initially

        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Trigger search every time the text in the search field changes
            HandleSearch(null);
        });
    }


    private void showAlert (String title, String header, String content){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        }
        void refreshFlow()
        {
            packFlowPane.getChildren().clear();
            ServicePack servicePack= new ServicePack();
            List<Pack> packs = servicePack.afficher();
            for (Pack p : packs) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLAbonnement/Pack.fxml"));
                AnchorPane pane = null;
                try {
                    pane = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                CardController cardController = loader.getController();
                cardController.initializeData(p);
                cardController.setPack(p);
                packFlowPane.getChildren().add(pane);
            }


        }
    @FXML
    void HandleSearch(KeyEvent event) {
        String searchText = SearchField.getText().trim();
        ServicePack servicePack = new ServicePack();

        packFlowPane.getChildren().clear();

        // Vérifier si le texte de recherche n'est pas vide
        if (!searchText.isEmpty()) {
            List<Pack> packs;

            // Vérifier si le texte de recherche est un nombre (réduction)
            if (isNumeric(searchText)) {
                // Si le texte saisi est un nombre, recherche par réduction
                int reduction = Integer.parseInt(searchText);
                packs = servicePack.afficherByPartialReduction(searchText);
            } else {
                // Sinon, recherche par nom de pack
                packs = servicePack.afficherByNameStartingWith(searchText);
            }

            // Afficher les packs trouvés dans l'interface utilisateur
            for (Pack p : packs) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLAbonnement/Pack.fxml"));
                AnchorPane pane;
                try {
                    pane = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                CardController cardController = loader.getController();
                cardController.initializeData(p);
                cardController.setPack(p);
                packFlowPane.getChildren().add(pane);
            }
        }
    }


    // Vérifie si une chaîne est numérique
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    void switchW(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXMLAbonnement/WebView.fxml"));
            Scene scene= new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

@FXML
    private void handleLoadWebsite(ActionEvent event) {
        String url = "https://www.nike.com/";
        openWebpage(url);
    }

    private void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void loadVideo() {
        String gifUrl = getClass().getResource("/FXMLAbonnement/nike.gif").toExternalForm();
        String htmlContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "html, body {"
                + "    margin: 0;"
                + "    padding: 0;"
                + "    height: 100%;"
                + "}"
                + "body {"
                + "    display: flex;"
                + "    justify-content: center;"
                + "    align-items: center;"
                + "}"
                + "img {"
                + "    width: 100%; /* L'image s'étendra sur toute la largeur de WebView */"
                + "    height: auto; /* La hauteur s'ajustera automatiquement pour maintenir le rapport d'aspect */"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<img src='" + gifUrl + "' alt='GIF'>"
                + "</body>"
                + "</html>";

        webview.getEngine().loadContent(htmlContent);

// Désactiver la barre de défilement
        webview.setContextMenuEnabled(false);
        webview.setZoom(1);
    }














}







