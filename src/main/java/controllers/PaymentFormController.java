package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import entities.Commande;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.ServiceCommande;
import services.StripePaymentService;

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

        // Créer un objet de paiement avec les informations de paiement
        Map<String, Object> params = new HashMap<>();
        params.put("amount", commande.getPrix()); // Montant en centimes (120 $)
        params.put("currency", " DT");
        params.put("source", cardNumber);
        params.put("description", "Example payment");

        try {
            // Créer un paiement avec Stripe
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            System.out.println("paiement reussi");

        } catch (StripeException e) {
            // Afficher un message d'erreur
            System.out.println("Erreur lors du paiement : " + e.getMessage());
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            serviceCommande = new ServiceCommande();

            // Récupérer la commande à partir de la base de données
            Commande commande = serviceCommande.getCommande("dida16");

            // Afficher le montant total de la commande dans l'interface utilisateur
            total.setText(String.valueOf(commande.getPrix()));
        } catch (SQLException e) {
            // Afficher un message d'erreur
            System.out.println("Erreur lors de la récupération de la commande : " + e.getMessage());
        }
    }
}
