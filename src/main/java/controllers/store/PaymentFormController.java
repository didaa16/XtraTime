package controllers.store;

import com.stripe.exception.StripeException;
import entities.store.Commande;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import services.store.ServiceCommande;
import services.store.StripePaymentService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
            commande = serviceCommande.getCommande("dida16");
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
            showAlert("Paiement", "", "Paiement effectué avec succès.", Alert.AlertType.INFORMATION);

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            serviceCommande = new ServiceCommande();
            Commande commande = serviceCommande.getCommande("dida16");
            total.setText(String.valueOf(commande.getPrix()) + "DT");
        } catch (SQLException e) {
            showAlert("Erreur", "", "Erreur lors de la récupération de la commande.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void goBack(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlStore/imagesss/AjoutCom.fxml"));
            Scene scene = back.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            showAlert("Erreur", "", "Erreur lors de la navigation en arrière.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
