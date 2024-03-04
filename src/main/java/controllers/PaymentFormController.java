package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import entities.Commande;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import services.ServiceCommande;
import services.StripePaymentService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PaymentFormController implements Initializable {

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
        // Récupérer les informations de paiement du formulaire
        String cardNumber = cardNumberField.getText();
        String expMonthText = expMonthField.getText();
        String expYearText = expYearField.getText();
        String cvc = cvcField.getText();

        // Vérifier que les champs ne sont pas vides
        if (cardNumber.isEmpty() || expMonthText.isEmpty() || expYearText.isEmpty() || cvc.isEmpty()) {
            showAlert("Erreur", "", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR);

            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier que les champs expMonthField et expYearField contiennent des caractères numériques
        int expMonth;
        int expYear;
        try {
            expMonth = Integer.parseInt(expMonthText);
            expYear = Integer.parseInt(expYearText);
        } catch (NumberFormatException e) {
            System.out.println("Les champs mois et année doivent être des nombres.");
            showAlert("Erreur", "", "Les champs mois et année doivent être des nombres.", Alert.AlertType.ERROR);

            return;
        }

        // Vérifier la validité de la carte de crédit
        if (!CreditCardValidator.isValid(cardNumber)) {
            showAlert("Erreur", "", "Numéro de carte de crédit invalide.", Alert.AlertType.ERROR);

            System.out.println("Numéro de carte de crédit invalide.");
            return;
        }

        // Vérifier la date d'expiration de la carte de crédit
        if (expYear < 2022 || (expYear == 2022 && expMonth < 3)) {
            showAlert("Erreur", "", "Date d'expiration de la carte de crédit invalide.", Alert.AlertType.ERROR);

            System.out.println("Date d'expiration de la carte de crédit invalide.");
            return;
        }

        // Vérifier le code CVC
        if (cvc.length() != 3) {
            showAlert("Erreur", "", "Code CVC invalide.", Alert.AlertType.ERROR);

            System.out.println("Code CVC invalide.");
            return;
        }

        Commande commande;

        try {
            commande = serviceCommande.getCommande("dida16");
        } catch (SQLException e){
            // Afficher un message d'erreur
            System.out.println("erreur");
            return;
        }

        // Vérifier le montant de la commande
        if (commande.getPrix() <= 0) {
            showAlert("Erreur", "", "Montant de la commande invalide.", Alert.AlertType.ERROR);

            System.out.println("Montant de la commande invalide.");
            return;
        }

        // Créer un paiement avec Stripe
        StripePaymentService stripePaymentService = new StripePaymentService();
        PaymentIntent paymentIntent = stripePaymentService.createPayment(commande.getPrix(), "DT", "Example payment");
        System.out.println("paiement réussi");
    }




    private void showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            serviceCommande = new ServiceCommande();

            // Récupérer la commande à partir de la base de données
            Commande commande = serviceCommande.getCommande("dida16");

            // Afficher le montant total de la commande dans l'interface utilisateur
            total.setText(String.valueOf(commande.getPrix())+ "DT");
        } catch (SQLException e) {
            // Afficher un message d'erreur
            System.out.println("Erreur lors de la récupération de la commande : " + e.getMessage());
            showAlert("Erreur", "", "Erreur lors de la récupération de la commande .", Alert.AlertType.ERROR);

        }
    }
    @FXML
    void goBack(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjoutCom.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
