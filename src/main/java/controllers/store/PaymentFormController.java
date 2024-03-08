package controllers.store;

import com.stripe.exception.StripeException;
import entities.store.Commande;
import entities.utilisateur.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import services.store.ServiceCommande;
import services.store.StripePaymentService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PaymentFormController implements Initializable {
    private static Utilisateur loggedInUser;
    public static void setLoggedInUser(Utilisateur user) {
        loggedInUser = user;
    }


    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expMonthField;

    @FXML
    private TextField expYearField;

    @FXML
    private TextField cvcField;

    @FXML
    private Button payButton;

    @FXML
    private Label total;

    @FXML
    private ImageView back;

    private ServiceCommande serviceCommande;

    @FXML
    private void handlePayment() {
        String cardNumber = cardNumberField.getText();
        String expMonthText = expMonthField.getText();
        String expYearText = expYearField.getText();
        String cvc = cvcField.getText();

        if (cardNumber.isEmpty() || expMonthText.isEmpty() || expYearText.isEmpty() || cvc.isEmpty()) {
            showAlert("Erreur", "", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);
            return;
        }
//        // Ajouter une condition de validation du numéro de carte de crédit
//        if (!CreditCardValidator.isValid(cardNumber)) {
//            showAlert("Erreur", "", "Numéro de carte de crédit invalide.", Alert.AlertType.ERROR);
//            return;
//        }

        int expMonth;
        int expYear;
        try {
            expMonth = Integer.parseInt(expMonthText);
            expYear = Integer.parseInt(expYearText);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "", "Les champs mois et année doivent être des nombres.", Alert.AlertType.ERROR);
            return;
        }

        if (expYear < 2024 || (expYear == 2024 && expMonth < 5 )) {
            showAlert("Erreur", "", "Date d'expiration de la carte de crédit invalide.", Alert.AlertType.ERROR);
            return;
        }

        if (cvc.length() != 3) {
            showAlert("Erreur", "", "Code CVC invalide.", Alert.AlertType.ERROR);
            return;
        }

        Commande commande;
        try {
            commande = serviceCommande.getCommande(loggedInUser.getPseudo());
        } catch (SQLException e){
            showAlert("Erreur", "", "Erreur lors de la récupération de la commande.", Alert.AlertType.ERROR);
            return;
        }

        double totalAmount = commande.getPrix();
        if (totalAmount <= 0) {
            showAlert("Erreur", "", "Montant de la commande invalide.", Alert.AlertType.ERROR);
            return;
        }

        try {
            StripePaymentService.createPayment(totalAmount * 100); // Convert total to cents
            showAlertsuccess("Paiement", "", "Votre paiement a été effectué avec succès \n Merci d'avoir fait vos achats chez nous !", Alert.AlertType.INFORMATION);

        } catch (StripeException e) {
            showAlert("Erreur", "", "Erreur lors du paiement.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    private void showAlertsuccess(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Charger une image à partir d'un chemin local
        try {
            URL imageUrl = getClass().getResource("/fxmlStore/imagesss/cong.png");
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(64); // Réduire la taille de l'icône
                imageView.setFitHeight(64);
                alert.setGraphic(imageView);
            }
        } catch (Exception e) {
            // Gérer les erreurs de chargement d'image
            e.printStackTrace();
        }

        // Accéder à la zone de dialogue (DialogPane) de l'alerte pour personnaliser le contenu
        GridPane grid = (GridPane) alert.getDialogPane().getContent();

        // Vérifier si la grille est initialisée
        if (grid != null) {
            Label label = (Label) grid.getChildren().get(1); // Récupérer le label de contenu

            // Centrer le texte
            label.setAlignment(Pos.CENTER);
            label.setFont(Font.font("Arial", 20));
            label.setStyle("-fx-font-weight: bold;");


        } else {
            System.err.println("Grid is null!");
        }

        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            serviceCommande = new ServiceCommande();
            Commande commande = serviceCommande.getCommande(loggedInUser.getPseudo());
            total.setText(String.valueOf(commande.getPrix()) + "DT");
        } catch (SQLException e) {
            showAlert("Erreur", "", "Erreur lors de la récupération de la commande.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void goBack(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/AjoutCom.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "", "Erreur lors de la navigation en arrière.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}