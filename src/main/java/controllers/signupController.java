package controllers;
import entities.Utilisateur;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.text.Font;
import utils.Encryptor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import services.ServiceUtilisateurs;
import utils.SendMail;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class signupController implements Initializable {
    ServiceUtilisateurs serviceUtilisateurs = new ServiceUtilisateurs();
    Encryptor encryptor = new Encryptor();

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getComplexite();
        setupRandomCaptcha();
    }
    @FXML
    private TextField pseudoSignup, cinSignup, nomSignup, prenomSignup, ageSignup, numtelSignup, emailSignup, codeVerification, captchaField;
    @FXML
    private PasswordField mdpSignup, confirmMdpSignup;
    @FXML
    private RadioButton roleClientSignup, roleLocateurSignup;
    @FXML
    private Button signUpButton, loginSignup, validerCode, retourButton, conditionsButton, reloadCaptcha;
    @FXML
    private Label pseudoSignupError, cinSignupError, nomSignupError, prenomSignupError, ageSignupError, numtelSignupError, emailSignupError, mdpSignupError, confirmMdpSignupError, roleSignupError, complexiteLabel, conditionLabel, captcha, captchaError;
    @FXML
    private AnchorPane codeVerifAnchor, signUpAnchor ;
    @FXML
    private ProgressBar complexiteBar;
    @FXML
    private CheckBox conditionsBox;
    @FXML
    private TextArea conditionsText;
    @FXML
    private ScrollPane scrollConditions;



    private static int rand;
    private static void setRand(int r) {
        rand = r;
    }
    private static String randomString;

    public void loginSignupButtonOnClick(ActionEvent event){
        serviceUtilisateurs.changeScreen(event, "/login.fxml", "Login");
    }

    public boolean getErrors(){
        pseudoSignupError.setText("");
        cinSignupError.setText("");
        nomSignupError.setText("");
        prenomSignupError.setText("");
        ageSignupError.setText("");
        numtelSignupError.setText("");
        emailSignupError.setText("");
        mdpSignupError.setText("");
        confirmMdpSignupError.setText("");
        roleSignupError.setText("");
        if(pseudoSignup.getText().isBlank()){
            pseudoSignupError.setTextFill(Color.RED);
            pseudoSignupError.setText("Le Pseudo est invalide");
            return true;
        }
        if(cinSignup.getText().isBlank() || !cinSignup.getText().matches("\\d{1,9}")){
            cinSignupError.setTextFill(Color.RED);
            cinSignupError.setText("Le CIN est invalide");
            return true;
        }
        if(nomSignup.getText().isBlank() || !nomSignup.getText().matches("[a-zA-Z ]+")){
            nomSignupError.setTextFill(Color.RED);
            nomSignupError.setText("Le nom est invalide");
            return true;
        }
        if(prenomSignup.getText().isBlank() || !prenomSignup.getText().matches("[a-zA-Z ]+")){
            prenomSignupError.setTextFill(Color.RED);
            prenomSignupError.setText("Le prénom est invalide");
            return true;
        }
        if(ageSignup.getText().isBlank() || !ageSignup.getText().matches("\\d+") || Integer.parseInt(ageSignup.getText()) < 18){
            ageSignupError.setTextFill(Color.RED);
            ageSignupError.setText("L'âge doit être un nombre valide et être supérieur à 18");
            return true;
        }
        if(numtelSignup.getText().isBlank() || !numtelSignup.getText().matches("\\d{1,12}")){
            numtelSignupError.setTextFill(Color.RED);
            numtelSignupError.setText("Le numéro de téléphone est invalide");
            return true;
        }
        if(emailSignup.getText().isBlank() || !emailSignup.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            emailSignupError.setTextFill(Color.RED);
            emailSignupError.setText("L'email est invalide");
            return true;
        }
        if(mdpSignup.getText().isBlank()|| complexiteLabel.getText().equals("Faible") || complexiteLabel.getText().equals("Très Faible")){
            mdpSignupError.setTextFill(Color.RED);
            mdpSignupError.setText("Le mot de passe est invalide");
            return true;
        }
        if(confirmMdpSignup.getText().isBlank()){
            confirmMdpSignupError.setTextFill(Color.RED);
            confirmMdpSignupError.setText("La confirmation du mot de passe est invalide");
            return true;
        }
        if(!Objects.equals(confirmMdpSignup.getText(), mdpSignup.getText())){
            confirmMdpSignupError.setTextFill(Color.RED);
            confirmMdpSignupError.setText("Le mot de passe doit etre le meme");
            return true;
        }
        if(!roleClientSignup.isSelected() && !roleLocateurSignup.isSelected()){
            roleSignupError.setTextFill(Color.RED);
            roleSignupError.setText("Le role est obligatoire");
            return true;
        }
        if(!conditionsBox.isSelected()){
            conditionLabel.setTextFill(Color.RED);
            conditionLabel.setText("Vous devez accepter les conditions d'utilisation");
            return true;
        }
        if (!captchaField.getText().equals(randomString)){
            captchaError.setTextFill(Color.RED);
            captchaError.setText("Vous devez insérer le code CAPTCHA");
            return true;
        }
        try {
            if (serviceUtilisateurs.pseudoExiste(pseudoSignup.getText())){
                pseudoSignupError.setTextFill(Color.RED);
                pseudoSignupError.setText("Ce pseudo est déjà utilisé, veuillez en choisir un autre");
                return true;
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void signUpButtonButtonOnClick(ActionEvent event) throws NoSuchAlgorithmException {
        if (!getErrors()) {
            Random rd = new Random();
            int Rand = rd.nextInt(1000000+1);
            signupController.setRand(Rand);
            signUpAnchor.setVisible(false);
            codeVerifAnchor.setVisible(true);
            Utilisateur newUser = new Utilisateur(pseudoSignup.getText(), Integer.parseInt(cinSignup.getText()), nomSignup.getText(),
                    prenomSignup.getText(), Integer.parseInt(ageSignup.getText()), Integer.parseInt(numtelSignup.getText()), emailSignup.getText(),
                    encryptor.encryptString(mdpSignup.getText()), (roleClientSignup.isSelected() ? "Client" : "Locateur"));
            SendMail.SendMail(event, Rand, newUser);
        }
    }
    @FXML
    void validateCodeOnClick(ActionEvent event) throws NoSuchAlgorithmException {
        System.out.println(String.valueOf(rand));
        if (Integer.parseInt(codeVerification.getText()) == this.rand){
            Utilisateur newUser = new Utilisateur(pseudoSignup.getText(), Integer.parseInt(cinSignup.getText()), nomSignup.getText(),
                    prenomSignup.getText(), Integer.parseInt(ageSignup.getText()), Integer.parseInt(numtelSignup.getText()), emailSignup.getText(),
                    encryptor.encryptString(mdpSignup.getText()), (roleClientSignup.isSelected() ? "Client" : "Locateur"));
            try {
                serviceUtilisateurs.ajouter(newUser);
                System.out.println("Utilisateur ajouté avec succès !");
                JOptionPane.showMessageDialog(null,"Vous etes inscris avec succès ! Veuillez connecter maintenant.");
                serviceUtilisateurs.changeScreen(event, "/login.fxml", "LOGIN");
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            }
        }
        else {
            JOptionPane.showMessageDialog(null,"Code incorrecte! ");
        }
    }

    @FXML
    private void returnButtonOnClick(ActionEvent event){
        codeVerifAnchor.setVisible(false);
        signUpAnchor.setVisible(true);
    }

    @FXML
    private void getComplexite() {
        mdpSignup.textProperty().addListener((observable, oldValue, newValue) -> {
            complexiteBar.setVisible(true);

            // Reset complexity label and bar color
            complexiteLabel.setText("");
            complexiteBar.setProgress(0);
            complexiteBar.setBlendMode(null);

            // Check if the password contains special characters
            boolean hasSpecialChars = newValue.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
            // Check if the password contains digits
            boolean hasDigits = newValue.matches(".*\\d.*");
            // Check if the password contains lowercase letters
            boolean hasLowercase = newValue.matches(".*[a-z].*");
            // Check if the password contains uppercase letters
            boolean hasUppercase = newValue.matches(".*[A-Z].*");

            // Calculate complexity score
            int complexityScore = 0;
            if (hasSpecialChars) complexityScore += 2;
            if (hasDigits) complexityScore += 2;
            if (hasLowercase) complexityScore += 2;
            if (hasUppercase) complexityScore += 2;

            // Check if the password length is at least 8 characters
            if (newValue.length() < 8) {
                complexiteLabel.setText("Faible");
                complexiteBar.setProgress(0.0);
                complexiteBar.setBlendMode(BlendMode.RED);
            } else {
                // Set complexity level and progress bar based on score
                if (complexityScore >= 8) {
                    complexiteLabel.setText("Très Fort");
                    complexiteBar.setProgress(1.0);
                    complexiteBar.setBlendMode(BlendMode.COLOR_DODGE);
                } else if (complexityScore >= 6) {
                    complexiteLabel.setText("Fort");
                    complexiteBar.setProgress(0.75);
                    complexiteBar.setBlendMode(BlendMode.COLOR_DODGE);
                } else if (complexityScore >= 4) {
                    complexiteLabel.setText("Moyenne");
                    complexiteBar.setProgress(0.5);
                    complexiteBar.setBlendMode(BlendMode.HARD_LIGHT);
                } else if (complexityScore >= 2) {
                    complexiteLabel.setText("Faible");
                    complexiteBar.setProgress(0.25);
                    complexiteBar.setBlendMode(BlendMode.RED);
                } else {
                    complexiteLabel.setText("Très Faible");
                    complexiteBar.setProgress(0.0);
                    complexiteBar.setBlendMode(BlendMode.RED);
                }
            }
        });
    }

    @FXML
    private void conditionsButtonOnClick(ActionEvent event){
        scrollConditions.setVisible(true);
        signUpAnchor.setVisible(false);
        codeVerifAnchor.setVisible(false);
        conditionsText.setText("Conditions d'utilisation de l'application XtraTime\n" +
                "\n" +
                "Bienvenue sur XtraTime ! Avant d'utiliser notre application, veuillez lire attentivement les conditions d'utilisation suivantes. En accédant ou en utilisant l'application XtraTime, vous acceptez d'être lié par ces conditions. Si vous n'acceptez pas ces conditions, veuillez ne pas utiliser notre application.\n" +
                "\n" +
                "Utilisation de l'application :\n" +
                "a. Vous devez avoir au moins 18 ans pour utiliser l'application XtraTime ou disposer du consentement parental.\n" +
                "b. Vous êtes seul responsable de toutes les activités effectuées via votre compte XtraTime.\n" +
                "c. Vous vous engagez à utiliser l'application XtraTime uniquement à des fins légales et conformément à toutes les lois et réglementations applicables.\n" +
                "\n" +
                "Compte utilisateur :\n" +
                "a. Pour accéder à certaines fonctionnalités de l'application, vous devez créer un compte utilisateur.\n" +
                "b. Vous devez fournir des informations exactes, complètes et à jour lors de la création de votre compte.\n" +
                "c. Vous êtes responsable du maintien de la confidentialité de votre compte et de votre mot de passe, et vous acceptez de ne pas partager vos informations de connexion avec des tiers.\n" +
                "\n" +
                "Location d'espaces sportifs :\n" +
                "a. En tant que locataire, vous acceptez de respecter les règles et les conditions spécifiques établies par chaque locateur pour la location d'espaces sportifs.\n" +
                "b. En tant que locateur, vous vous engagez à fournir des informations précises et à jour sur vos espaces sportifs, ainsi qu'à respecter toutes les réservations confirmées.\n" +
                "\n" +
                "Livraison de produits sportifs :\n" +
                "a. Les livraisons de produits sportifs sont soumises aux conditions de livraison spécifiées par XtraTime et par les fournisseurs tiers.\n" +
                "b. XtraTime n'est pas responsable des retards ou des problèmes liés à la livraison des produits sportifs.\n" +
                "\n" +
                "Contenu utilisateur :\n" +
                "a. Vous êtes seul responsable de tout contenu que vous publiez ou partagez via l'application XtraTime.\n" +
                "b. Vous garantissez que tout contenu que vous publiez respecte les droits de propriété intellectuelle et les droits des tiers.\n" +
                "\n" +
                "Modification et résiliation :\n" +
                "a. XtraTime se réserve le droit de modifier, suspendre ou résilier l'accès à l'application à tout moment et sans préavis.\n" +
                "b. Vous pouvez résilier votre compte à tout moment en suivant les instructions fournies dans l'application.\n" +
                "\n" +
                "En utilisant l'application XtraTime, vous acceptez également notre politique de confidentialité disponible sur notre site Web. Si vous avez des questions concernant ces conditions d'utilisation, veuillez nous contacter à l'adresse suivante : [adresse e-mail de contact].\n" +
                "\n" +
                "Merci d'utiliser XtraTime !\n" +
                "\n");
    }

    @FXML
    private void backFromConditions(ActionEvent event){
        scrollConditions.setVisible(false);
        signUpAnchor.setVisible(true);
        codeVerifAnchor.setVisible(false);
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!/:.;?*-+=";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            stringBuilder.append(chars.charAt(index));
        }
        return stringBuilder.toString();
    }
    private Font getRandomFont() {
        String[] fontFamilies = Font.getFamilies().toArray(new String[0]);
        int randomIndex = (int) (Math.random() * fontFamilies.length);
        String randomFontFamily = fontFamilies[randomIndex];
        double randomFontSize = Math.random() * 20 + 10;
        return Font.font(randomFontFamily, randomFontSize);
    }
    private void setupRandomCaptcha() {
        randomString = generateRandomString(6);
        captcha.setText(randomString);
        captcha.setFont(getRandomFont());
    }
    @FXML
    private void reloadCaptchaOnClick(ActionEvent event){
        setupRandomCaptcha();
    }




}
