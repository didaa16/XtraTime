package Controllers;

import entities.Pack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import services.ServicePack;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.List;

public class PackControllerClient {
    @FXML
    private ScrollPane ScrollPane;
    @FXML
    private FlowPane packFlowPane;

    @FXML
    private TextField SearchField;

    public void SetDataModules() throws IOException {
        ServicePack servicePack= new ServicePack();
        List<Pack> packs = servicePack.afficher();
        for (Pack p : packs) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack.fxml"));
            AnchorPane pane = loader.load();
            CardController cardController = loader.getController();
            cardController.initializeData(p);
            cardController.setPack(p);
            packFlowPane.getChildren().add(pane);
        }

    }
    @FXML
    void initialize()  {
        try {
            this.SetDataModules();
        } catch (IOException exception ) {
            throw new RuntimeException();
        }


       // ScrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack.fxml"));
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
        // Obtient le texte saisi dans le champ de recherche et le met en minuscules
        String searchtext= SearchField.getText().toLowerCase().trim();

        // Crée une instance du service Pack pour effectuer des opérations sur les packs
        ServicePack servicePack=new ServicePack();

        // Efface tous les enfants du conteneur packFlowPane
        packFlowPane.getChildren().clear();

        // Vérifie si le champ de recherche est vide
        if(searchtext.isEmpty()) {
            // Si le champ de recherche est vide, rafraîchit l'affichage des packs
            refreshFlow();
        } else {
            // Si le champ de recherche n'est pas vide, effectue une recherche par nom
            // pour trouver les packs correspondants
            List<Pack> packs = servicePack.afficherByName(searchtext);

            // Parcourt la liste des packs trouvés
            for (Pack p : packs) {
                // Charge la vue Pack.fxml à l'aide de FXMLLoader
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pack.fxml"));
                AnchorPane pane = null;
                try {
                    pane = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Obtient le contrôleur associé à la vue chargée
                CardController cardController = loader.getController();

                // Initialise les données du contrôleur avec les informations du pack
                cardController.initializeData(p);

                // Définit le pack associé au contrôleur
                cardController.setPack(p);

                // Ajoute le panneau (contenant les informations du pack) au conteneur packFlowPane
                packFlowPane.getChildren().add(pane);
            }
        }
    }
    }
